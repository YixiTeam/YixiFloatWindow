package com.yixi.window.view;

import java.util.ArrayList;
import java.util.List;

import com.yixi.window.R;
import com.yixi.window.data.IMediaData;
import com.yixi.window.data.MediaPlayState;
import com.yixi.window.data.MusicPlayer;
import com.yixi.window.service.IOnServiceConnectComplete;
import com.yixi.window.service.ServiceManager;
import com.yixi.window.utils.MediaTimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FloatMovieView extends RelativeLayout implements
        SurfaceHolder.Callback, OnSeekBarChangeListener, OnClickListener, IOnServiceConnectComplete{
    private static final int REFRESH_PROGRESS_EVENT = 0x0010;
    private boolean mIsHaveData = false;
    private View mLayoutView;
    private SurfaceView mMediaView;
    private SurfaceHolder mHolder;

    public ImageButton mBtnPlay;
    public ImageButton mBtnPause;
    public ImageButton mBtnPlayNext;
    public ImageButton mBtnPlayPre;
    public TextView mPlaySongTextView;
    public SeekBar mPlayProgress;
    public TextView mcurtimeTextView;
    public TextView mtotaltimeTextView;
    private int mCurrent;
    private MediaPlayStateBrocast mPlayStateBrocast;
    private Context mContext;
    private List<IMediaData> mVedioList;
    private int mCurMediaTotalTime;
    private MediaTimer mMediaTimer;
    private Handler mHandler;
    private ServiceManager mServiceManager;

    public FloatMovieView(Context context) {
        super(context);
    }

    public FloatMovieView(Context context, AttributeSet ats) {
        super(context, ats);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutView = inflater.inflate(R.layout.movie_player, this, true);
        initView();
        init();
    }
    private void init() {
    }

    private void initView() {
        // TODO Auto-generated method stub
        mMediaView = (SurfaceView) mLayoutView.findViewById(R.id.movie_layout);
        mHolder = mMediaView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mBtnPlay = (ImageButton) mLayoutView.findViewById(R.id.buttonPlay);
        mBtnPause = (ImageButton) mLayoutView.findViewById(R.id.buttonPause);
        mBtnPlayPre = (ImageButton) mLayoutView
                .findViewById(R.id.buttonPlayPre);
        mBtnPlayNext = (ImageButton) mLayoutView
                .findViewById(R.id.buttonPlayNext);
        mBtnPlay.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnPlayPre.setOnClickListener(this);
        mBtnPlayNext.setOnClickListener(this);

        mPlaySongTextView = (TextView) mLayoutView
                .findViewById(R.id.movie_title);
        mcurtimeTextView = (TextView) mLayoutView
                .findViewById(R.id.textViewCurTime);
        mtotaltimeTextView = (TextView) mLayoutView
                .findViewById(R.id.textViewTotalTime);

        mPlayProgress = (SeekBar) mLayoutView.findViewById(R.id.seekBar);
        mPlayProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
        
    }


    class MediaPlayStateBrocast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicPlayer.BROCAST_NAME)) {
                transPlayStateEvent(intent);
            }

        }

    }

    public void transPlayStateEvent(Intent intent) {
        IMediaData data = new IMediaData();
        int playState = intent.getIntExtra(MediaPlayState.PLAY_STATE_NAME, -1);
        Bundle bundle = intent.getBundleExtra(IMediaData.KEY_MEDIA_DATA);
        if (bundle != null) {
            data = bundle.getParcelable(IMediaData.KEY_MEDIA_DATA);
        }
        int playIndex = intent.getIntExtra(MediaPlayState.PLAY_MEDIA_INDEX, -1);
        switch (playState) {
        case MediaPlayState.MPS_INVALID:
            mMediaTimer.stopTimer();
            showPlay(true);
            break;
        case MediaPlayState.MPS_PREPARE:
            mMediaTimer.stopTimer();
            mCurMediaTotalTime = data.mMediaTime;
            if (mCurMediaTotalTime == 0) {
            }
            showPlay(true);
            break;
        case MediaPlayState.MPS_PLAYING:
            mMediaTimer.startTimer();
            if (mCurMediaTotalTime == 0) {
            }
            showPlay(false);
            break;
        case MediaPlayState.MPS_PAUSE:
            mMediaTimer.stopTimer();
            if (mCurMediaTotalTime == 0) {
            }
            showPlay(true);
            break;
        default:
            break;
        }
    }

    @Override
    public void onClick(View view) {
         //TODO Auto-generated method stub
        switch (view.getId()) {
        case R.id.buttonPlay:
            rePlay();
            break;
        case R.id.buttonPause:
            pause();
            break;
        case R.id.buttonPlayPre:
            playPre();
            break;
        case R.id.buttonPlayNext:
            playNext();
            break;
        default:
            break;
        }
    }

    @Override
    public void OnServiceConnectComplete() {
        // TODO Auto-generated method stub
        String state = Environment.getExternalStorageState().toString();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
        } else {
            return;
        }

        int playState = mServiceManager.getPlayState();
        switch (playState) {
        case MediaPlayState.MPS_NOFILE:
//            mVedioList = MediaUtils.getVideoFileList(mContext);
//            mServiceManager.refreshVideoList(mVedioList);
            break;
        case MediaPlayState.MPS_INVALID:
        case MediaPlayState.MPS_PREPARE:
        case MediaPlayState.MPS_PLAYING:
        case MediaPlayState.MPS_PAUSE:
//            mVedioList = mServiceManager.getFileList();
            long time2 = System.currentTimeMillis();
            break;
        default:
            break;
        }

        if (mVedioList.size() > 0) {
            mIsHaveData = true;
        }
    }

    public void setPlayInfo(int curTime, int totalTime, String musicName, boolean loadingInfo) {
        curTime /= 1000;
        totalTime /= 1000;
        int curminute = curTime / 60;
        int cursecond = curTime % 60;

        String curTimeString = String.format("%02d:%02d", curminute, cursecond);

        int totalminute = totalTime / 60;
        int totalsecond = totalTime % 60;
        String totalTimeString = String.format("%02d:%02d", totalminute,
                totalsecond);

        int rate = 0;
        if (totalTime != 0) {
            rate = (int) ((float) curTime / totalTime * 100);
        }

        mPlayProgress.setProgress(rate);

        mcurtimeTextView.setText(curTimeString);
        mtotaltimeTextView.setText(totalTimeString);
        if (loadingInfo) {
            if (musicName != null) {
                mPlaySongTextView.setText(musicName);
            }
        }

    }

    public void showPlay(boolean flag) {
        if (flag) {
            mBtnPlay.setVisibility(View.VISIBLE);
            mBtnPause.setVisibility(View.GONE);
        } else {
            mBtnPlay.setVisibility(View.GONE);
            mBtnPause.setVisibility(View.VISIBLE);
        }

    }

    public void showNoData() {

    }

    public void rePlay() {
        if (mIsHaveData == false) {
            showNoData();
        } else {
            mServiceManager.rePlay();
        }

    }

    public void pause() {
        mServiceManager.pause();
    }

    public void playPre() {
        if (!mIsHaveData) {
            showNoData();
        } else {
            mServiceManager.playPre();
        }

    }

    public void playNext() {
        if (!mIsHaveData) {
            showNoData();
        } else {
            mServiceManager.playNext();
        }

    }

    public void seekTo(int rate) {
        mServiceManager.seekTo(rate);
    }

    public void exit() {
        mServiceManager.pause();
        showPlay(false);
        
    }

}
