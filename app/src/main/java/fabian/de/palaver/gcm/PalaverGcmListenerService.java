package fabian.de.palaver.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by fabian on 15.06.16.
 */
public class PalaverGcmListenerService extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        Intent i = new Intent("de.uni_due.paluno.se.palaver.message" + bundle.getString("594324547505"));
        sendBroadcast(i);

        Log.v("mytag", bundle.toString());
    }
}
