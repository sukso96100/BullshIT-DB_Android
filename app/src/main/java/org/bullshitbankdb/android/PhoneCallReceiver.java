package org.bullshitbankdb.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class PhoneCallReceiver extends BroadcastReceiver {
    public PhoneCallReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean PhoneCallAlert = Pref.getBoolean("call_alert",false);
        final String PhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if(PhoneCallAlert){
            Intent myIntent = new Intent(context, DetilActivity.class);
            myIntent.putExtra("phone",PhoneNumber);
            final PendingIntent pendingIntent
                    = PendingIntent.getActivity(context,0,myIntent, 0);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
            query.whereStartsWith("phone", PhoneNumber);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(parseObjects.size()>0){
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                .setContentTitle(PhoneNumber)
                                .setContentText(context.getResources().getString(R.string.returns_true))
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.drawable.ic_noti_bullshitbank);
                        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0,builder.build());
                    }
                }
            });
        }else{}
    }
}
