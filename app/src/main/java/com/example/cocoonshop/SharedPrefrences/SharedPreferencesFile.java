package com.example.cocoonshop.SharedPrefrences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesFile {


    private String TAG = "SessionManager";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;
    public static Context _context;
    public static final String PREF_NAME = "WalkthroughtScreen" ;

    private static final String WALKTHROUGHT_KEY = "WalkthroughKEY" ;


    // create session ----------------------------------------------------------------------------------------

    public void SessionManager(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    // WalkThrought check  ----------------------------------------------------------------------------------------

    public boolean getWalkthroughtFirstTime(){
        return sharedPreferences.getBoolean(WALKTHROUGHT_KEY, true);
    }
    public void setWalkthrought(boolean isFirstTime){
        editor.putBoolean(WALKTHROUGHT_KEY, isFirstTime);
        editor.apply();
    }


}
