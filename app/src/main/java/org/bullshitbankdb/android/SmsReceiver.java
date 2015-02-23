package org.bullshitbankdb.android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SmsReceiver extends BroadcastReceiver {
    String PhoneNumber;
    String TAG = "SmsReceiver";
    public SmsReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null){
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                PhoneNumber = msgs[i].getOriginatingAddress();
                Log.d(TAG, "Phone Number: " + PhoneNumber);
            }

            SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(context);
            Boolean SmsAlert = Pref.getBoolean("sms_alert",true);
            if(SmsAlert){
                
                Intent myIntent = new Intent(context, DetilActivity.class);
                myIntent.putExtra("phone",PhoneNumber);
                final PendingIntent pendingIntent
                        = PendingIntent.getActivity(context,0,myIntent, 0);

                try{
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
                    query.whereEqualTo("phone", PhoneNumber);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(parseObject!=null){
                                Log.d(TAG,"It's BullshIT!");
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                        .setContentTitle(PhoneNumber)
                                        .setContentText(context.getResources().getString(R.string.returns_true))
                                        .setContentIntent(pendingIntent)
                                        .setSmallIcon(R.drawable.ic_noti_bullshitbank);
                                NotificationManager notificationManager
                                        = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0,builder.build());
                                Intent alertIntent = new Intent(context, BullshITAlertService.class);
                                alertIntent.putExtra("phone",PhoneNumber);

                                try{
                                    context.stopService(alertIntent);
                                }catch(Exception ee){}
                                context.startService(alertIntent);
                            }else{}
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{}
        }



    
    }
}
