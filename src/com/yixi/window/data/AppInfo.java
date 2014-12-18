package com.yixi.window.data;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppInfo {
	private String mLabel;
	private Drawable mIcon;
	private Intent mIntent;
	private String mPkgName;
	
	public AppInfo(){
	
	}
	
	public void setLabel(String label){
		this.mLabel = label;
	}
	
	public String getLabel(){
		return mLabel;
	}

	public void setIcon(Drawable icon){
		this.mIcon = icon;
	}
	
	public Drawable getIcon(){
		return mIcon;
	}
	
	public void setIntent(Intent intent){
		this.mIntent = intent;
	}
	
	public Intent getIntent(){
		return mIntent;
	}
	
	public void setPkgName(String pkgName){
		this.mPkgName = pkgName;
	}
	
	public String getPkgName(){
		return mPkgName;
	}
	
}
