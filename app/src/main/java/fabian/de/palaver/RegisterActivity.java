package fabian.de.palaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fabian.de.palaver.networking.ApiResult;
import fabian.de.palaver.networking.NetworkHelper;
import fabian.de.palaver.networking.OnDownloadFinished;

public class RegisterActivity extends AppCompatActivity implements OnDownloadFinished {

    private PalaverApplication app;
    private EditText username, password1, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        app = (PalaverApplication) getApplication();
        app.setContext(this);
        username = (EditText) findViewById(R.id.register_user_name_edit_text);
        password1 = (EditText) findViewById(R.id.register_password_edit_text_1);
        password2 = (EditText) findViewById(R.id.register_password_edit_text_2);
    }

    public void registerNewAccount(View view) {

        if(username.getText().toString().length() == 0){
            Toast.makeText(this, R.string.empty_username, Toast.LENGTH_SHORT).show();
            return;
        } else if (!password1.getText().toString().equals(password2.getText().toString()) || password1.getText().toString().length() == 0) {
            Toast.makeText(this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
            return;
        }

        NetworkHelper nwh = new NetworkHelper(this);
        JSONObject json = new JSONObject();
        try {
            json.put("Username", username.getText().toString());
            json.put("Password", password1.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        nwh.execute(NetworkHelper.ApiCommand.USER_REGISTER.toString(), json.toString());
    }

    @Override
    public void onDownloadFinished(ApiResult json) {
        JSONObject serverAnswer = json.getJsonobj();

        try {
            if(serverAnswer.getInt("MsgType") == 0){
                Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_LONG).show();
            }
            else{
                app.setUsername(username.getText().toString());
                app.setPassword(password1.getText().toString());
                Intent openContactListIntent = new Intent(this, ContactListActivity.class);
                startActivity(openContactListIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
