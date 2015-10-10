package com.catchingnow.utils.debug;

import android.util.Log;

/**
 * Created by Heaven on 9/23/15.
 */
public class MyLog {
    public static final boolean DEBUG = true;
    public static final boolean UNDEBUG = false;
    public static boolean mode = DEBUG;
    public static void d(String tag, String msg){
        if (mode){
            Log.d(tag, msg);
        }
    }
    public static void i(String tag, String msg){
        if (mode){
            Log.i(tag, msg);
        }
    }
}
