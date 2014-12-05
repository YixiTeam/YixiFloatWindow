package com.yixi.window.utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.yixi.window.R;
import com.yixi.window.data.MusicData;

public class MusicUtils {

    private static final Uri mAlbumArtUri = Uri
            .parse("content://media/external/audio/albumart");
    public static List<MusicData> getMusicFileList(Context context) {
        List<MusicData> list = new ArrayList<MusicData>();

        String[] projection = new String[] { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int colNameIndex = cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE);
            int colTimeIndex = cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION);
            int colPathIndex = cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA);
            int colArtistIndex = cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int colMusicIdIndex = cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID);
            int colArtistIdIndex = cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int fileNum = cursor.getCount();
            for (int counter = 0; counter < fileNum; counter++) {

                MusicData data = new MusicData();
                data.mMusicName = cursor.getString(colNameIndex);
                data.mMusicTime = cursor.getInt(colTimeIndex);
                data.mMusicPath = cursor.getString(colPathIndex);
                data.mMusicAritst = cursor.getString(colArtistIndex);
                data.mMusicId = cursor.getInt(colMusicIdIndex);
                data.mAritstId = cursor.getInt(colArtistIdIndex);
                list.add(data);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return list;
    }


    public static Drawable getArtworkFromFile(Context context, long songid,
            long albumid) {
        Bitmap bm = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException(
                    "Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(mAlbumArtUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }

            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (bm == null) {
            return context.getResources().getDrawable(R.drawable.ablum_deflaut);
        } else {
            return new BitmapDrawable(bm);
        }
    }

}
