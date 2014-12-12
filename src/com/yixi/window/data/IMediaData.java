package com.yixi.window.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

public class IMediaData implements Parcelable {

    public final static String KEY_MEDIA_DATA = "MediaData";

    private final static String KEY_MEDIA_ID = "MediaId";
    private final static String KEY_MEDIA_NAME = "MediaName";
    private final static String KEY_MEDIA_TIME = "MediaTime";
    private final static String KEY_MEDIA_PATH = "MediaPath";
    private final static String KEY_MEDIA_ARITST = "MediaAritst";
    private final static String KEY_ARITST_ID = "AritstId";

    public String mMediaName;
    public int mMediaTime;
    public String mMediaPath;
    public String mMediaAritst;
    public int mMediaId;
    public int mAritstId;

    public IMediaData() {
        mMediaName = "";
        mMediaTime = 0;
        mMediaPath = "";
        mMediaAritst = "";
        mMediaId = -1;
        mAritstId = -1;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        Bundle mBundle = new Bundle();

        mBundle.putString(KEY_MEDIA_NAME, mMediaName);
        mBundle.putInt(KEY_MEDIA_TIME, mMediaTime);
        mBundle.putString(KEY_MEDIA_PATH, mMediaPath);
        mBundle.putString(KEY_MEDIA_ARITST, mMediaAritst);
        mBundle.putInt(KEY_MEDIA_ID, mMediaId);
        mBundle.putInt(KEY_ARITST_ID, mAritstId);
        dest.writeBundle(mBundle);
    }

    public static final Parcelable.Creator<IMediaData> CREATOR = new Parcelable.Creator<IMediaData>() {

        @Override
        public IMediaData createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            IMediaData Data = new IMediaData();

            Bundle mBundle = new Bundle();
            mBundle = source.readBundle();
            Data.mMediaName = mBundle.getString(KEY_MEDIA_NAME);
            Data.mMediaTime = mBundle.getInt(KEY_MEDIA_TIME);
            Data.mMediaPath = mBundle.getString(KEY_MEDIA_PATH);
            Data.mMediaAritst = mBundle.getString(KEY_MEDIA_ARITST);
            Data.mMediaId = mBundle.getInt(KEY_MEDIA_ID);
            Data.mAritstId = mBundle.getInt(KEY_ARITST_ID);

            return Data;
        }

        @Override
        public IMediaData[] newArray(int size) {
            // TODO Auto-generated method stub
            return new IMediaData[size];
        }

    };

}
