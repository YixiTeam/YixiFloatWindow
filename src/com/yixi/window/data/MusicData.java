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

public class MusicData implements Parcelable {

    public final static String KEY_MUSIC_DATA = "MusicData";

    private final static String KEY_MUSIC_ID = "MusicId";
    private final static String KEY_MUSIC_NAME = "MusicName";
    private final static String KEY_MUSIC_TIME = "MusicTime";
    private final static String KEY_MUSIC_PATH = "MusicPath";
    private final static String KEY_MUSIC_ARITST = "MusicAritst";
    private final static String KEY_ARITST_ID = "AritstId";

    public String mMusicName;
    public int mMusicTime;
    public String mMusicPath;
    public String mMusicAritst;
    public int mMusicId;
    public int mAritstId;

    public MusicData() {
        mMusicName = "";
        mMusicTime = 0;
        mMusicPath = "";
        mMusicAritst = "";
        mMusicId = -1;
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

        mBundle.putString(KEY_MUSIC_NAME, mMusicName);
        mBundle.putInt(KEY_MUSIC_TIME, mMusicTime);
        mBundle.putString(KEY_MUSIC_PATH, mMusicPath);
        mBundle.putString(KEY_MUSIC_ARITST, mMusicAritst);
        mBundle.putInt(KEY_MUSIC_ID, mMusicId);
        mBundle.putInt(KEY_ARITST_ID, mAritstId);
        dest.writeBundle(mBundle);
    }

    public static final Parcelable.Creator<MusicData> CREATOR = new Parcelable.Creator<MusicData>() {

        @Override
        public MusicData createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            MusicData Data = new MusicData();

            Bundle mBundle = new Bundle();
            mBundle = source.readBundle();
            Data.mMusicName = mBundle.getString(KEY_MUSIC_NAME);
            Data.mMusicTime = mBundle.getInt(KEY_MUSIC_TIME);
            Data.mMusicPath = mBundle.getString(KEY_MUSIC_PATH);
            Data.mMusicAritst = mBundle.getString(KEY_MUSIC_ARITST);
            Data.mMusicId = mBundle.getInt(KEY_MUSIC_ID);
            Data.mAritstId = mBundle.getInt(KEY_ARITST_ID);

            return Data;
        }

        @Override
        public MusicData[] newArray(int size) {
            // TODO Auto-generated method stub
            return new MusicData[size];
        }

    };

}
