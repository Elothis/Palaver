package fabian.de.palaver.gcm;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by fabian on 15.06.16.
 */
public class TokenService extends IntentService {

    public TokenService() {
        super("TokenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
