package com.yixi.window;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

import com.ab.activity.AbActivity;
import com.ab.util.AbAppUtil;
import com.yixi.window.service.FloatService;
import com.yixi.window.service.IService;

public class MainActivity extends AbActivity {

    //private String TAG = "ss";
    private IService mRemoteService;
    private Intent myService = null;
    private PowerManager.WakeLock wakeLock = null;

    //private int currentFrequency = 0;
    //private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mTextView = (TextView)findViewById(R.id.test_data);
        startServiceAndBind();
        //acquireWakeLock();
        finish();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/

    public void startServiceAndBind() {
        if (AbAppUtil.isServiceRunning(this.getApplicationContext(),
                "com.yixi.service.FloatService")) {
        } else {
            myService = new Intent(this, FloatService.class);
            startService(myService);
        }

        if (myService == null) {
            myService = new Intent(this.getApplicationContext(),
                    FloatService.class);
        }
        myService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bindService(myService, sc, BIND_AUTO_CREATE);
    }

    private ServiceConnection sc = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteService = IService.Stub.asInterface(service);
            try {
                if (mRemoteService != null) {
//                    mRemoteService.setUser(String.valueOf(application.mUser
//                            .getuId()));
                    mRemoteService.startCalorie();
                    //Thread mUpdateDesktopThread = new Thread(mUpdateRunnable);
                    //mUpdateDesktopThread.start();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteService = null;
        }
    };

    /*public void showCalorie() {
        int frequency = 0;
        double calorie = 0;
        double distance = 0;
        try {
            frequency = mRemoteService.getFrequency();
            calorie = mRemoteService.getCalorie();
            distance = mRemoteService.getDistance();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (frequency != currentFrequency) {
            currentFrequency = frequency;
            String text = "step:"+frequency+"___calorie:"+calorie+"____distance:"+distance;
            mTextView.setText(text);
        }
    }

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
        // wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK |
        // PowerManager.ACQUIRE_CAUSES_WAKEUP;
        // wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK;
        wakeFlags = PowerManager.SCREEN_BRIGHT_WAKE_LOCK;
        wakeLock = pm.newWakeLock(wakeFlags, this.getClass().getCanonicalName());
        wakeLock.acquire();
    }

    private Handler mUpdateHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                showCalorie();
                break;
            }
            super.handleMessage(msg);
        }
    };

    private Runnable mUpdateRunnable = new Runnable() {
        public void run() {
            while (true) {
                Message message = new Message();
                message.what = 1;
                mUpdateHandler.sendMessage(message);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    };*/

    protected void onDestroy() {
        super.onDestroy();
        if (sc != null) {
            unbindService(sc);
        }
    };

    @Override
    public void finish() {
        try {
            if (wakeLock != null) {
                wakeLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish();
    }
}
