package fabian.de.palaver;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PalaverApplication extends Application {

    private SharedPreferences sharedPrefs;
    public static final String MYPREFERENCES = "palaver_preferences";
    public static final String DEFVALUE = "Error";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    Context context;

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
        editor.commit();
    }

    public String getUserName(){
        return sharedPrefs.getString(USERNAME, DEFVALUE);
    }

    public void setPassword(String password){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getPassword(){
        return sharedPrefs.getString(PASSWORD, DEFVALUE);
    }
}
