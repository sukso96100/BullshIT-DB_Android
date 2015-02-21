package org.bullshitbankdb.android;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by youngbin on 15. 2. 21.
 */
public class ApplicationClass extends Application{
    String TAG = "ApplicationClass";
    @Override
    public void onCreate(){
        super.onCreate();

        Log.d(TAG, "Initializing Parse SDK");
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "TK6eb71TLg0APbyMsHiSaGEbW3jfG7ItMr6GFaLE", "hhKVpCMilgjCtrWqtwc5nno7hoA2W0AMrpGzLY01");
    }
}
