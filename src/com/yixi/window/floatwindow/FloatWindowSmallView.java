package com.yixi.window.floatwindow;

import java.lang.reflect.Field;

import com.yixi.window.R;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FloatWindowSmallView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    private static int statusBarHeight;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private float xInScreen;
    private float yInScreen;
    private float xDownInScreen;
    private float yDownInScreen;
    private float xInView;
    private float yInView;
    private View view;
    private ImageView image;

    private FloatWindowBigView bigWindow;

    FxWindowManager mFXWindowManager;

    public FloatWindowSmallView(Context context, FxWindowManager fxWindowManager) {
        super(context);
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.floatwindowsmall, this);
        view = findViewById(R.id.smallwindowlayout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        image = (ImageView) findViewById(R.id.image);
        // percentView.setText(MyWindowManager.getUsedPercentValue(context));
        mFXWindowManager = fxWindowManager;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            xInView = event.getX();
            yInView = event.getY();

            xDownInScreen = event.getRawX();
            yDownInScreen = event.getRawY() - getStatusBarHeight();

            xInScreen = event.getRawX();
            yInScreen = event.getRawY() - getStatusBarHeight();
            break;
        case MotionEvent.ACTION_MOVE:
            xInScreen = event.getRawX();
            yInScreen = event.getRawY() - getStatusBarHeight();
            updateViewPosition();
            break;
        case MotionEvent.ACTION_UP:

            /*
             * if(MyWindowManager.isReadyToLaunch()){ new
             * launchTask().execute(); }
             */
            if (Math.abs(xDownInScreen - xInScreen) < 10
                    && Math.abs(yDownInScreen - yInScreen) < 10) {
                Alpha();
            }
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            mParams.x = (int) (xInScreen - xInView);
            mParams.y = (int) (yInScreen - yInView);
            if (screenWidth / 2 >= (mParams.x + viewWidth)) {
                mParams.x = 0;
            } else {
                mParams.x = screenWidth;
            }
            windowManager.updateViewLayout(this, mParams);
            // removeLauncherWindow();
            break;

        }
        return true;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
        // MyWindowManager.updateLauncher();
    }

    private void openBigWindow() {
        mFXWindowManager.removeSmallWindow(getContext());
        mFXWindowManager.createBigWindow(getContext());
    }

    class launchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            int height = 10;
            while (mParams.y > 0) {
                mParams.y = mParams.y - height;
                publishProgress();
                try {
                    height = +10;
                    Thread.sleep(6);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // windowManager.updateViewLayout(FloatWindowSmallView.this,
            // mParams);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mParams.x = (int) (xDownInScreen - xInView);
            mParams.y = (int) (yDownInScreen - yInView);
            windowManager.updateViewLayout(FloatWindowSmallView.this, mParams);
            // openSuccessWindow();
        }

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

    public void Alpha() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(400);
        animationSet.addAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                openBigWindow();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }
        });
        view.startAnimation(animationSet);
    }

}
