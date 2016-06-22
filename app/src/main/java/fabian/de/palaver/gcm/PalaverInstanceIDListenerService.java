package fabian.de.palaver.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by fabian on 15.06.16.
 */
public class PalaverInstanceIDListenerService extends InstanceIDListenerService{

    @Override
    public void onTokenRefresh() {
        Intent msgIntent = new Intent(this, TokenService.class);
        startService(msgIntent);
    }
}
