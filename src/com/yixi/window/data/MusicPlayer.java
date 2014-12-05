package com.yixi.window.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class MusicPlayer implements OnCompletionListener, OnErrorListener {

    public final static String TAG = "MusicPlayer";

    public final static String BROCAST_NAME = "com.genius.musicplay.brocast";

    private MediaPlayer mMediaPlayer;

    private List<MusicData> mMusicFileList;

    private int mCurPlayIndex;

    private int mPlayState;

    private Random mRandom;

    private Context mContext;

    private void defaultParam() {
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setOnCompletionListener(this);

        mMediaPlayer.setOnErrorListener(this);

        mMusicFileList = new ArrayList<MusicData>();

        mCurPlayIndex = -1;

        mPlayState = MusicPlayState.MPS_NOFILE;

    }

    public MusicPlayer(Context context) {
        mContext = context;
        defaultParam();

        mRandom = new Random();
        mRandom.setSeed(System.currentTimeMillis());
    }

    public void exit() {
        mMediaPlayer.reset();
        mMusicFileList.clear();
        mCurPlayIndex = -1;
        mPlayState = MusicPlayState.MPS_NOFILE;
    }

    public void refreshMusicList(List<MusicData> FileList) {

        if (FileList == null) {
            mMusicFileList.clear();
            mPlayState = MusicPlayState.MPS_NOFILE;
            mCurPlayIndex = -1;
            return;
        }

        mMusicFileList = FileList;

        if (mMusicFileList.size() == 0) {
            mPlayState = MusicPlayState.MPS_NOFILE;
            mCurPlayIndex = -1;
            return;
        }

        switch (mPlayState) {
        case MusicPlayState.MPS_NOFILE:
            prepare(0);
            break;
        case MusicPlayState.MPS_INVALID:
            prepare(0);
            break;
        case MusicPlayState.MPS_PREPARE:
            prepare(0);
            break;
        case MusicPlayState.MPS_PLAYING:
            break;
        case MusicPlayState.MPS_PAUSE:
            break;
        default:
            break;
        }
    }

    public List<MusicData> getFileList() {
        Log.i(TAG, "getFileList	mMusicFileList.size = " + mMusicFileList.size());
        return mMusicFileList;
    }

    public int getPlayState() {
        return mPlayState;
    }

    public boolean replay() {

        if (mPlayState == MusicPlayState.MPS_NOFILE
                || mPlayState == MusicPlayState.MPS_INVALID) {
            return false;
        }

        mMediaPlayer.start();
        mPlayState = MusicPlayState.MPS_PLAYING;
        sendPlayStateBrocast();

        return true;
    }

    public boolean play(int position) {
        if (mPlayState == MusicPlayState.MPS_NOFILE) {
            return false;
        }

        if (mCurPlayIndex == position) {
            if (mMediaPlayer.isPlaying() == false) {
                mMediaPlayer.start();
                mPlayState = MusicPlayState.MPS_PLAYING;
                sendPlayStateBrocast();
            }

            return true;
        }

        mCurPlayIndex = position;
        if (prepare(mCurPlayIndex) == false) {
            return false;
        }

        return replay();

    }

    public boolean pause() {
        if (mPlayState != MusicPlayState.MPS_PLAYING) {
            return false;
        }

        mMediaPlayer.pause();
        mPlayState = MusicPlayState.MPS_PAUSE;
        sendPlayStateBrocast();

        return true;
    }

    public boolean stop() {
        if (mPlayState != MusicPlayState.MPS_PLAYING
                && mPlayState != MusicPlayState.MPS_PAUSE) {
            return false;
        }

        return prepare(mCurPlayIndex);

    }

    public boolean playNext() {
        if (mPlayState == MusicPlayState.MPS_NOFILE) {
            return false;
        }

        mCurPlayIndex++;
        mCurPlayIndex = reviceIndex(mCurPlayIndex);

        if (prepare(mCurPlayIndex) == false) {
            return false;
        }

        return replay();
    }

    public boolean playPre() {
        if (mPlayState == MusicPlayState.MPS_NOFILE) {
            return false;
        }

        mCurPlayIndex--;
        mCurPlayIndex = reviceIndex(mCurPlayIndex);

        if (prepare(mCurPlayIndex) == false) {
            return false;
        }

        return replay();
    }

    public boolean seekTo(int rate) {
        if (mPlayState == MusicPlayState.MPS_NOFILE
                || mPlayState == MusicPlayState.MPS_INVALID) {
            return false;
        }

        int r = reviceSeekValue(rate);
        int time = mMediaPlayer.getDuration();
        int curTime = (int) ((float) r / 100 * time);

        mMediaPlayer.seekTo(curTime);
        return true;
    }

    public int getCurPosition() {
        if (mPlayState == MusicPlayState.MPS_PLAYING
                || mPlayState == MusicPlayState.MPS_PAUSE) {
            return mMediaPlayer.getCurrentPosition();
        }

        return 0;
    }

    public int getDuration() {

        if (mPlayState == MusicPlayState.MPS_NOFILE
                || mPlayState == MusicPlayState.MPS_INVALID) {
            return 0;
        }

        return mMediaPlayer.getDuration();

    }

    private int reviceIndex(int index) {
        if (index < 0) {
            index = mMusicFileList.size() - 1;
        }

        if (index >= mMusicFileList.size()) {
            index = 0;
        }

        return index;
    }

    private int reviceSeekValue(int value) {
        if (value < 0) {
            value = 0;
        }

        if (value > 100) {
            value = 100;
        }

        return value;
    }

    private int getRandomIndex() {
        int size = mMusicFileList.size();
        if (size == 0) {
            return -1;
        }
        return Math.abs(mRandom.nextInt() % size);
    }

    private boolean prepare(int index) {
        Log.i(TAG, "prepare index = " + index);
        mCurPlayIndex = index;
        mMediaPlayer.reset();

        String path = mMusicFileList.get(index).mMusicPath;
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mPlayState = MusicPlayState.MPS_PREPARE;
            Log.i("apple", "mMediaPlayer.prepare	path = " + path);
            sendPlayStateBrocast();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            mPlayState = MusicPlayState.MPS_INVALID;
            sendPlayStateBrocast();
            return false;
        }

        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();

    }

    public void sendPlayStateBrocast() {

        if (mContext != null) {
            Intent intent = new Intent(BROCAST_NAME);
            intent.putExtra(MusicPlayState.PLAY_STATE_NAME, mPlayState);
            intent.putExtra(MusicPlayState.PLAY_MUSIC_INDEX, mCurPlayIndex);

            if (mPlayState != MusicPlayState.MPS_NOFILE) {
                Bundle bundle = new Bundle();
                MusicData data = mMusicFileList.get(mCurPlayIndex);

                bundle.putParcelable(MusicData.KEY_MUSIC_DATA, data);
                intent.putExtra(MusicData.KEY_MUSIC_DATA, bundle);
            }

            mContext.sendBroadcast(intent);
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub

        Log.e(TAG, "MusicPlayer		onError!!!\n");

        return false;
    }
}
