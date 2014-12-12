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

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.text.Editable;
import android.text.Layout;    
import android.text.Selection;    
import android.view.ContextMenu;    
import android.view.Gravity;    
import android.view.MotionEvent;   
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import com.yixi.window.R;
import android.text.TextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.Spannable;
import android.widget.Button;
import android.content.ClipboardManager;

import com.yixi.window.data.NotePad;
import com.yixi.window.data.NoteDataOperation;
import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;

/**
 * Defines a custom EditText View that draws lines between each line of text
 * that is displayed.
 */
public class NewEditText extends EditText implements ActionCallBack, View.OnClickListener{

    private Rect mRect;
    private Paint mPaint;
    private static final float MULT_HEIGHT = 1.4f;

    private NoteDataOperation mNoteDO;
    private ViewGroup mRoot;
    private Context mContext;
    private boolean isImmShow = false;
    private boolean isFirstTouch = true;
    private PopupWindow mPop;
    private Button mSelBtn;
    private Button mSelAllBtn;
    private Button mCopyBtn;
    private Button mPlasterBtn;
    
    private int off;
    

    // This constructor is used by LayoutInflater
    public NewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Creates a Rect and a Paint object, and sets the style and color of
        // the Paint object.
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x800000FF);
        this.setLineSpacing(0, MULT_HEIGHT);
        mContext = context;
        mNoteDO = new NoteDataOperation(context);
    }
    
    @Override
    protected void onFinishInflate() {
    	Log.e("anne","onFinishInflate....");
    	LayoutInflater inflater = LayoutInflater.from(mContext); 
    	View view = inflater.inflate(R.layout.note_popup_menu, null);
    	
    	mSelBtn = (Button)view.findViewById(R.id.select);
    	mSelBtn.setOnClickListener(this);
    	mSelAllBtn = (Button)view.findViewById(R.id.select_all);
    	mSelAllBtn.setOnClickListener(this);
    	mCopyBtn = (Button)view.findViewById(R.id.copy);
    	mCopyBtn.setOnClickListener(this);
    	mPlasterBtn = (Button)view.findViewById(R.id.plaster);
    	mPlasterBtn.setOnClickListener(this);
    	
    	mPop  = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
    	mPop.setOutsideTouchable(true);
    	mPop.setFocusable(true);
    	mPop.setBackgroundDrawable(new BitmapDrawable());
    	mPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
    }

    /**
     * This is called to draw the NewEditText object
     *
     * @param canvas The canvas on which the background is drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        // Gets the number of lines of text in the View.
        int count = getLineCount();

        // Gets the global Rect and Paint objects
        Rect r = mRect;
        Paint paint = mPaint;

        /*
         * Draws one line in the rectangle for every line of text in the
         * EditText
         */
        for (int i = 0; i < count; i++) {

            // Gets the baseline coordinates for the current line of text
            getLineBounds(i, r);
            int baseline = (i + 1) * getLineHeight();

            /*
             * Draws a line in the background from the left of the rectangle to
             * the right, at a vertical position one dip below the baseline,
             * using the "paint" object for details.
             */
            canvas.drawLine(r.left, baseline, r.right, baseline, paint);
        }

        // Finishes up by calling the parent method
        super.onDraw(canvas);
    }
    
    /*
     * save the note to database
     */
    public boolean doSaveAction(){
    	Uri uri = null;
    	String text = getText().toString();
    	int length = text.length();

    	if(length == 0){
    		return false;
    	}else{
    		
            // Sets up a map to contain values to be updated in the provider.
            ContentValues values = new ContentValues();
            values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, System.currentTimeMillis());

            // This puts the desired notes text into the map.
            values.put(NotePad.Notes.COLUMN_NAME_NOTE, text);
            
            try{
            	uri = mNoteDO.saveNote(NotePad.Notes.CONTENT_URI, values);
            	Log.e("anne", "save text = " + text + ", uri = " + uri);
            }catch(Exception e){
            	e.printStackTrace();
            }
            
            if(uri != null){
            	return true;
            }
    	}
    	
    	return false;
    }
    
    @Override
    public void doAction(){
    	Log.e("anne", "doSaveAction from note....");
    	doSaveAction();
    }
    
    
    @Override    
    public boolean onTouchEvent(MotionEvent event) {   
    	if(!isImmShow){
    		InputMethodManager imm = (InputMethodManager) 
    				mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    		imm.showSoftInput(this, InputMethodManager.HIDE_NOT_ALWAYS); 
    	}

        int action = event.getAction();    
        Layout layout = getLayout();    
        int line = 0;    
        switch(action) {    
        case MotionEvent.ACTION_DOWN:    
        	if(isFirstTouch){
        		isFirstTouch = false;
        		break;
        	}
        	
        	String data = getText().toString();
        	if(mPop != null && !TextUtils.isEmpty(data)){
        		Log.e("anne", "mPop show = " + mPop.isShowing() + isFirstTouch);
        		if(!mPop.isShowing()){
        			mPop.showAsDropDown(this);
        		}else{
        			mPop.dismiss();
        		}
        	}
            break;    
        case MotionEvent.ACTION_MOVE:    
        case MotionEvent.ACTION_UP:    
            break;    
        }    
        return true;    
    } 
    
    @Override
    public void onClick(View v){
    	int id = v.getId();
    	String select = null;
    	switch(id){
    	case R.id.select:
    		Log.e("anne","select btn click...");
    		break;
    	case R.id.select_all:
    		Log.e("anne","select_all btn click...");
    		break;
    	case R.id.copy:
    		Log.e("anne","copy btn click...");
    		ClipboardManager copy = (ClipboardManager)mContext
    		.getSystemService(Context.CLIPBOARD_SERVICE);
    		select = getText().toString();
    		copy.setText(select);
    		break;
    	case R.id.plaster:
    		Log.e("anne","plaster btn click...");
    		ClipboardManager plaster = (ClipboardManager)mContext
    		.getSystemService(Context.CLIPBOARD_SERVICE);
    		select = plaster.getText().toString().trim();
    		Log.e("anne", "getText().toString() + data = " + getText().toString() + select);
    		setText(getText().toString() + select);
    		//set the cursor at the end of text
    		CharSequence text = getText();
    		if(text instanceof Spannable){
    			Spannable spanText = (Spannable)text;
    			Selection.setSelection(spanText, text.length());
    		}
    		break;
    	}
    	
    }
    
}
