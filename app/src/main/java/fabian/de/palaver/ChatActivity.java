package fabian.de.palaver;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    PalaverApplication app;
    private EditText messageEditText;
    private String chatPartner;
    private ListView chatListView;
    private ImageButton sendButton;
    private List<ChatMessage> chatList;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initialiizeWidgets();
        chatPartner = getIntent().getExtras().getString("Chat Partner");
        setTitle(chatPartner);

        chatList = new ArrayList<>();
        adapter = new ChatAdapter(this, chatList, app);
        chatListView.setAdapter(adapter);

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
            Intent logOut = new Intent(this, LogInActivity.class);
            startActivity(logOut);
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
                sendTextMessage(v);
            }
        });
    }

    private void sendTextMessage(View v){
        String message = messageEditText.getEditableText().toString();
        if(!message.isEmpty()){
            ChatMessage chatMessage = new ChatMessage(app.getUserName(), chatPartner, Calendar.getInstance().getTime(), message);
            chatList.add(chatMessage);
            adapter.notifyDataSetChanged();
            messageEditText.setText("");

            //dummy echo-message from chat partner
            ChatMessage chatMessageEcho = new ChatMessage(chatPartner, app.getUserName(), Calendar.getInstance().getTime(), message);
            chatList.add(chatMessageEcho);
            adapter.notifyDataSetChanged();
        }
    }
}
