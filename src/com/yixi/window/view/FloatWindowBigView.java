package com.yixi.window.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.ServiceManager;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;

import com.yixi.window.FloatWindowManager;
import com.yixi.window.R;

public class FloatWindowBigView extends LinearLayout {

	private static final String TAG = "FloatWindowBigView";
    public static int viewWidth;
    public static int viewHeight;
    private float xInScreen;
    private float yInScreen;
    private static int statusBarHeight;
    private WindowManager windowManager;

    RelativeLayout mRelativeLayout;
    FrameLayout mFrameLayout1;
    FrameLayout mFrameLayout;
    RelativeLayout mWindowFront;
    RelativeLayout mWindowBack;
    ViewGroup mChangeView;
    ViewGroup playSwitchLayout;
    ImageView mediaImageview;
    ImageView widgetImageview;
    ImageView missImageview;
    ImageView cleanImageview;
    ImageView setImageview;
    
    ImageView shotBtn;
    ImageView lockBtn;
    ImageView upBtn;
    ImageView downBtn;
    
    ImageView calBtn;
    ImageView conBtn;
    ImageView noteBtn;
    ImageView seaBtn;
    
    ImageView musicBtn;
    ImageView videoBtn;
    
    Button backBut;
    Button exitBut;
    TextView title;
    
    private IPowerManager mIPowerManager;
    
    int TAG_SWITCH_CONTROL_PAGE_BUTTON = 0;
    int TAG_SWITCH_MARK_PAGE_BUTTON = 1;
    int TAG_SWITCH_CIRCLE_PAGE_STEP = 2;
    int TAG_SWITCH_CIRCLE_PAGE_CALORIE = 3;

    ObjectAnimator visToInvis;
    ObjectAnimator invisToVis;
    private Context mContext;
    private FloatWindowManager mFloatWindowManager;

    public static final int ANIMATION_PERIOD = 200;

    public FloatWindowBigView(Context context, FloatWindowManager floatWindowManager, int flag) {
        super(context);
        mContext = context;
        this.mIPowerManager = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
        mFloatWindowManager = floatWindowManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.floatwindowbig, this);
        mFrameLayout = (FrameLayout) findViewById(R.id.bigwindowlayout);
        mFrameLayout1 = (FrameLayout) findViewById(R.id.medialayout);
        
        mediaImageview = (ImageView) findViewById(R.id.image1);
        widgetImageview = (ImageView) findViewById(R.id.image2);
        missImageview = (ImageView) findViewById(R.id.image3);
        cleanImageview = (ImageView) findViewById(R.id.image4);
        setImageview = (ImageView) findViewById(R.id.image5);
        
        backBut = (Button) findViewById(R.id.back);
        exitBut = (Button) findViewById(R.id.exit);
        title = (TextView) findViewById(R.id.title);
        
        shotBtn = (ImageView) findViewById(R.id.shotscreen);
        lockBtn = (ImageView) findViewById(R.id.lock);
        upBtn = (ImageView) findViewById(R.id.volume_up);
        downBtn = (ImageView) findViewById(R.id.volume_down);
        
        calBtn = (ImageView) findViewById(R.id.calculator);
        conBtn = (ImageView) findViewById(R.id.contacts);
        noteBtn = (ImageView) findViewById(R.id.note);
        seaBtn = (ImageView) findViewById(R.id.search);
        
        musicBtn = (ImageView) findViewById(R.id.music);
        videoBtn = (ImageView) findViewById(R.id.video);
        
        noteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------noteBtn------Click!!-----");
				openNextWindow(R.layout.floatwindownote);
			}
		});
        
        conBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------conBtn------Click!!-----");
				openNextWindow(R.layout.floatwindowcon);
			}
		});
        
        calBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------calBtn------Click!!-----");
				openNextWindow(R.layout.floatwindowcal);
			}
		});
        
        seaBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------seaBtn------Click!!-----");
				openNextWindow(R.layout.floatwindowsearch);
			}
		});
        
        musicBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------musicBtn------Click!!-----");
				openNextWindow(R.layout.floatwindowmusic);
			}
		});
        
        videoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------videoBtn------Click!!-----");
				openNextWindow(R.layout.floatwindowvideo);
			}
		});
        
		shotBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------shotBtn------Click!!-----");
				shotscreen();
			}
		});

		lockBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------lockBtn------Click!!-----");
				try
		        {
		          mIPowerManager.goToSleep(SystemClock.uptimeMillis(), 0);
		          openSmallWindow();
		          return;
		        }
		        catch (RemoteException localRemoteException)
		        {
		          Log.e("TydFloatTask.FloatTaskActivity", localRemoteException.toString());
		          return;
		        }
			}
		});

		upBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------upBtn------Click!!-----");
				AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
				int music_vol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				Log.d(TAG,	"------FloatWindowBigView--------upBtn-----music_vol = " + music_vol);
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, music_vol+1, AudioManager.FLAG_SHOW_UI);
			}
		});

		downBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------downBtn------Click!!-----");
				AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
				int music_vol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				Log.d(TAG,	"---1111---FloatWindowBigView--------downBtn-----music_vol = " + music_vol);
				
				if (music_vol != 0) {
					Log.d(TAG,	"--2222----FloatWindowBigView--------downBtn-----music_vol = " + music_vol);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, music_vol-1, AudioManager.FLAG_SHOW_UI);
				} else {
					Log.d(TAG,	"---3333---FloatWindowBigView--------downBtn-----music_vol = " + music_vol);
//					mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				}
			}
		});
        
        backBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------backBut------Click!!-----");
				mFrameLayout.setVisibility(View.VISIBLE);
				findViewById(R.id.head_layout).setVisibility(View.GONE);
				findViewById(R.id.media_layout).setVisibility(View.GONE);
				findViewById(R.id.widget_layout).setVisibility(View.GONE);
				findViewById(R.id.seting_layout).setVisibility(View.GONE);
			}
		});
        
        exitBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------exitBut------Click!!-----");
				openSmallWindow();
			}
		});

        mediaImageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------mediaImageview------Click!!-----");
//				mRelativeLayout.setVisibility(View.VISIBLE);
				mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.widget_layout).setVisibility(View.GONE);
				findViewById(R.id.seting_layout).setVisibility(View.GONE);
				title.setText(R.string.media);
			}
		});
		
		widgetImageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------widgetImageview------Click!!-----");
				mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.GONE);
				findViewById(R.id.widget_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.seting_layout).setVisibility(View.GONE);
				title.setText(R.string.widget);
			}
		});
		missImageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------missImageview------Click!!-----");
				openSmallWindow();
			}
		});
		cleanImageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------cleanImageview------Click!!-----");
				clearMemory();
				openSmallWindow();
			}
		});
		setImageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG,	"------FloatWindowBigView--------setImageview------Click!!-----");
				mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.GONE);
				findViewById(R.id.widget_layout).setVisibility(View.GONE);
				findViewById(R.id.seting_layout).setVisibility(View.VISIBLE);
				title.setText(R.string.set);
			}
		});
        
