package fabian.de.palaver;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fabian.de.palaver.networking.NetworkHelper;
import fabian.de.palaver.networking.OnDownloadFinished;

public class PalaverApplication extends Application {

    private SharedPreferences sharedPrefs;
    public static final String MYPREFERENCES = "palaver_preferences";
    public static final String DEFVALUE = "Not saved";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FRIENDS = "friends";
    public static final String LOGGEDIN = "logged_in";
    private Context context;

    public void sendTokenToServer(String gcmID){
        NetworkHelper nwh = new NetworkHelper(null);
        JSONObject json = new JSONObject();

        try {
            json.put("Username", getUserName());
            json.put("Password", getPassword());
            json.put("PushToken", gcmID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nwh.execute(NetworkHelper.ApiCommand.USER_PUSHTOKEN.toString(), json.toString());
    }


    public void setContext(Context context){
        this.context = context;
        sharedPrefs = context.getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
    }

    public SharedPreferences getSharedPrefs(){
        return sharedPrefs;
    }

    public void setUsername(String userName){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(USERNAME, userName);
        editor.apply();
    }

    public String getUserName(){
        return sharedPrefs.getString(USERNAME, DEFVALUE);
    }

    public void setPassword(String password){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public String getPassword(){
        return sharedPrefs.getString(PASSWORD, DEFVALUE);
    }

    public void setLoggedIn(boolean b){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(LOGGEDIN, b);
        editor.apply();
    }

    public boolean getLoggedIn(){
        return sharedPrefs.getBoolean(LOGGEDIN, false);
    }
}
