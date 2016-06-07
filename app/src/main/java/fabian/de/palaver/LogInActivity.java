package fabian.de.palaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity implements OnDownloadFinished{

    private PalaverApplication app;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = (EditText) findViewById(R.id.user_name_login_edit_text);
        password = (EditText) findViewById(R.id.passwort_login_edit_text);
        app = (PalaverApplication) getApplication();
        app.setContext(this);
    }

    public void openRegisterActivity(View view) {
        Intent registerNewAccIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerNewAccIntent);
    }

    public void loginUser(View view) {
        if(username.getText().length() == 0){
            Toast.makeText(this, R.string.empty_username, Toast.LENGTH_LONG).show();
            return;
        }

        else if(password.getText().length() == 0){
            Toast.makeText(this, R.string.wrong_password, Toast.LENGTH_LONG).show();
            return;
        }

        NetworkHelper nwh = new NetworkHelper(this);
        JSONObject json = new JSONObject();
        try {
            json.put("Username", username.getText().toString());
            json.put("Password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nwh.execute("USER_VALIDATE", json.toString());
    }

    @Override
    public void onDownloadFinished(ApiResult json) {
        if(json == null){
            Toast.makeText(this, "ApiResult is NULL", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject serverAnswer = json.getJsonobj();
        try {
            if(serverAnswer.getInt("MsgType") == 0){
                Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_LONG).show();
            }
            else{
                app.setUsername(username.getText().toString());
                app.setPassword(password.getText().toString());

                Intent openContactListIntent = new Intent(this, ContactListActivity.class);
                startActivity(openContactListIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
