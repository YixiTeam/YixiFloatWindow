package com.yixi.window.data;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.util.Log;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.yixi.window.data.ContactItemCache;
import com.yixi.window.R;

public class ContactsItemAdapter extends CursorAdapter{ 
	public Context mContext;
    protected LayoutInflater mInflater;
    public boolean mIsContentChanged = false;
    
    public static final int CONTACT_ID_INDEX = 0;
    public static final int DISPLAY_NAME_PRIMARY_INDEX = 1;
    public static final int CONTACT_PRESENCE_INDEX = 2;
    public static final int CONTACT_STATUS_INDEX = 3;
    public static final int PHOTO_ID_INDEX = 4;
    public static final int PHOTO_THUMBNAIL_URI_INDEX = 5;
    public static final int LOOKUP_KEY_INDEX = 6;
    public static final int IS_USER_PROFILE_INDEX = 7;
    public static final int SNIPPET_INDEX = 8;
    
    public boolean isSearchMode;

	public ContactsItemAdapter(Context context){
        this(context, null, true);

	}
	
	public ContactsItemAdapter(Context context, Cursor cursor, boolean autoRetry){
		super(context, cursor, autoRetry);
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent){
		View v  = mInflater.inflate(R.layout.pick_contact_item, parent, false);
		ContactItemCache cache = new ContactItemCache();
		v.setTag(cache);
		return v;
	}
	
	@Override
    public void bindView(View view, Context context, Cursor cursor){
		ContactItemCache cache = (ContactItemCache) view.getTag();
            cache.id = cursor.getLong(CONTACT_ID_INDEX);
            cache.lookupKey = cursor.getString(LOOKUP_KEY_INDEX);
            cache.name = cursor.getString(DISPLAY_NAME_PRIMARY_INDEX);
            if(isSearchMode){
            	cache.number = cursor.getString(SNIPPET_INDEX);
            }else{
            	cache.number = null;
            }
            TextView name = (TextView) view.findViewById(R.id.pick_contact_name);
            TextView number = (TextView) view.findViewById(R.id.pick_contact_number);
            CheckBox selectBtn = (CheckBox) view.findViewById(R.id.pick_contact_check);
            name.setText(cache.name == null ? "unKnow" : cache.name);
            if(TextUtils.isEmpty(cache.number)){
            	number.setVisibility(View.INVISIBLE);
            }else{
            	number.setVisibility(View.VISIBLE);
            	number.setText(cache.number);
            }
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException(
                    "couldn't move cursor to position " + position);
        }
        
        if(convertView != null){
        	v = convertView;
        }else{
        	v = newView(mContext, mCursor, parent);
        }
        
        bindView(v, mContext, mCursor);
		return v;
	}

	public void setSearchMode(boolean isSearch){
		isSearchMode = isSearch;
	}
	
    @Override
    protected void onContentChanged() {
//        updateContent();
    }
    
    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }
    
    
    @Override
    public int getCount(){
    	if(mCursor !=null){
    		return mCursor.getCount();
    	}
    	return 0;
    }
	
}
