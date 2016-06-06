package fabian.de.palaver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity implements OnDownloadFinished{

    private PalaverApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        app = (PalaverApplication) getApplication();
        app.setContext(this);
    }

    public void registerNewAccount(View view) {
        Intent openContactListIntent = new Intent(this, ContactListActivity.class);
        startActivity(openContactListIntent);
    }

    @Override
    public void onDownloadFinished(ApiResult json) {

    }
}
