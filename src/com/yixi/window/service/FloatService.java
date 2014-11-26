package com.yixi.window.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ab.util.AbDateUtil;
import com.yixi.window.service.IService;
import com.j256.ormlite.dao.Dao;
import com.yixi.window.config.AppData;
import com.yixi.window.floatwindow.FxWindowManager;

public class FloatService extends Service {

    private SensorEventListener mCalorieListener = null;
    private SensorManager mSensorMgr;

    private AppData mAppData = null;
    private Handler handler = new Handler();
    private Timer timer;
    private FxWindowManager mFXWindowManager;
    private Context mContext;
    private boolean window_is_show = true;
    private boolean isshow=false;
    private SharedPreferences sharedata;
    private SharedPreferences.Editor editordata;
    private final IService.Stub mBinder = new IService.Stub() {
        @Override
        public void stopCalorie() throws RemoteException {
            stopStep();
        }

        @Override
        public void startCalorie() throws RemoteException {
            startStep();
        }

        @Override
        public void resetData(float sensitivity, int interval)
                throws RemoteException {
            AppData.sensitivity = sensitivity;
            AppData.interval = interval;
        }

        @Override
        public void saveData() throws RemoteException {
            saveCacheData();
        }

    };

    @Override
    public void onCreate() {
        mSensorMgr = (SensorManager) this
                .getSystemService(android.content.Context.SENSOR_SERVICE);
        mContext=this.getApplicationContext();
        mFXWindowManager = new FxWindowManager(mContext);
        IntentFilter filter=new IntentFilter("com.phicomm.WINDOW_IS_SHOW");
        registerReceiver(mBroadcastReceiver, filter);
        mAppData = new AppData(this);
        editordata=getSharedPreferences("window_data", 0).edit();
        sharedata= getSharedPreferences("window_data", 0);
        if(sharedata==null){
            window_is_show=true;
        }else{
            window_is_show=sharedata.getBoolean("window_is_show", true);
            Intent mIntent = new Intent();
            mIntent.setAction("com.phicomm.WINDOW_NOW_SHOW");
            if(window_is_show){
                mIntent.putExtra("nowshow", "is_show");
                }else{
                    mIntent.putExtra("nowshow", "not_show");
                }
            sendBroadcast(mIntent);
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
        if (timer == null) {
            timer = new Timer();
        }
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mFXWindowManager.removeAllWindow(getApplicationContext());
        //mFXWindowManager = null;
        stopStep();
        timer.cancel();
        timer = null;
        //handler = null;
        editordata.putBoolean("window_is_show", window_is_show);
        editordata.commit();
        //unregisterReceiver(mBroadcastReceiver);

        Intent intent=new Intent();
        intent.setClass(mContext, FloatService.class);
        startService(intent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (isHome() && !mFXWindowManager.isWindowShowing()&& window_is_show) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!isshow){
                            mFXWindowManager.createSmallWindow(mContext);
                            isshow=true;
                        }
                    }
                });
            }
            else if ((!isHome() && mFXWindowManager.isWindowShowing()) || !window_is_show){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFXWindowManager
                                .removeAllWindow(getApplicationContext());
                        isshow=false;
                    }
                });
            }
            else if (isHome() && mFXWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                    }
                });
            }
        }
    }

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
        private static final String ACTION_WINDOW_IS_SHOW = "com.phicomm.WINDOW_IS_SHOW";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_WINDOW_IS_SHOW)){
                Intent mIntent = new Intent();
                intent.setAction("com.phicomm.WINDOW_NOW_SHOW");

                String show = intent.getStringExtra("isShow");
                if(show.equals("no")){
                    window_is_show = false;
                    editordata.putBoolean("window_is_show", window_is_show);
                  //send window_not_show sendBroadcast to notification bar
                    intent.putExtra("nowshow", "not_show");
                }
                else if(show.equals("yes")){
                    window_is_show = true;
                    //send window_show sendBroadcast to notification bar
                    editordata.putBoolean("window_is_show", window_is_show);
                    intent.putExtra("nowshow", "is_show");
                }
                sendBroadcast(intent);
            }
            editordata.commit();
        }

    };

}
