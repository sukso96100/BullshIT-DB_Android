package org.bullshitbankdb.android;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BullshITAlertService extends Service {
    String TAG = "BullshITAlertService";
    String PhoneNumber;
    LinearLayout mPopupView;
    private WindowManager.LayoutParams mParams;  //layout params 객체. 뷰의 위치 및 크기
    private WindowManager mWindowManager;
    public BullshITAlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        PhoneNumber = intent.getStringExtra("phone");
        
        Log.d(TAG,"Incoming Call Number:"+PhoneNumber);

        LayoutInflater LI = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);  //윈도우 매니저
        //최상위 윈도우에 넣기 위한 설정
        mParams =new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,//항상 최 상위. 터치 이벤트 받을 수 있음.
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  //포커스를 가지지 않음
                PixelFormat.TRANSLUCENT);                                         //투명
        mParams.gravity = Gravity.CENTER | Gravity.BOTTOM;                   //왼쪽 상단에 위치하게 함.


        mPopupView = (LinearLayout)LI.inflate(R.layout.alert_bullshit,null);
        TextView Phone = (TextView)mPopupView.findViewById(R.id.phone);
        Button Close = (Button)mPopupView.findViewById(R.id.close);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        mPopupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BullshITAlertService.this, DetilActivity.class);
                myIntent.putExtra("phone", PhoneNumber);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                stopSelf();
            }
        });
        Phone.setText(PhoneNumber);


        mWindowManager.addView(mPopupView, mParams);      //윈도우에 뷰 넣기. permission 필요.


        return START_STICKY;
    }
    
    @Override
    public void onCreate(){
        super.onCreate();
        
    }

    @Override
    public void onDestroy() {
        if(mWindowManager != null) {        //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
            if(mPopupView != null) mWindowManager.removeView(mPopupView);
        }
        super.onDestroy();
    }
}
