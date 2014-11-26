package com.yixi.window.floatwindow;

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

public class FxWindowManager {

    private FloatWindowSmallView smallWindow;
    private FloatWindowBigView bigWindow;
    private LayoutParams smallWindowParams;
    private LayoutParams bigWindowParams;
    private WindowManager mWindowManager;
    private Context mContext;
    public Thread mThread;
    private int oldStepFlag = 0;
    private double oldCaolorieFlag = 0.00;
    public Message msg;
    private int TAG_SWITCH_CIRCLE_PAGE_STEP = 2;
    private int mFlag = TAG_SWITCH_CIRCLE_PAGE_STEP;
    private WindowManager windowManager;
    public FxWindowManager(Context context) {
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

    public void createBigWindow(Context context) {
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        Log.d("ljz", "----FxWindowManager---createBigWindow------screenWidth = " + screenWidth + ",-----screenHeight = " + screenHeight);
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context, this);
            Log.d("ljz", "----FxWindowManager---createBigWindow------bigWindowParams = " + bigWindowParams);
            if (bigWindowParams == null) {
            	Log.d("ljz", "----FxWindowManager---createBigWindow------bigWindowParams = " + bigWindowParams);
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
            Log.d("ljz", "----FxWindowManager---createBigWindow------addView = ");
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

    public void createScendWindow(Context context) {
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context, this);
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

    public void setFlag(int falg) {
        this.mFlag = falg;

    }

    public boolean getFlag() {
        return mFlag == TAG_SWITCH_CIRCLE_PAGE_STEP ? true : false;

    }

}
