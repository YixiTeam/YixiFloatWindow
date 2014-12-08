package com.yixi.window.view;

import java.lang.reflect.Field;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yixi.window.FloatWindowManager;
import com.yixi.window.R;

public class FloatWindowBigView2 extends LinearLayout {

	private static final String TAG = "FloatWindowBigView2";
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
    private int mFlag;

    RelativeLayout mRelativeLayout;
    RelativeLayout mFrameLayout;
    RelativeLayout mWindowFront;
    RelativeLayout mWindowBack;
    ViewGroup mChangeView;
    
    Button backBut;
    Button exitBut;
    TextView title;
    
    public ActionCallBack mActionCallBack;
    
    public interface ActionCallBack{
    	public void doAction();
    }
    
    int TAG_SWITCH_CONTROL_PAGE_BUTTON = 0;
    int TAG_SWITCH_MARK_PAGE_BUTTON = 1;
    int TAG_SWITCH_CIRCLE_PAGE_STEP = 2;
    int TAG_SWITCH_CIRCLE_PAGE_CALORIE = 3;

    ObjectAnimator visToInvis;
    ObjectAnimator invisToVis;
    private Context mContext;
    private FloatWindowManager mFloatWindowManager;

    public static final int ANIMATION_PERIOD = 200;

    public FloatWindowBigView2(Context context, FloatWindowManager floatWindowManager, int layout_id) {
        super(context);
        mContext = context;
        mFloatWindowManager = floatWindowManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(layout_id, this);
        mFrameLayout = (RelativeLayout) findViewById(R.id.layout_container);
        
        backBut = (Button) findViewById(R.id.back);
        exitBut = (Button) findViewById(R.id.exit);
        title = (TextView) findViewById(R.id.title);
        Log.d(TAG, "------FloatWindowBigView--------layout_id-----" + layout_id);
        Log.d(TAG, "------FloatWindowBigView--------R.layout.floatwindowmedia-----" + R.layout.floatwindowmedia);
        Log.d(TAG, "------FloatWindowBigView--------R.layout.floatwindowidget-----" + R.layout.floatwindowidget);
        Log.d(TAG, "------FloatWindowBigView--------R.layout.floatwindowset-----" + R.layout.floatwindowset);
        switch(layout_id) {
        	case R.layout.floatwindowmusic:
        		mFlag = FloatWindowManager.MEDIA_LAYER;
        		title.setText(R.string.music);
        		break;
        	case R.layout.floatwindowvideo:
        		title.setText(R.string.video);
        		mFlag = FloatWindowManager.MEDIA_LAYER;
        		break;
        	case R.layout.floatwindowcal:
        		title.setText(R.string.cal);
        		mFlag = FloatWindowManager.WIDGET_LAYER;
        		break;
        	case R.layout.floatwindowcon:
        		title.setText(R.string.contacts);
        		mFlag = FloatWindowManager.WIDGET_LAYER;
        		break;
        	case R.layout.floatwindownote:
        		title.setText(R.string.note);
        		mFlag = FloatWindowManager.WIDGET_LAYER;
        		mActionCallBack = (ActionCallBack) findViewById(R.id.note);
        		break;
        	case R.layout.floatwindowsearch:
        		title.setText(R.string.search);
        		mFlag = FloatWindowManager.WIDGET_LAYER;
        		break;
        	default:
        		mFlag = FloatWindowManager.TOP_LAYER;
        }
        
        Log.d(TAG, "------FloatWindowBigView--------mFlag-----" + mFlag);
        backBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "------FloatWindowBigView--------backBut------Click!!-----");
				openLastWindow();
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
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	if (!isTouchInBigWindow(event)) {
                openSmallWindow();
            }
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

            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            mParams.x = (int) (xInScreen - xInView);
            mParams.y = (int) (yInScreen - yInView);
            if (screenWidth / 2 >= (mParams.x + viewWidth)) {
                mParams.x = 0;
            } else {
                mParams.x = screenWidth;
            }
            windowManager.updateViewLayout(this, mParams);
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
    
    private void openLastWindow() {
        mFloatWindowManager.removeSmallWindow(getContext());
        Log.d("ljz", "----FloatWindowBigView-----openLastWindow----mFlag = " + mFlag);
        mFloatWindowManager.createBigWindow(getContext(), mFlag);
        mFloatWindowManager.removeBigWindow2(getContext());
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
    
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        Log.d(TAG, "--------updateViewPosition---mParams.x = " + mParams.x + "----mParams.y = " + mParams.y);
        windowManager.updateViewLayout(this, mParams);
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
            	
            }else if(tag == TAG_SWITCH_CONTROL_PAGE_BUTTON){
            	
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

}
