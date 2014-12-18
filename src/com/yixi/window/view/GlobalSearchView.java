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

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.content.ComponentName;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.util.Log;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.text.Editable;
import android.graphics.drawable.Drawable;


import com.yixi.window.data.AppInfo;
import com.yixi.window.data.AppInfoAdapter;
import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;
import com.yixi.window.FloatWindowManager;
import com.yixi.window.R;

/**
 * Defines a custom EditText View that draws lines between each line of text
 * that is displayed.
 */
public class GlobalSearchView extends LinearLayout implements ActionCallBack, 
View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener{

    private EditText mSearchEditor;
    private ListView mListView;
    private List<AppInfo> mlistAppInfo = null;  
    private AppInfoAdapter mAppInfoAdapter;
    private Context mContext;
    private FloatWindowManager mFloatWindowManager = null;
    
    // This constructor is used by LayoutInflater
    public GlobalSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mFloatWindowManager = FloatWindowManager.getInstance(mContext);
    }
    
    @Override
    protected void onFinishInflate() {
    	Log.e("anne", "onFinishInflate ....");
    	mSearchEditor = (EditText) findViewById(R.id.global_search_field);
    	mSearchEditor.addTextChangedListener(this);
    	
    	mListView = (ListView) findViewById(R.id.global_list);
    	mListView.setOnItemClickListener(this);
    	mlistAppInfo = new ArrayList<AppInfo>();
    	mAppInfoAdapter = new AppInfoAdapter(mContext, mlistAppInfo);
    	mListView.setAdapter(mAppInfoAdapter);
     }
    
    @Override
    public void doAction(){
    	Log.e("anne", "close contacts search view ....");
    }
    
    @Override
    public void onClick(View v){
    	
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = mlistAppInfo.get(position).getIntent(); 
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        Log.e("anne", "mFloatWindowManager = " + mFloatWindowManager);
        if(mFloatWindowManager != null){
        	mFloatWindowManager.removeBigWindow2(mContext);
        	mFloatWindowManager.removeBigWindow(mContext);
        	mFloatWindowManager.createSmallWindow(mContext);
        }
	}
    
    @Override
    public void afterTextChanged(Editable s) {
    	if(!TextUtils.isEmpty(s)){
    		queryAppInfo(s);
    	}
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    
    public void queryAppInfo(Editable s){
    	
    	Log.e("anne","getInstallApp....");
    	PackageManager pm = mContext.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm  
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);  
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();  
            for (ResolveInfo reInfo : resolveInfos) {
            	String label = (String) reInfo.loadLabel(pm);
            	Log.e("anne", "app label = " + label);
        		if(label.equals(s.toString())){
        			String appLabel = label;
        			String activityName = reInfo.activityInfo.name;
        			String pkgName = reInfo.activityInfo.packageName;
        			Drawable icon = reInfo.loadIcon(pm);
        			
        			Intent launchIntent = new Intent();  
        			launchIntent.setComponent(new ComponentName(pkgName,  
        					activityName));  
        			
        			AppInfo appInfo = new AppInfo();  
        			appInfo.setLabel(appLabel);  
        			appInfo.setPkgName(pkgName);  
        			appInfo.setIcon(icon); 
        			appInfo.setIntent(launchIntent);
        			mlistAppInfo.add(appInfo);
        		} 
            }
            Log.e("anne", "mlistAppInfo size = " + mlistAppInfo.size());
        }
        mAppInfoAdapter.notifyDataSetChanged();
    }
}
