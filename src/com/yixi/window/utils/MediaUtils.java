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
import com.yixi.window.data.IMediaData;

public class MediaUtils {

    private static final Uri mAlbumArtUri = Uri
            .parse("content://media/external/audio/albumart");
    public static List<IMediaData> getMusicFileList(Context context) {
        List<IMediaData> list = new ArrayList<IMediaData>();

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

                IMediaData data = new IMediaData();
                data.mMediaName = cursor.getString(colNameIndex);
                data.mMediaTime = cursor.getInt(colTimeIndex);
                data.mMediaPath = cursor.getString(colPathIndex);
                data.mMediaAritst = cursor.getString(colArtistIndex);
                data.mMediaId = cursor.getInt(colMusicIdIndex);
                data.mAritstId = cursor.getInt(colArtistIdIndex);
                list.add(data);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return list;
    }

    public static List<IMediaData> getVideoFileList(Context context) {
        List<IMediaData> list = new ArrayList<IMediaData>();

        String[] projection = new String[] { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.ARTIST};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int colNameIndex = cursor
                    .getColumnIndex(MediaStore.Video.Media.TITLE);
            int colTimeIndex = cursor
                    .getColumnIndex(MediaStore.Video.Media.DURATION);
            int colPathIndex = cursor
                    .getColumnIndex(MediaStore.Video.Media.DATA);
            int colArtistIndex = cursor
                    .getColumnIndex(MediaStore.Video.Media.ARTIST);
            int colMusicIdIndex = cursor
                    .getColumnIndex(MediaStore.Video.Media._ID);
            int fileNum = cursor.getCount();
            for (int counter = 0; counter < fileNum; counter++) {

                IMediaData data = new IMediaData();
                data.mMediaName = cursor.getString(colNameIndex);
                data.mMediaTime = cursor.getInt(colTimeIndex);
                data.mMediaPath = cursor.getString(colPathIndex);
                data.mMediaAritst = cursor.getString(colArtistIndex);
                data.mMediaId = cursor.getInt(colMusicIdIndex);
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
