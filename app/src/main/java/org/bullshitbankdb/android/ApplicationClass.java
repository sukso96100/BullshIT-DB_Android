package org.bullshitbankdb.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.Parse;

import java.util.Calendar;

/**
 * Created by youngbin on 15. 2. 21.
 */
public class ApplicationClass extends Application{
    private String TAG = "ApplicationClass";
    SharedPreferences Pref;
    public String GUID;
    @Override
    public void onCreate(){
        super.onCreate();

        //Initialize Parse SDK
        Log.d(TAG, "Initializing Parse SDK");
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "TK6eb71TLg0APbyMsHiSaGEbW3jfG7ItMr6GFaLE", "hhKVpCMilgjCtrWqtwc5nno7hoA2W0AMrpGzLY01");

        //Get GUID
        Pref = getSharedPreferences("pref",MODE_PRIVATE);
        GUID = Pref.getString("guid","NOGUID");
        if(GUID.equals("NOGUID")){
            //No GUID? Create New One
            GUID = createGUID();
            SharedPreferences.Editor Editor = Pref.edit();
            Editor.putString("guid",GUID);
            Editor.commit();
        }
        Log.d(TAG,"Your GUID Value : "+GUID);
    }

    //Create GUID Based On Current Time
    public String createGUID(){
        Calendar c = Calendar.getInstance();
        String NewGUID = "GUID"
                + c.get(Calendar.YEAR)
                + c.get(Calendar.MONTH)
                + c.get(Calendar.DAY_OF_MONTH)
                + c.get(Calendar.HOUR_OF_DAY)
                + c.get(Calendar.MINUTE)
                + c.get(Calendar.MILLISECOND);
        return NewGUID;
    }
}
