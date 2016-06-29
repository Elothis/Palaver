package fabian.de.palaver;

import android.*;
import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fabian.de.palaver.networking.ApiResult;
import fabian.de.palaver.networking.NetworkHelper;
import fabian.de.palaver.networking.OnDownloadFinished;

public class ChatActivity extends AppCompatActivity implements OnDownloadFinished, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    PalaverApplication app;
    private EditText messageEditText;
    private String chatPartner;
    private ListView chatListView;
    private ImageButton sendButton;
    private List<ChatMessage> chatList = new ArrayList<>();
    private ChatAdapter adapter;
    public static final String LOCATION_SHARED = "hat seinen Standort geteilt";
    private final int REQUEST_PERMISSION = 123;

    private boolean receiverRegistered = false;
    private MessageReceiver receiver = null;

    GoogleApiClient mGoogleApiClient = null;
    Location lastLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initialiizeWidgets();
        chatPartner = getIntent().getExtras().getString("Chat Partner");
        setTitle(chatPartner);
        app = (PalaverApplication) getApplication();
        app.setContext(this);

        adapter = new ChatAdapter(this, chatList, app);
        chatListView.setAdapter(adapter);
        receiver = new MessageReceiver();

        getChatHistory();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiverRegistered) {
            unregisterReceiver(receiver);
            receiverRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!receiverRegistered) {
            registerReceiver(receiver, new IntentFilter("de.uni_due.paluno.se.palaver.message." + chatPartner));
        }
        removeNotifications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_bar_logout) {
            app.setUsername("");
            app.setPassword("");
            app.setLoggedIn(false);

            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
        } else if (id == R.id.action_bar_location) {
            sendMessage(false);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialiizeWidgets() {
        app = (PalaverApplication) getApplication();
        app.setContext(this);
        messageEditText = (EditText) findViewById(R.id.chat_message_edit_text);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ChatMessage message = (ChatMessage) adapterView.getItemAtPosition(i);
                if (message.getMimetype().equals("location/plain")) {
                    Toast.makeText(getApplicationContext(), message.getMessageText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendButton = (ImageButton) findViewById(R.id.send_message_image_button);
        sendButton.setEnabled(false);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(true);
            }
        });
    }

    private void sendMessage(boolean textMessage) {
        String message = messageEditText.getText().toString();
        String mimeType = textMessage ? "text/plain" : "location/plain";

        Log.v("mytag", mimeType);

        if (!message.isEmpty() || mimeType.equals("location/plain")) {
            NetworkHelper nwh = new NetworkHelper(this);
            JSONObject json = new JSONObject();
            try {
                json.put("Username", app.getUserName());
                json.put("Password", app.getPassword());
                json.put("Recipient", chatPartner);
                json.put("Mimetype", mimeType);
                if (textMessage) json.put("Data", message);
                else
                    json.put("Data", lastLocation.getAltitude() + ":" + lastLocation.getLatitude() + ":" + lastLocation.getLongitude());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            nwh.execute(NetworkHelper.ApiCommand.MESSAGE_SEND.toString(), json.toString());
        }
    }


    private void getChatHistory() {
        NetworkHelper nwh = new NetworkHelper(this);
        JSONObject json = new JSONObject();
        try {
            json.put("Username", app.getUserName());
            json.put("Password", app.getPassword());
            json.put("Recipient", chatPartner);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nwh.execute(NetworkHelper.ApiCommand.MESSAGE_GET.toString(), json.toString());
    }

    @Override
    public void onDownloadFinished(ApiResult json) {
        NetworkHelper.ApiCommand command = json.getApicmd();
        JSONObject serverAnswer = json.getJsonobj();

        try {
            switch (command) {
                case MESSAGE_GET:
                    if (serverAnswer.getInt("MsgType") == 1) {
                        chatList.clear();
                        JSONArray jarray = serverAnswer.getJSONArray("Data");
                        JSONObject jitem;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

                        for (int i = 0; i < jarray.length(); i++) {
                            jitem = jarray.getJSONObject(i);
                            String sender = jitem.getString("Sender");
                            String recipient = jitem.getString("Recipient");
                            String mimetype = jitem.getString("Mimetype");
                            boolean textMessage = mimetype.equals("text/plain");
                            String data = jitem.getString("Data");
                            Date date = dateFormat.parse(jitem.getString("DateTime"));

                            chatList.add(new ChatMessage(sender, recipient, date, mimetype, data));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MESSAGE_SEND:
                    if (serverAnswer.getInt("MsgType") == 1) {
                        getChatHistory();
                        messageEditText.setText("");
                    } else {
                        Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void removeNotifications() {
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {

                    Toast.makeText(this, "Location Services denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            getChatHistory();
            removeNotifications();
        }
    }

}

