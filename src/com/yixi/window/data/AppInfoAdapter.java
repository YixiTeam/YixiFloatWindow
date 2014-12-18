package com.yixi.window.data;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.CheckBox;
import android.util.Log;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.yixi.window.data.AppInfo;
import com.yixi.window.R;

public class AppInfoAdapter extends BaseAdapter{ 
	
	private List<AppInfo> mlistAppInfo = null;  
	
	LayoutInflater mInflater = null;
	
	public AppInfoAdapter(Context context, List<AppInfo> appInfos){
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mlistAppInfo = appInfos;
	}
	
	
	@Override  
	public int getCount() {  
		// TODO Auto-generated method stub  
		if(mlistAppInfo != null){
			return mlistAppInfo.size();  
		}
		return -1;
	} 
	
	@Override  
	public Object getItem(int position) {  
		// TODO Auto-generated method stub  
		return mlistAppInfo.get(position);  
	} 
	
	@Override  
	public long getItemId(int position) {  
		// TODO Auto-generated method stub  
		return 0;  
	}  
	
	@Override  
	public View getView(int position, View convertview, ViewGroup arg2) {  
		View view = null;  
		ViewHolder holder = null;  
		if (convertview == null || convertview.getTag() == null) {  
			view = mInflater.inflate(R.layout.search_app_item, null);  
			holder = new ViewHolder(view);  
			view.setTag(holder);  
		}   
		else{  
			view = convertview ;  
			holder = (ViewHolder) convertview.getTag() ;  
		}  
		AppInfo appInfo = (AppInfo) getItem(position);  
		holder.appIcon.setImageDrawable(appInfo.getIcon());  
		holder.tvAppLabel.setText(appInfo.getLabel());  
		return view;  
	}  
	
	class ViewHolder {  
		ImageView appIcon;  
		TextView tvAppLabel;  
		
		public ViewHolder(View view) {  
			this.appIcon = (ImageView) view.findViewById(R.id.pick_app_icon);  
			this.tvAppLabel = (TextView) view.findViewById(R.id.pick_app_label);  
		}  
	}  
}
