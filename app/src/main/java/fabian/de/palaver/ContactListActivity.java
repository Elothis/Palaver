package fabian.de.palaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.util.Collections.sort;

public class ContactListActivity extends AppCompatActivity implements OnDownloadFinished, AddContactInterface{

    private PalaverApplication app;
    private ArrayAdapter<String> adapter;
    private AddContactDialog dialog;
    private AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_contact_list);

        app = (PalaverApplication) getApplication();
        app.setContext(this);

        ListView contactList = (ListView) findViewById(R.id.contact_list_view);
        setFriendsList();

        contactList.setLongClickable(true);


        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactSelected = String.valueOf(parent.getItemAtPosition(position));
                Intent startChatIntent = new Intent(ContactListActivity.this, ChatActivity.class);
                startChatIntent.putExtra("Chat Partner", contactSelected);
                startActivity(startChatIntent);
            }
        });

        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactListActivity.this);
                builder.setTitle(R.string.remove_friend_dialog);
                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkHelper nwh = new NetworkHelper(ContactListActivity.this);
                        JSONObject json = new JSONObject();
                        try {
                            json.put("Username", app.getUserName());
                            json.put("Password", app.getPassword());
                            json.put("Friend", adapter.getItem(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nwh.execute(NetworkHelper.ApiCommand.FRIENDS_REMOVE.toString(), json.toString());
                    }
                });

                builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.dismiss();
                    }
                });

                deleteDialog = builder.show();

                return true;
            }
        });

        }

    @Override
    public void onResume(){
        super.onResume();
        setFriendsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_bar_logout){
            app.setUsername("");
            app.setPassword("");

            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
        }

        else if(id == R.id.action_bar_add_contact){
            dialog = new AddContactDialog();

            dialog.show(getFragmentManager(), "Add Contact");
        }

        else if(id == R.id.action_bar_refresh){
            Log.v("mytag", "refresh clicked");
            setFriendsList();
        }
        return false;
    }

    private void setFriendsList(){
            Log.v("mytag", "setFriendsList()");
            NetworkHelper nwh = new NetworkHelper(this);
            JSONObject json = new JSONObject();
            try {
                json.put("Username", app.getUserName());
                Log.v("mytag", "userName in setFriendsList() = " + app.getUserName());
                json.put("Password", app.getPassword());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            nwh.execute(NetworkHelper.ApiCommand.FRIENDS_GET.toString(), json.toString());
    }

    @Override
    public void onDownloadFinished(ApiResult json) {
        JSONObject serverAnswer = json.getJsonobj();
        NetworkHelper.ApiCommand command = json.getApicmd();

        try {
            switch (command) {
                case FRIENDS_GET:
                    if (serverAnswer.getInt("MsgType") == 1) {
                        JSONArray array = serverAnswer.getJSONArray("Data");
                        ArrayList<String> friends = new ArrayList<>();
                        for(int i = 0; i < array.length(); i++){
                            friends.add(array.getString(i));
                        }
                        sort(friends);
                        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
                        ((ListView)findViewById(R.id.contact_list_view)).setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(this, "FGET " + serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                        Log.v("mytag", "FRIENDS_GET failed" + " - " + serverAnswer.getString("Info"));
                    }
                    break;
                case FRIENDS_ADD:
                    if(serverAnswer.getInt("MsgType") == 0){
                        Toast.makeText(this, "FADD " + serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                        Log.v("mytag", "FRIENDS_ADD failed" + " - " + serverAnswer.getString("Info"));
                    }
                    else{
                        Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                        Log.v("mytag", "FRIENDS_ADD worked");
                        setFriendsList();
                    }
                    break;
                case FRIENDS_REMOVE:
                    if(serverAnswer.getInt("MsgType") == 0){
                        Toast.makeText(this, "FREM " + serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                        Log.v("mytag", "FRIENDS_REMOVE failed" + " - " + serverAnswer.getString("Info"));
                    }
                    else{
                        Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_SHORT).show();
                        Log.v("mytag", "FRIENDS_REMOVE worked");
                        setFriendsList();
                    }
                    break;
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addContact() {
        EditText et  = (EditText) dialog.getDialog().findViewById(R.id.add_contact_dialog_edit_text);
        String friendToAdd = et.getText().toString();
        if(friendToAdd.length() == 0){
            Toast.makeText(this, "Bitte gib einen Nutzernamen zum hinzufügen ein.", Toast.LENGTH_SHORT).show();
            return;
        }

        NetworkHelper nwh = new NetworkHelper(this);
        JSONObject json = new JSONObject();
        try {
            json.put("Username", app.getUserName());
            json.put("Password", app.getPassword());
            json.put("Friend", friendToAdd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nwh.execute(NetworkHelper.ApiCommand.FRIENDS_ADD.toString(), json.toString());
    }
}
