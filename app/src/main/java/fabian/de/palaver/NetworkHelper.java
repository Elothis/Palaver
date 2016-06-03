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

        String urlString = "http://palaver.se.paluno.uni-due.de/api";

            switch (command) {
                case USER_VALIDATE:
                    urlString += "/user/register";
                    break;
                case USER_REGISTER:
                    urlString += "/user/validate";
                    break;
                case USER_PASSWORD:
                    urlString += "/user/password";
                    break;
                case USER_PUSHTOKEN:
                    urlString += "/user/pushtoken";
                    break;
                case MESSAGE_SEND:
                    urlString += "/message/send";
                    break;
                case MESSAGE_GET:
                    urlString += "/message/get";
                    break;
                case MESSAGE_GETOFFSET:
                    urlString += "/message/getoffset";
                    break;
                case FRIENDS_ADD:
                    urlString += "/friends/add";
                    break;
                case FRIENDS_GET:
                    urlString += "/friends/get";
                    break;
                case FRIENDS_REMOVE:
                    urlString += "/friends/remove";
                    break;
                default:
                    break;
            }
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
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
