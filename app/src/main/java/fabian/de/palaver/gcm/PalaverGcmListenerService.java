package fabian.de.palaver.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import fabian.de.palaver.ChatActivity;
import fabian.de.palaver.PalaverApplication;
import fabian.de.palaver.R;

/**
 * Created by fabian on 15.06.16.
 */
public class PalaverGcmListenerService extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        PalaverApplication app = (PalaverApplication) getApplication();

        Intent startChat = new Intent(this, ChatActivity.class);
        startChat.putExtra("Chat Partner", bundle.getString("sender"));
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, startChat, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.palaver_logo_rund)
                .setContentTitle(bundle.getString("sender"))
                .setContentText(bundle.getString("preview"))
                .setContentIntent(resultIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(app.notificationID++, notificationBuilder.build());

        Intent i = new Intent("de.uni_due.paluno.se.palaver.message." + bundle.getString("sender"));
        sendBroadcast(i);
    }
}
