package fabian.de.palaver.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;

import java.io.IOException;

import fabian.de.palaver.PalaverApplication;
import fabian.de.palaver.networking.ApiResult;
import fabian.de.palaver.networking.OnDownloadFinished;

/**
 * Created by fabian on 15.06.16.
 */
public class TokenService extends IntentService{

    public TokenService() {
        super("TokenService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken("594324547505", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            ((PalaverApplication)getApplication()).sendTokenToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
