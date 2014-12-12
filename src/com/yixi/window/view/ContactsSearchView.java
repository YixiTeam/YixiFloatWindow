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
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.view.View;
import android.util.Log;

import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;

/**
 * Defines a custom EditText View that draws lines between each line of text
 * that is displayed.
 */
public class ContactsSearchView extends LinearLayout implements ActionCallBack, View.OnClickListener{

    
    // This constructor is used by LayoutInflater
    public ContactsSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onFinishInflate() {
    	Log.e("anne", "onFinishInflate ....");
    }
    
    @Override
    public void doAction(){
    	Log.e("anne", "close contacts search view ....");
    }
    
    @Override
    public void onClick(View v){
    	
    }
    
}
