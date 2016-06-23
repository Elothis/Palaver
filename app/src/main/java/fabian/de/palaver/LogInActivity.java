package fabian.de.palaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fabian.de.palaver.gcm.TokenService;
import fabian.de.palaver.networking.ApiResult;
import fabian.de.palaver.networking.NetworkHelper;
import fabian.de.palaver.networking.OnDownloadFinished;

public class LogInActivity extends AppCompatActivity implements OnDownloadFinished {

    private PalaverApplication app;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = (EditText) findViewById(R.id.user_name_login_edit_text);
        password = (EditText) findViewById(R.id.passwort_login_edit_text);
        username.setText("");
        password.setText("");
        app = (PalaverApplication) getApplication();
        app.setContext(this);
        String user = app.getUserName();

        if(app.getLoggedIn()){
            sendLogInRequest(app.getUserName(), app.getPassword());
        }
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

        sendLogInRequest(username.getText().toString(), password.getText().toString());
    }

    @Override
    public void onDownloadFinished(ApiResult json) {

        JSONObject serverAnswer = json.getJsonobj();
        try {
            if(serverAnswer.getInt("MsgType") == 0){
                Toast.makeText(this, serverAnswer.getString("Info"), Toast.LENGTH_LONG).show();
                app.setLoggedIn(false);
            }
            else{
                app.setLoggedIn(true);

                //Intent msgIntent = new Intent(this, TokenService.class);
                //startService(msgIntent);

                Intent openContactListIntent = new Intent(this, ContactListActivity.class);
                startActivity(openContactListIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendLogInRequest(String username, String password){
        NetworkHelper nwh = new NetworkHelper(this);
        JSONObject json = new JSONObject();
        try {
            json.put("Username", username);
            json.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        app.setUsername(username);
        app.setPassword(password);
        nwh.execute(NetworkHelper.ApiCommand.USER_VALIDATE.toString(), json.toString());
    }
}
