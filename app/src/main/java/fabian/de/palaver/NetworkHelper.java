package fabian.de.palaver;

import android.os.AsyncTask;
import android.widget.Button;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkHelper extends AsyncTask<String, Integer, ApiResult> {

    public enum ApiCommand{
        USER_VALIDATE, USER_REGISTER, USER_PASSWORD, USER_PUSHTOKEN,
        MESSAGE_SEND, MESSAGE_GET, MESSAGE_GETOFFSET,
        FRIENDS_ADD, FRIENDS_REMOVE, FRIENDS_GET

    }

    private Button toDisable;
    private ApiCommand command;

    public NetworkHelper(Button toDisable, ApiCommand command) {
        this.toDisable = toDisable;
        this.command = command;
    }

    public URL getApiUrl(ApiCommand command){

        URL result = null;

        try {

            switch (command) {
                case USER_VALIDATE:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/user/register");
                    break;
                case USER_REGISTER:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/user/validate");
                    break;
                case USER_PASSWORD:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/user/password");
                    break;
                case USER_PUSHTOKEN:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/user/pushtoken");
                    break;
                case MESSAGE_SEND:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/message/send");
                    break;
                case MESSAGE_GET:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/message/get");
                    break;
                case MESSAGE_GETOFFSET:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/message/getoffset");
                    break;
                case FRIENDS_ADD:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/friends/add");
                    break;
                case FRIENDS_GET:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/friends/get");
                    break;
                case FRIENDS_REMOVE:
                    result = new URL("http://palaver.se.paluno.uni-due.de/api/friends/remove");
                    break;
                default:
                    result = null;
                    break;
            }
            return result;
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected ApiResult doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (toDisable != null){
            toDisable.setEnabled(false);
        }
    }

    @Override
    protected void onPostExecute(ApiResult apiResult) {
        if (toDisable != null){
            toDisable.setEnabled(true);
        }
    }

}
