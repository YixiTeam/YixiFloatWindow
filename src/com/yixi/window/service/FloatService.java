package com.yixi.window.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.ab.util.AbDateUtil;
import com.yixi.window.config.AppData;
import com.yixi.window.FloatWindowManager;

public class FloatService extends Service {

    private SensorEventListener mCalorieListener = null;
    private SensorManager mSensorMgr;

    private AppData mAppData = null;
    private Handler handler = new Handler();
    private Timer timer;
    private FloatWindowManager mFloatWindowManager;
    private Context mContext;
    private boolean mWindowShow = true;
    private boolean mShow = false;
    private static final String ACTION_WINDOW_IS_SHOW = "com.phicomm.WINDOW_IS_SHOW";
    private static final String WINDOW_IS_SHOW = "window_is_show";
    private static final String WINDOW_DATA = "window_data";
    private static final String IS_SHOW = "isShow";
    private SharedPreferences sharedata;
    private SharedPreferences.Editor editordata;

    @Override
    public void onCreate() {
        mSensorMgr = (SensorManager) this
                .getSystemService(android.content.Context.SENSOR_SERVICE);
        mContext=this.getApplicationContext();
        mFloatWindowManager = new FloatWindowManager(mContext);
        IntentFilter filter=new IntentFilter(ACTION_WINDOW_IS_SHOW);
        registerReceiver(mBroadcastReceiver, filter);
        mAppData = new AppData(this);
        editordata=getSharedPreferences(WINDOW_DATA, 0).edit();
        sharedata= getSharedPreferences(WINDOW_DATA, 0);
        if(sharedata==null){
            mWindowShow = true;
        }else{
            mWindowShow = sharedata.getBoolean(WINDOW_IS_SHOW, true);
        }
        init();
        startStep();
        super.onCreate();
    }

    public void init() {
        String todayYMD = AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMD);
    }

    public void stopStep() {
        if (mSensorMgr != null && mCalorieListener != null) {
            Sensor sensor = mSensorMgr
                    .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorMgr.unregisterListener(mCalorieListener, sensor);
        }
    }

    public void startStep() {
        if (mSensorMgr != null && mCalorieListener != null) {
            Sensor sensor = mSensorMgr
                    .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorMgr.registerListener(mCalorieListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void saveCacheData() {
    }

    private void updateDesktops() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        this.init();
        /*if (timer == null) {
            timer = new Timer();
        }
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);*/
        mFloatWindowManager.createSmallWindow(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mFloatWindowManager.removeAllWindow(getApplicationContext());
        mFloatWindowManager = null;
        stopStep();
        timer.cancel();
        timer = null;
        //handler = null;
        editordata.putBoolean(WINDOW_IS_SHOW, mWindowShow);
        editordata.commit();
        unregisterReceiver(mBroadcastReceiver);

        /*Intent intent=new Intent();
        intent.setClass(mContext, FloatService.class);
        startService(intent);*/

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /*class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (isHome() && !mFloatWindowManager.isWindowShowing()&& mWindowShow) {

            if (!mFloatWindowManager.isWindowShowing()&& window_is_show) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!mShow){
                            mFloatWindowManager.createSmallWindow(mContext);
                            mShow=true;
                        }
                    }
                });
            }
            else if ((!isHome() && mFloatWindowManager.isWindowShowing()) || !mWindowShow){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFloatWindowManager.removeAllWindow(getApplicationContext());
                        mShow=false;
            else if (mFloatWindowManager.isWindowShowing() || !window_is_show){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    	if(){
                    		
//                        mFloatWindowManager.removeAllWindow(getApplicationContext());
                    		isshow=false;
                    	}
                    }
                });
            }
            else if (mFloatWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                    }
                });
            }
        }
    }*/

    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);

        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_WINDOW_IS_SHOW)){
                mWindowShow = intent.getBooleanExtra(IS_SHOW, false);
                if (mWindowShow) {
                    mFloatWindowManager.createSmallWindow(getApplicationContext());
                } else {
                    mFloatWindowManager.removeAllWindow(getApplicationContext());
                }
                editordata.putBoolean(WINDOW_IS_SHOW, mWindowShow);
                editordata.commit();
            }
        }

    };

}