//        mWindowFront = (RelativeLayout) findViewById(R.id.windowfront);
//        mWindowBack = (RelativeLayout) findViewById(R.id.windowback);
		
        viewWidth = mFrameLayout.getLayoutParams().width;
        viewHeight = mFrameLayout.getLayoutParams().height;
        Log.d("ljz", "----FloatWindowBigView---------viewWidth = " + viewWidth + ",--viewHeight = " + viewHeight);
        Log.d(TAG, "-------FloatWindowBigView-------flag------------" + flag);
        switch(flag) {
        	case FloatWindowManager.TOP_LAYER:
        		break;
        	case FloatWindowManager.MEDIA_LAYER:
        		mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.widget_layout).setVisibility(View.GONE);
				findViewById(R.id.seting_layout).setVisibility(View.GONE);
        		break;
        	case FloatWindowManager.WIDGET_LAYER:
        		mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.GONE);
				findViewById(R.id.widget_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.seting_layout).setVisibility(View.GONE);
        		break;
        	case FloatWindowManager.SET_LAYER:
        		mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.GONE);
				findViewById(R.id.widget_layout).setVisibility(View.GONE);
				findViewById(R.id.seting_layout).setVisibility(View.VISIBLE);
        		break;
        	default:
        		break;
        	
        }
    }
    
    private Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    private Matrix mDisplayMatrix;
    private Bitmap mScreenBitmap;
    private WindowManager mWindowManager;
    private void shotscreen() {
    	mDisplayMatrix = new Matrix();
        
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getRealMetrics(mDisplayMetrics);
    	mDisplay.getRealMetrics(mDisplayMetrics);
        float[] dims = {mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
        float degrees = getDegreesForRotation(mDisplay.getRotation());
        boolean requiresRotation = (degrees > 0);
        if (requiresRotation) {
            // Get the dimensions of the device in its native orientation
            mDisplayMatrix.reset();
            mDisplayMatrix.preRotate(-degrees);
            mDisplayMatrix.mapPoints(dims);
            dims[0] = Math.abs(dims[0]);
            dims[1] = Math.abs(dims[1]);
        }
        
        mScreenBitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);
        if (requiresRotation) {
            // Rotate the screenshot to the current orientation
            Bitmap ss = Bitmap.createBitmap(mDisplayMetrics.widthPixels,
                    mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(ss);
            c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
            c.rotate(degrees);
            c.translate(-dims[0] / 2, -dims[1] / 2);
            c.drawBitmap(mScreenBitmap, 0, 0, null);
            c.setBitmap(null);
            mScreenBitmap = ss;
        }

        // If we couldn't take the screenshot, notify the user
        if (mScreenBitmap == null) {
           
            return;
        }

        // Optimizations
        mScreenBitmap.setHasAlpha(false);
        mScreenBitmap.prepareToDraw();
        try {
        	saveMyBitmap("shotscreen", mScreenBitmap);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
    }
    
    /**
     * @return the current display rotation in degrees
     */
    private float getDegreesForRotation(int value) {
        switch (value) {
        case Surface.ROTATION_90:
            return 360f - 90f;
        case Surface.ROTATION_180:
            return 360f - 180f;
        case Surface.ROTATION_270:
            return 360f - 270f;
        }
        return 0f;
    }
    
    private void saveMyBitmap(String bitName, Bitmap bitmap) throws IOException {
        File f = new File("/data/data/com.yixi.window/" + bitName + ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    private void removeRecentTask() {
    	final ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//    	final List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(2, ActivityManager.RECENT_WITH_EXCLUDED);
    	final List<RunningTaskInfo> recentTasks = am.getRunningTasks(10);
    	for (int i = 0; i<recentTasks.size(); i++) {
    		Log.d(TAG, "-------removeRecentTask-----1--i = " + i + "--- " + recentTasks.get(i).toString());
    		am.removeTask(recentTasks.get(i).id, ActivityManager.REMOVE_TASK_KILL_PROCESS);
//    		Log.d(TAG, "-------removeRecentTask----2---i = " + i + "--- " + recentTasks.get(i));
    	}
    	
    }
    
    private void clearMemory() {
    	ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        int count = 0;
        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                RunningAppProcessInfo appProcessInfo = infoList.get(i);
                Log.d(TAG, "process name : " + appProcessInfo.processName);

                Log.d(TAG, "importance : " + appProcessInfo.importance);

                if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; ++j) {
                    	Log.d(TAG, "---------clearMemory-------package name : " + pkgList[j]);
                        if ("com.yixi.window".equals(pkgList[j])) {
                        	Log.d(TAG, "I will do nothing for this app, package name : " + pkgList[j]);
                        } else {
                        	Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
                        	am.killBackgroundProcesses(pkgList[j]);
                            count++;
                        }
                    }
                }

            }
        }

        Toast.makeText(mContext, "I have clear " + count + " process for your phone", Toast.LENGTH_LONG).show();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (!isTouchInBigWindow(event)) {
                openSmallWindow();
            }
            break;
        }
        return true;

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)  {
        super.dispatchKeyEvent(event);
        switch (event.getKeyCode()) {
        case KeyEvent.KEYCODE_BACK:
        case KeyEvent.KEYCODE_MENU:
            openSmallWindow();
            return true;
        }
        return false;
    }

    private void openSmallWindow() {
        mFloatWindowManager.createSmallWindow(getContext());
        mFloatWindowManager.removeBigWindow(getContext());
        mFloatWindowManager.removeBigWindow2(getContext());
    }
    
    private void openNextWindow(int id) {
        mFloatWindowManager.removeBigWindow(getContext());
        mFloatWindowManager.removeBigWindow(getContext());
        mFloatWindowManager.createBigWindow2(getContext(), id);
    }
    
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

	private boolean isTouchInBigWindow(MotionEvent event) {
		boolean flag = true;
		// viewWidth = mWindowFront.getLayoutParams().width;
		// viewHeight = mWindowFront.getLayoutParams().height;
		viewWidth = mFrameLayout.getLayoutParams().width;
		viewHeight = mFrameLayout.getLayoutParams().height;
		Log.d("ljz", "----FloatWindowBigView----isTouchInBigWindow-----viewWidth = " + viewWidth + ",--viewHeight = " + viewHeight);
		xInScreen = event.getRawX();
		yInScreen = event.getRawY() - getStatusBarHeight();
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (xInScreen < (screenWidth / 2 - viewWidth / 2)) {
			flag = false;
		}
		if (xInScreen > (screenWidth / 2 + viewWidth / 2)) {
			flag = false;
		}
		if (yInScreen < (screenHeight / 2 - viewHeight / 2)) {
			flag = false;
		}
		if (yInScreen > (screenHeight / 2 + viewHeight / 2)) {
			flag = false;
		}
		return flag;
	}

    private void applyRotation(ViewGroup view,int tag, float start, float end) {
        // Find the center of the container
        final View layout;

        layout = view;

        final float centerX = layout.getWidth() / 2.0f;
        final float centerY = layout.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
                centerX, centerY, 310.0f, true);
        rotation.setDuration(300);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(tag,view));

        layout.startAnimation(rotation);
    }

    private final class DisplayNextView implements Animation.AnimationListener {

        private final int tag;
        private final ViewGroup view;
        private DisplayNextView(int tag,ViewGroup view) {
            this.tag = tag;
            this.view = view;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
//            playSwitchLayout.post(new SwapViews(tag,view));

            if (tag == TAG_SWITCH_MARK_PAGE_BUTTON){
                updateData();
            }else if(tag == TAG_SWITCH_CONTROL_PAGE_BUTTON){
                updateData();
                }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    public void updateData(){
        // set Data for every view.
//        if(mWindowFront.getVisibility() == View.VISIBLE && mStepView.getVisibility() == View.VISIBLE){
//            mStepView.setStep(mData.getNewStepValue());
//        }
//        if(mWindowFront.getVisibility() == View.VISIBLE && mCalrieView.getVisibility() == View.VISIBLE){
//        }
//        if(mWindowBack.getVisibility() == View.VISIBLE){
//            mDialyStep.setText(mData.getDialyAvaryStep());
//            mDialyCalorie.setText(mData.getDialyAvaryCalorie());
//            mTotalStepsView.setText(mData.getTotalStep());
//            mTotalCaloriesView.setText(mData.getTotalCalorie());
//            startChartAnimation();
//        }
        //end
    }

}
