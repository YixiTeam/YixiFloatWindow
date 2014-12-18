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


import android.os.Bundle;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.AsyncQueryHandler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.Button;
import android.util.AttributeSet;
import android.util.Log;
import android.database.Cursor;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.SearchSnippetColumns;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;

import java.util.Iterator;
import java.util.Set;

import android.app.Dialog;
import android.app.AlertDialog;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnCancelListener;
import android.app.ProgressDialog;

import com.yixi.window.data.ContactItemCache;
import com.yixi.window.data.ContactsItemAdapter;
import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;
import com.yixi.window.R;

/**
 * Defines a custom EditText View that draws lines between each line of text
 * that is displayed.
 */
public class ContactsSearchView extends LinearLayout implements ActionCallBack,
		View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {

	private final static String TAG = "ContactsSearchView";
	private Context mContext;
	private ListView mListView;
	private Button mCopyBtn;
	private Button mCancelBtn;
	private CheckBox mSelectAll;
	private EditText mSearchEditor;
	
	private ProgressDialog mProgressDialog;
	private QueryHandler mQueryHandler;
	

	public static final char SNIPPET_START_MATCH = '\u0001';
	public static final char SNIPPET_END_MATCH = '\u0001';
	public static final String SNIPPET_ELLIPSIS = "\u2026";
	public static final int SNIPPET_MAX_TOKENS = 5;
	public String sortOrder = " asc";
	
	private int mMode;
	private ContactsItemAdapter mAdapter;
	private Bundle mChoiceSet;
	
	private static final int QUERY_TOKEN = 42;
	private static final int MODE_MASK_SEARCH = 0x80000000;
	private static final int MODE_DEFAULT_CONTACT = 0;
	private static final int MODE_SEARCH_CONTACT = MODE_DEFAULT_CONTACT | MODE_MASK_SEARCH;
	
	public static final String SNIPPET_ARGS = SNIPPET_START_MATCH + ","
			+ SNIPPET_END_MATCH + "," + SNIPPET_ELLIPSIS + ","
			+ SNIPPET_MAX_TOKENS;

	private static final String[] CONTACT_PROJECTION_PRIMARY = new String[] {
			Contacts._ID, // 0
			Contacts.DISPLAY_NAME_PRIMARY, // 1
			Contacts.CONTACT_PRESENCE, // 2
			Contacts.CONTACT_STATUS, // 3
			Contacts.PHOTO_ID, // 4
			Contacts.PHOTO_THUMBNAIL_URI, // 5
			Contacts.LOOKUP_KEY, // 6
			Contacts.IS_USER_PROFILE, // 7
	};
	
	private static final String[] FILTER_PROJECTION_PRIMARY = new String[] {
		Contacts._ID, // 0
		Contacts.DISPLAY_NAME_PRIMARY, // 1
		Contacts.CONTACT_PRESENCE, // 2
		Contacts.CONTACT_STATUS, // 3
		Contacts.PHOTO_ID, // 4
		Contacts.PHOTO_THUMBNAIL_URI, // 5
		Contacts.LOOKUP_KEY, // 6
		Contacts.IS_USER_PROFILE, // 7
		SearchSnippetColumns.SNIPPET, // 8
};

	// This constructor is used by LayoutInflater
	public ContactsSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		mCopyBtn = (Button) findViewById(R.id.btn_copy);
		mCopyBtn.setOnClickListener(this);
		mCancelBtn = (Button) findViewById(R.id.btn_cancel);
		mCancelBtn.setOnClickListener(this);
		mSelectAll = (CheckBox) findViewById(R.id.select_all_check);
		mSelectAll.setOnClickListener(this);
		
		mSearchEditor = (EditText) findViewById(R.id.search_field);
		mSearchEditor.setOnClickListener(this);
		mSearchEditor.addTextChangedListener(this);
		
		
		mListView = (ListView) findViewById(R.id.list);
		mListView.setOnItemClickListener(this);
		mAdapter = new ContactsItemAdapter(mContext, null, true);
		mAdapter.setSearchMode(false);
		mListView.setAdapter(mAdapter);
		mQueryHandler = new QueryHandler();
		
		mChoiceSet = new Bundle();
		mMode = MODE_DEFAULT_CONTACT;
		initData();
	}

	public void initData() {
		Uri baseUri = Contacts.CONTENT_URI;
		Builder builder = baseUri.buildUpon();
		mQueryHandler.startQuery(QUERY_TOKEN, null, builder.build(), CONTACT_PROJECTION_PRIMARY, null,
                null, CONTACT_PROJECTION_PRIMARY[1]+sortOrder);
//		queryData(builder.build(),CONTACT_PROJECTION_PRIMARY, null, null, CONTACT_PROJECTION_PRIMARY[1]+sortOrder);
	}
	
	public void doFilter(Editable filter){
		Uri filterUri = Contacts.CONTENT_FILTER_URI;
		Builder builder = filterUri.buildUpon();
		builder.appendEncodedPath(Uri.encode(filter.toString()));
		mQueryHandler.startQuery(QUERY_TOKEN, null, builder.build(), FILTER_PROJECTION_PRIMARY, null,
                null, FILTER_PROJECTION_PRIMARY[1] + sortOrder);
	}
	
	public void queryData(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy){
		Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);
		if(cursor != null){
			Log.e("anne", "queryData cursor count = " + cursor.getCount());
		}
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void doAction() {
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.btn_copy:
			if(mChoiceSet != null && mChoiceSet.size() > 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext.getApplicationContext())
				.setTitle(mContext.getResources().getString(R.string.search_copy_message))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(mContext.getResources().getString(R.string.copy_contacts_details))
	            .setNegativeButton(android.R.string.cancel, null)
	            .setPositiveButton(android.R.string.ok,
	                    new CopyClickListener());
				AlertDialog dialog = builder.create();
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				dialog.show();
			}
			break;
		case R.id.btn_cancel:
			doAction();
			break;
		case R.id.select_all_check:
			CheckBox select = (CheckBox)v;
			boolean isSelect = select.isChecked();
			selectAll(isSelect);
			break;
		}

	}
	
    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            if (!isSearchMode()) {
                enterSearchMode();
            }
        } else if (isSearchMode()) {
            exitSearchMode();
        }
        doFilter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    
    public void enterSearchMode(){
    	mMode |= MODE_MASK_SEARCH;
    	if(mAdapter != null){
    		mAdapter.setSearchMode(true);
    	}
    }
    
    //save the choiced contactItem
    public void exitSearchMode(){
    	mMode &= ~MODE_MASK_SEARCH;
    	if(mAdapter != null){
    		mAdapter.setSearchMode(false);
    	}
    }
    
    private boolean isSearchMode() {
        return (mMode & MODE_MASK_SEARCH) == MODE_MASK_SEARCH;
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.pick_contact_check);
		boolean isChecked = !checkBox.isChecked();
		checkBox.setChecked(isChecked);
		if(isChecked){
			String[] value = null;
			ContactItemCache cache = (ContactItemCache)view.getTag();
			value = new String[]{String.valueOf(cache.id),cache.name,cache.number};
			mChoiceSet.putStringArray(String.valueOf(cache.id), value);
		}else{
			mChoiceSet.remove(String.valueOf(id));
		}
		
	}
	
	public void copyText(){
		ClipboardManager copy = (ClipboardManager) mContext
				.getSystemService(Context.CLIPBOARD_SERVICE);
		StringBuilder copyText = new StringBuilder();
		if(mChoiceSet != null){
            Bundle choiceSet = (Bundle) mChoiceSet.clone();
            Set<String> keySet = choiceSet.keySet();
            Iterator<String> it = keySet.iterator();
			while(it.hasNext()){
				String contactId = it.next();
				String[] value = mChoiceSet.getStringArray(contactId);
				StringBuilder builder = new StringBuilder();
				for(String data : value){
					builder.append(data);
					builder.append(", ");
				}
				copyText.append(builder);
				copyText.append("\n");
			}
		}
		Log.e(TAG, "copyText = " + copyText);
		copy.setText(copyText.toString());
	}
	
	public void selectAll(boolean select){
		if(mAdapter != null){
			Cursor cursor = mAdapter.getCursor();
			if(cursor == null){
				Log.e(TAG, "cursor is null.");
				return;
			}
			
			cursor.moveToPosition(-1);
			while(cursor.moveToNext()){
				String[] value = null;
				long id = cursor.getLong(0);
				String number = null;
				String lookupKey = cursor.getString(6);
				String name = cursor.getString(1);
				if(isSearchMode()){
					number = cursor.getString(8);
				}else{
					number = null;
				}
				value = new String[]{name, number};
				if(select){
					mChoiceSet.putStringArray(String.valueOf(id), value);
				}else{
					mChoiceSet.remove(String.valueOf(id));
				}
			}
		}
		int count = mListView.getChildCount();
		 for(int i=0; i < count; i++){
			 View v = mListView.getChildAt(i);
			 CheckBox checkBox = (CheckBox) v.findViewById(R.id.pick_contact_check);
			 checkBox.setChecked(select);
		 }
	}
	
	
	private class CopyClickListener implements DialogInterface.OnClickListener{
		
		public void onClick(DialogInterface dialog, int which){
            
			Thread thread;
			thread = new CopyThread();
			
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_SEARCH:
                        case KeyEvent.KEYCODE_CALL:
                            return true;
                        default:
                            return false;
                    }
                }
            };
			//mContext.getResources().getString(R.string.search_cancel)
            mProgressDialog = new ProgressDialog(mContext.getApplicationContext());
            mProgressDialog.setTitle(mContext.getResources().getString(R.string.search_copy_message));
            mProgressDialog.setMessage(mContext.getResources().getString(R.string.copy_contacts_details));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "Cancel", (DialogInterface.OnClickListener) thread);
            mProgressDialog.setOnCancelListener((OnCancelListener) thread);
            mProgressDialog.setOnKeyListener(keyListener);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(mChoiceSet.size());
            mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

            // set dialog can not be canceled by touching outside area of
            // dialog.
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            thread.start();
		}
	}
	
	private class CopyThread extends Thread 
	implements OnCancelListener, DialogInterface.OnClickListener{
		boolean mCanceled = false;
		ClipboardManager mClipboard = null;
		
		public CopyThread(){
			mClipboard = (ClipboardManager) mContext
					.getSystemService(Context.CLIPBOARD_SERVICE);
		}
		
		@Override
		public void run(){
			
			StringBuilder copyText = new StringBuilder();
			if(mChoiceSet != null){
				Bundle choiceSet = (Bundle) mChoiceSet.clone();
				Set<String> keySet = choiceSet.keySet();
				Iterator<String> it = keySet.iterator();
				while(!mCanceled && it.hasNext()){
					String contactId = it.next();
					String[] value = mChoiceSet.getStringArray(contactId);
					StringBuilder builder = new StringBuilder();
					for(String data : value){
						builder.append(data);
						builder.append(", ");
					}
					copyText.append(builder);
					copyText.append("\n");
					
					mProgressDialog.incrementProgressBy(1);
				}
			}
			Log.e(TAG, "copyText = " + copyText);
			mProgressDialog.dismiss();
			mClipboard.setText(copyText.toString());
		}
		
		public void onCancel(DialogInterface dialog) {
			mCanceled = true;
			Log.d(TAG, "CopyThread onCancel, progress:" + mProgressDialog.getProgress());
			//  Give a toast show to tell user delete termination
		}
		
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_NEGATIVE) {
				mCanceled = true;
				mProgressDialog.dismiss();
			}
		}
	}
	
	private class QueryHandler extends AsyncQueryHandler{
		
		public QueryHandler(){
			super(mContext.getContentResolver());
		}
        
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor){
        	Log.e("anne", "onQueryComplete...");
        	if(cursor != null){
        		Log.e("anne", "cursor = " + cursor.getCount());
        	}
            mAdapter.changeCursor(cursor);
        }
	}
}
