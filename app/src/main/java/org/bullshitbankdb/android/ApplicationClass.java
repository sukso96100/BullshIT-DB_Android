package org.bullshitbankdb.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.Parse;

import java.util.Calendar;

/**
 * Created by youngbin on 15. 2. 21.
 */
public class ApplicationClass extends Application {
    private String TAG = "ApplicationClass";
    SharedPreferences Pref;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Parse SDK
        Log.d(TAG, "Initializing Parse SDK");
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "TK6eb71TLg0APbyMsHiSaGEbW3jfG7ItMr6GFaLE", "hhKVpCMilgjCtrWqtwc5nno7hoA2W0AMrpGzLY01");

        GuidTool GT = new GuidTool(ApplicationClass.this);
    }
}
