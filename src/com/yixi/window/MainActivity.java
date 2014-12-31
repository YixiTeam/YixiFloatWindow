package com.yixi.window;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.ab.activity.AbActivity;
import com.ab.util.AbAppUtil;
import com.umeng.update.UmengUpdateAgent;
import com.yixi.window.service.FloatService;

public class MainActivity extends AbActivity {

    private static final String TAG = "MainActivity";
    private Intent myService = null;
    private PowerManager.WakeLock wakeLock = null;

    private Switch mFloatSwitch;
    private static final String ACTION_WINDOW_IS_SHOW = "com.phicomm.WINDOW_IS_SHOW";
    private static final String IS_SHOW = "isShow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main);
        mFloatSwitch = (Switch) findViewById(R.id.float_window_switch);
        mFloatSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton button, boolean check) {
                if (check) {
                    startServiceAndBind();
                } else {
                    stopServiceAndUnbind();
                }
            }
        });
    }

    public void startServiceAndBind() {
        if (AbAppUtil.isServiceRunning(this.getApplicationContext(),
                "com.yixi.window.service.FloatService")) {
            Intent intent = new Intent();
            intent.setAction(ACTION_WINDOW_IS_SHOW);
            intent.putExtra(IS_SHOW, true);
            intent.putExtra("now_change", true);
            sendBroadcast(intent);
        } else {
            myService = new Intent(this, FloatService.class);
            startService(myService);
        }
        if (myService == null) {
            myService = new Intent(this, FloatService.class);
            startService(myService);
        }
    }

    protected void onDestroy() {
        Intent intent = new Intent();
        intent.setAction(ACTION_WINDOW_IS_SHOW);
        intent.putExtra(IS_SHOW, false);
        sendBroadcast(intent);
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
    public void stopServiceAndUnbind() {
        Intent intent = new Intent();
        intent.setAction(ACTION_WINDOW_IS_SHOW);
        intent.putExtra(IS_SHOW, false);
        sendBroadcast(intent);
    }
}
