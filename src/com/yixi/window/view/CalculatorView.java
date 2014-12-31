/*
 * Copyright (c) 2013 Qualcomm Technologies, Inc.
 * All Rights Reserved.
 * Qualcomm Technologies Confidential and Proprietary.
 *
 * Not a Contribution.
 * Apache license notifications and license are retained
 * for attribution purposes only.
 */
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yixi.window.view;



import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.support.v4.view.ViewPager;

import com.yixi.window.view.CalculatorDisplay;
import com.yixi.window.view.EventListener;
import com.yixi.window.view.History;
import com.yixi.window.view.HistoryAdapter;
import com.yixi.window.view.Logic;
import com.yixi.window.view.Persist;
import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;
import com.yixi.window.R;

/**
 * Defines a custom EditText View that draws lines between each line of text
 * that is displayed.
 */
public class CalculatorView extends LinearLayout implements ActionCallBack, Logic.Listener {

	private final static String TAG = "CalculatorView";
	private Context mContext;
    EventListener mListener = new EventListener();
    private CalculatorDisplay mDisplay;
    private Persist mPersist;
    private History mHistory;
    private Logic mLogic;
    private ViewPager mPager = null;
    private View mClearButton;
    private View mBackspaceButton;
    private View mOverflowMenuButton;

    static final int BASIC_PANEL    = 0;
    static final int ADVANCED_PANEL = 1;
	
	// This constructor is used by LayoutInflater
	public CalculatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
//		init();
	}

	@Override
	protected void onFinishInflate() {
		init();
	}
	
	public void init(){
		// Disable IME for this application
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // Single page UI
        final TypedArray buttons = getResources().obtainTypedArray(R.array.simple_buttons);
        for (int i = 0; i < buttons.length(); i++) {
        	setOnClickListener(null, buttons.getResourceId(i, 0));
        }
        buttons.recycle();

        if (mClearButton == null) {
            mClearButton = findViewById(R.id.clear);
            mClearButton.setOnClickListener(mListener);
            mClearButton.setOnLongClickListener(mListener);
        }
        if (mBackspaceButton == null) {
            mBackspaceButton = findViewById(R.id.del);
            mBackspaceButton.setOnClickListener(mListener);
            mBackspaceButton.setOnLongClickListener(mListener);
        }

        mPersist = new Persist(mContext);
        mPersist.load();

        mHistory = mPersist.history;

        mDisplay = (CalculatorDisplay) findViewById(R.id.display);

        mLogic = new Logic(mContext, mHistory, mDisplay);
        mLogic.setListener(this);

        mLogic.setDeleteMode(mPersist.getDeleteMode());
        mLogic.setLineLength(mDisplay.getMaxDigits());

        HistoryAdapter historyAdapter = new HistoryAdapter(mContext, mHistory, mLogic);
        mHistory.setObserver(historyAdapter);

        mListener.setHandler(mLogic, mPager);
        mDisplay.setOnKeyListener(mListener);

        mLogic.resumeWithHistory();
        updateDeleteMode();
	}
    void setOnClickListener(View root, int id) {
        final View target = root != null ? root.findViewById(id) : findViewById(id);
        target.setOnClickListener(mListener);
    }
    
    @Override
    public void onDeleteModeChange() {
        updateDeleteMode();
    }
	
    private void updateDeleteMode() {
        if (mLogic.getDeleteMode() == Logic.DELETE_MODE_BACKSPACE) {
            mClearButton.setVisibility(View.GONE);
            mBackspaceButton.setVisibility(View.VISIBLE);
        } else {
            mClearButton.setVisibility(View.VISIBLE);
            mBackspaceButton.setVisibility(View.GONE);
        }
    }
    
	@Override
	public void doAction() {
        mLogic.updateHistory();
        mPersist.setDeleteMode(mLogic.getDeleteMode());
        mPersist.save();
	}

}
