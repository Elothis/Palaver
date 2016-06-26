package fabian.de.palaver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

public class ChatActivity extends AppCompatActivity implements OnDownloadFinished {

    PalaverApplication app;
    private EditText messageEditText;
    private String chatPartner;
    private ListView chatListView;
    private ImageButton sendButton;
    private List<ChatMessage> chatList = new ArrayList<>();
    private ChatAdapter adapter;

    private boolean receiverRegistered = false;
    private MessageReceiver receiver = null;

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

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiverRegistered){
            unregisterReceiver(receiver);
            receiverRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!receiverRegistered){
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

        if(id == R.id.action_bar_logout){
            app.setUsername("");
            app.setPassword("");
            app.setLoggedIn(false);

            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
        }

        else if(id == R.id.action_bar_refresh){
            getChatHistory();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialiizeWidgets(){
        app = (PalaverApplication) getApplication();
        app.setContext(this);
        messageEditText = (EditText) findViewById(R.id.chat_message_edit_text);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    sendButton.setEnabled(false);
                }
                else{
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        sendButton = (ImageButton) findViewById(R.id.send_message_image_button);
        sendButton.setEnabled(false);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage();
            }
        });
    }

    private void sendTextMessage(){
        String message = messageEditText.getText().toString();
        if(!message.isEmpty()){
            NetworkHelper nwh = new NetworkHelper(this);
            JSONObject json = new JSONObject();
            try {
                json.put("Username", app.getUserName());
                json.put("Password", app.getPassword());
                json.put("Recipient", chatPartner);
                json.put("Mimetype", "text/plain");
                json.put("Data", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            nwh.execute(NetworkHelper.ApiCommand.MESSAGE_SEND.toString(), json.toString());
        }
    }

    private void getChatHistory(){
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

                        for(int i = 0; i < jarray.length(); i++){
                            jitem = jarray.getJSONObject(i);
                            String sender = jitem.getString("Sender");
                            String recipient = jitem.getString("Recipient");
                            String mimetype = jitem.getString("Mimetype");
                            String data = jitem.getString("Data");
                            Date date = dateFormat.parse(jitem.getString("DateTime"));

                            chatList.add(new ChatMessage(sender, recipient, date, mimetype, data));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MESSAGE_SEND:
                    if(serverAnswer.getInt("MsgType") == 1){
                        getChatHistory();
                        messageEditText.setText("");
                    }
                    else{
                        Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        catch(JSONException | ParseException e){
            e.printStackTrace();
        }
    }

    public void removeNotifications(){
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    private class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            getChatHistory();
            removeNotifications();
        }
    }

}

