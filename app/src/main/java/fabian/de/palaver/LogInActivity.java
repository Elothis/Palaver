package fabian.de.palaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void openRegisterActivity(View view) {
        Intent registerNewAccIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerNewAccIntent);
    }

    public void loginUser(View view) {
        Intent openContactListIntent = new Intent(this, ContactListActivity.class);
        startActivity(openContactListIntent);
    }
}
