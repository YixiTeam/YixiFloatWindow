package com.yixi.window.floatwindow;

import java.lang.reflect.Field;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    
    Button backBut;
    Button exitBut;
    
    
    int TAG_SWITCH_CONTROL_PAGE_BUTTON = 0;
    int TAG_SWITCH_MARK_PAGE_BUTTON = 1;
    int TAG_SWITCH_CIRCLE_PAGE_STEP = 2;
    int TAG_SWITCH_CIRCLE_PAGE_CALORIE = 3;

    ObjectAnimator visToInvis;
    ObjectAnimator invisToVis;
    private Context mContext;
    private FxWindowManager mFXWindowManager;

    public static final int ANIMATION_PERIOD = 200;

    public FloatWindowBigView(Context context, FxWindowManager fxWindowManager) {
        super(context);
        mContext = context;
        mFXWindowManager = fxWindowManager;
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
				Log.d(TAG, "------FloatWindowBigView--------imageview1------Click!!-----");
//				mRelativeLayout.setVisibility(View.VISIBLE);
				mFrameLayout.setVisibility(View.GONE);
				findViewById(R.id.head_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.media_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.widget_layout).setVisibility(View.GONE);
				findViewById(R.id.seting_layout).setVisibility(View.GONE);
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
			}
		});
        
//        mWindowFront = (RelativeLayout) findViewById(R.id.windowfront);
//        mWindowBack = (RelativeLayout) findViewById(R.id.windowback);
		
        mFXWindowManager.setFlag(TAG_SWITCH_CIRCLE_PAGE_STEP);
        viewWidth = mFrameLayout.getLayoutParams().width;
        viewHeight = mFrameLayout.getLayoutParams().height;
        Log.d("ljz", "----FloatWindowBigView---------viewWidth = " + viewWidth + ",--viewHeight = " + viewHeight);
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
        mFXWindowManager.createSmallWindow(getContext());
        mFXWindowManager.removeBigWindow(getContext());
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
