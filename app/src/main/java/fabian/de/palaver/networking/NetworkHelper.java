package fabian.de.palaver.networking;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkHelper extends AsyncTask<String, Integer, ApiResult> {

    public enum ApiCommand{
        USER_VALIDATE, USER_REGISTER, USER_PASSWORD, USER_PUSHTOKEN,
        MESSAGE_SEND, MESSAGE_GET, MESSAGE_GETOFFSET,
        FRIENDS_ADD, FRIENDS_REMOVE, FRIENDS_GET

    }

    private OnDownloadFinished activityToNotify;

    public NetworkHelper(OnDownloadFinished activityToNotify) {
        this.activityToNotify = activityToNotify;
    }

    public URL getApiUrl(ApiCommand command){

        String urlString = "http://palaver.se.paluno.uni-due.de/api";

            switch (command) {
                case USER_VALIDATE:
                    urlString += "/user/validate";
                    break;
                case USER_REGISTER:
                    urlString += "/user/register";
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
        ApiCommand apiCommand = ApiCommand.valueOf(strings[0]);
        URL url = getApiUrl(apiCommand);
        try {
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(strings[1]);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String text = "";
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }
            text = sb.toString();
            reader.close();
            return new ApiResult(apiCommand, new JSONObject(text));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(ApiResult apiResult) {
        if(activityToNotify != null) {
            activityToNotify.onDownloadFinished(apiResult);
        }
    }

    public OnDownloadFinished getActivityToNotify() {
        return activityToNotify;
    }
}
