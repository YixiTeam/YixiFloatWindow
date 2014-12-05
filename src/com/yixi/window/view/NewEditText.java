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
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yixi.window.data.NotePad;
import com.yixi.window.data.NoteDataOperation;
import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;

/**
 * Defines a custom EditText View that draws lines between each line of text
 * that is displayed.
 */
public class NewEditText extends EditText implements ActionCallBack{

    private Rect mRect;
    private Paint mPaint;
    private static final float MULT_HEIGHT = 1.4f;

    private NoteDataOperation mNoteDO;
    private ViewGroup mRoot;
    

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
        mNoteDO = new NoteDataOperation(context);
        
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
}
