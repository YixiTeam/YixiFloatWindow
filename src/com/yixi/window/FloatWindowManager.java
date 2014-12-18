package com.yixi.window;

import com.yixi.window.view.FloatWindowBigView;
import com.yixi.window.view.FloatWindowBigView2;
import com.yixi.window.view.FloatWindowSmallView;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class FloatWindowManager {

	public static final int TOP_LAYER = 0;
	public static final int MEDIA_LAYER = 1;
	public static final int WIDGET_LAYER = 2;
	public static final int SET_LAYER = 3;
	
    private FloatWindowSmallView smallWindow;
    private FloatWindowBigView bigWindow;
    private FloatWindowBigView2 bigWindow2;
    private LayoutParams smallWindowParams;
    private LayoutParams bigWindowParams;
    private WindowManager mWindowManager;
    private Context mContext;
    public Thread mThread;
    public Message msg;
    private WindowManager windowManager;
    public FloatWindowManager(Context context) {
        mContext = context;
        windowManager = getWindowManager(context);
    }
    public void createSmallWindow(Context context) {
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(context, this);
            if (smallWindowParams == null) {
                smallWindowParams = new LayoutParams();
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
                smallWindowParams.type = LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.viewWidth;
                smallWindowParams.height = FloatWindowSmallView.viewHeight;
                smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
    }
    public void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }

    }

    public void createBigWindow(Context context, int flag) {
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        Log.d("ljz", "----FloatWindowManager---createBigWindow------screenWidth = " + screenWidth + ",-----screenHeight = " + screenHeight);
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context, this, flag);
            Log.d("ljz", "----FloatWindowManager---createBigWindow------bigWindowParams = " + bigWindowParams);
            if (bigWindowParams == null) {
            	Log.d("ljz", "----FloatWindowManager---createBigWindow------bigWindowParams = " + bigWindowParams);
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = screenWidth / 2
                        - FloatWindowBigView.viewWidth / 2;
                bigWindowParams.y = screenHeight / 2
                        - FloatWindowBigView.viewHeight / 2;
                bigWindowParams.type = LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
            Log.d("ljz", "----FloatWindowManager---createBigWindow------addView ----bigWindowParams.x = " + bigWindowParams.x + "----bigWindowParams.y = " + bigWindowParams.y);
            bigWindow.setParams(bigWindowParams);
            windowManager.addView(bigWindow, bigWindowParams);
        }
        ScaleToNormal(bigWindow);

//        StepView stepCount = (StepView) bigWindow
//                .findViewById(R.id.stepview);
        //CalorieView calorie=(CalorieView) bigWindow.findViewById(R.id.calorieview);
//        if(stepCount.isShown()){
//            stepCount.update(mData.getNewStepValue());
//        }

    }
    
    public void createBigWindow2(Context context, int id) {
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        Log.d("ljz", "----FloatWindowManager---createBigWindow2------screenWidth = " + screenWidth + ",-----screenHeight = " + screenHeight);
        if (bigWindow2 == null) {
            bigWindow2 = new FloatWindowBigView2(context, this, id);
            Log.d("ljz", "----FloatWindowManager---createBigWindow2------bigWindowParams = " + bigWindowParams);
            if (bigWindowParams == null) {
            	Log.d("ljz", "----FloatWindowManager---createBigWindow2------bigWindowParams = " + bigWindowParams);
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = screenWidth / 2
                        - FloatWindowBigView.viewWidth / 2;
                bigWindowParams.y = screenHeight / 2
                        - FloatWindowBigView.viewHeight / 2;
                bigWindowParams.type = LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
            Log.d("ljz", "----FloatWindowManager---createBigWindow2------addView = ");
            bigWindow2.setParams(bigWindowParams);
            windowManager.addView(bigWindow2, bigWindowParams);
        }
        ScaleToNormal(bigWindow2);

//        StepView stepCount = (StepView) bigWindow
//                .findViewById(R.id.stepview);
        //CalorieView calorie=(CalorieView) bigWindow.findViewById(R.id.calorieview);
//        if(stepCount.isShown()){
//            stepCount.update(mData.getNewStepValue());
//        }

    }

    public void createScendWindow(Context context) {
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context, this, 0);
            if (bigWindowParams == null) {
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = screenWidth / 2
                        - FloatWindowBigView.viewWidth / 2;
                bigWindowParams.y = screenHeight / 2
                        - FloatWindowBigView.viewHeight / 2;
                bigWindowParams.type = LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
        }
        windowManager.addView(bigWindow, bigWindowParams);
        ScaleToNormal(bigWindow);

    }

    public void removeBigWindow(Context context) {
        if (bigWindow != null) {
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }
    
    public void removeBigWindow2(Context context) {
    	Log.e("anne", "removeBigWindow2 ...bigWindow2 = " + bigWindow2);
        if (bigWindow2 != null) {
        	if (bigWindow2.mActionCallBack != null) {
        		bigWindow2.mActionCallBack.doAction();
        	}
            windowManager.removeView(bigWindow2);
            bigWindow2 = null;
        }
    }
    
    public void removeAllWindow(Context context){
        if (bigWindow != null) {
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }else if(smallWindow != null) {
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }


    public boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
    public FloatWindowSmallView getSmallWindow() {
        return smallWindow;
    }

    public void setSmallWindow(FloatWindowSmallView smallWindow) {
        this.smallWindow = smallWindow;
    }

    public void ScaleToNormal(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1, 0.1f, 1,
                Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF,
                0.1f);
        scaleAnimation.setDuration(1000);
        animationSet.addAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
            }
        });
        view.startAnimation(scaleAnimation);
    }

}
