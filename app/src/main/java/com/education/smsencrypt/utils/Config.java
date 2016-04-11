package com.education.smsencrypt.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Three Hero on 19/08/2015.
 */
public class Config {
    private Activity activity;

    public Config(Activity activity){
        this.activity = activity;
    }


    /*
   |-----------------------------------------------------------------------------------------------
   | URL server for this application to transfer data.
   |-----------------------------------------------------------------------------------------------
    */
    public static final String url_Server = "192.168.177.89";

    /*
   |-----------------------------------------------------------------------------------------------
   | URL server local for this application to transfer data.
   |-----------------------------------------------------------------------------------------------
    */
    public static final String url_Server_local = "192.168.177.89";
    public static final String url_server_proxy = "192.168.220.32";

    /*
  |-----------------------------------------------------------------------------------------------
  | Port server for this application to transfer data.
  |-----------------------------------------------------------------------------------------------
   */
    public static final String port_Server = "";
    public static final String port_Server_proxy = "3128";
    public static final String version = "1.8"; // ari

    /*
   |-----------------------------------------------------------------------------------------------
   | Key for access token
   |-----------------------------------------------------------------------------------------------
   */
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_LOGIN_PROFILE = "LOGIN_PROFILE";

    /*
   |-----------------------------------------------------------------------------------------------
   | Key for json
   |-----------------------------------------------------------------------------------------------
   */
    public static final String KEY_JSON_PROFILE = "json";

    /*
   |-----------------------------------------------------------------------------------------------
   | Key for imei
   |-----------------------------------------------------------------------------------------------
   */
    public static final String KEY_IMEI = "imei";


    /*
   |-----------------------------------------------------------------------------------------------
   | Database name
   |-----------------------------------------------------------------------------------------------
   */
    public static final String DATABASE_NAME = "SMSEncrypt";

    /*
    |-----------------------------------------------------------------------------------------------
    | Database version
    |-----------------------------------------------------------------------------------------------
    */
    public static final int DATABASE_VERSION = 2;

    /*
  |-----------------------------------------------------------------------------------------------
  | Database delay time
  |-----------------------------------------------------------------------------------------------
  */
    public static final int DB_CLOSE_DELAY_TIME = 5000;

    /*
   |-----------------------------------------------------------------------------------------------
   | Key for json respon
   |-----------------------------------------------------------------------------------------------
   */

    public void setTransclusent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = activity.getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}

