package com.yixi.window.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class NoteDataOperation {
	public Context mContext;
	public NoteDataOperation(Context context){
		mContext = context;
	}

	public  boolean loadNoteList(Uri uri){
		return false;
	}
	
	public  Uri saveNote(Uri uri, ContentValues values){
	
		return mContext.getContentResolver().insert(uri, values);
	}
	
	public  boolean clearNote(){
		return false;
	}
}
