package com.yixi.window;

import android.content.Intent;
import android.os.Bundle;
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
        startService(myService);
    }

    protected void onDestroy() {
        super.onDestroy();
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
