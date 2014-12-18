package com.yixi.window.view;

import java.util.ArrayList;
import java.util.List;

import com.yixi.window.R;
import com.yixi.window.data.IMediaData;
import com.yixi.window.data.MediaPlayState;
import com.yixi.window.data.MusicPlayer;
import com.yixi.window.utils.MediaTimer;
import com.yixi.window.utils.MediaUtils;
import com.yixi.window.view.FloatWindowBigView2.ActionCallBack;
import com.yixi.window.service.IOnServiceConnectComplete;
import com.yixi.window.service.ServiceManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FloatMusicView extends LinearLayout implements OnClickListener,
        OnSeekBarChangeListener, IOnServiceConnectComplete, ActionCallBack {
    private ServiceManager mServiceManager;
    private static final int REFRESH_PROGRESS_EVENT = 0x0010;
    private boolean mIsSdExist = false;
    private boolean mIsHaveData = false;
    public View mLayoutView;
    public TextView mPlaySongTextView;
    public ImageView mAritstImage;

    public SeekBar mPlayProgress;
    public TextView mcurtimeTextView;
    public TextView mtotaltimeTextView;

    public ImageButton mBtnPlay;
    public ImageButton mBtnPause;
    public ImageButton mBtnPlayNext;
    public ImageButton mBtnPlayPre;

    private boolean mPlayAuto = true;

    private MusicPlayStateBrocast mPlayStateBrocast;
    private Context mContext;

    private List<IMediaData> mMusicList;
    private int mCurMusicTotalTime;
    private MediaTimer mMusicTimer;
    private Handler mHandler;
    private Drawable mMusicImg;

    public FloatMusicView(Context context, AttributeSet ats) {
        super(context, ats);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutView = inflater.inflate(R.layout.audio_player, this, true);

        initView();
        init();
        registerMusicContentObserver();
    }

    public FloatMusicView(Context context) {
        super(context);
    }

    private void initView() {

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
                .findViewById(R.id.song_title);
        mAritstImage = (ImageView) mLayoutView.findViewById(R.id.aritst_image);
        mcurtimeTextView = (TextView) mLayoutView
                .findViewById(R.id.textViewCurTime);
        mtotaltimeTextView = (TextView) mLayoutView
                .findViewById(R.id.textViewTotalTime);

        mPlayProgress = (SeekBar) mLayoutView.findViewById(R.id.seekBar);
        mPlayProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        if (mPlayAuto == false) {
            mServiceManager.seekTo(progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
        mPlayAuto = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
        // TODO Auto-generated method stub
        mPlayAuto = true;
    }

    @Override
    public void onClick(View view) {
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
            mIsSdExist = true;
        } else {
            return;
        }

        int playState = mServiceManager.getPlayState();
        switch (playState) {
        case MediaPlayState.MPS_NOFILE:
            mMusicList = MediaUtils.getMusicFileList(mContext);
            mServiceManager.refreshMusicList(mMusicList);
            break;
        case MediaPlayState.MPS_INVALID:
        case MediaPlayState.MPS_PREPARE:
        case MediaPlayState.MPS_PLAYING:
        case MediaPlayState.MPS_PAUSE:
            long time1 = System.currentTimeMillis();
            mMusicList = mServiceManager.getFileList();
            long time2 = System.currentTimeMillis();
            mServiceManager.sendPlayStateBrocast();
            break;
        default:
            break;
        }

        if (mMusicList.size() > 0) {
            mIsHaveData = true;
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

    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        mContext.registerReceiver(mPhoneReceiver, intentFilter);
        mPlayStateBrocast = new MusicPlayStateBrocast();
        IntentFilter intentFilter1 = new IntentFilter(MusicPlayer.BROCAST_NAME);
        mContext.registerReceiver(mPlayStateBrocast, intentFilter1);
        mServiceManager = new ServiceManager(mContext);
        mServiceManager.setOnServiceConnectComplete(this);
        mServiceManager.connectService();

        mMusicList = new ArrayList<IMediaData>();
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch (msg.what) {
                case REFRESH_PROGRESS_EVENT:
                    setPlayInfo(mServiceManager.getCurPosition(),
                            mCurMusicTotalTime, null, false);
                    break;
                default:
                    break;
                }
            }

        };
        mMusicTimer = new MediaTimer(mHandler, REFRESH_PROGRESS_EVENT);

    }

    class MusicPlayStateBrocast extends BroadcastReceiver {

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

        mMusicImg = MediaUtils.getArtworkFromFile(
                mContext, data.mMediaId, data.mAritstId);
        switch (playState) {
        case MediaPlayState.MPS_INVALID:
            mMusicTimer.stopTimer();
            setPlayInfo(0, data.mMediaTime, data.mMediaName, true);
            showPlay(true);
            break;
        case MediaPlayState.MPS_PREPARE:
            mMusicTimer.stopTimer();
            mCurMusicTotalTime = data.mMediaTime;
            if (mCurMusicTotalTime == 0) {
                mCurMusicTotalTime = mServiceManager.getDuration();
            }
            setPlayInfo(0, data.mMediaTime, data.mMediaName, true);
            showPlay(true);
            break;
        case MediaPlayState.MPS_PLAYING:
            mMusicTimer.startTimer();
            if (mCurMusicTotalTime == 0) {
                mCurMusicTotalTime = mServiceManager.getDuration();
            }
            setPlayInfo(mServiceManager.getCurPosition(), data.mMediaTime,
                    data.mMediaName, true);
            showPlay(false);
            break;
        case MediaPlayState.MPS_PAUSE:
            mMusicTimer.stopTimer();
            if (mCurMusicTotalTime == 0) {
                mCurMusicTotalTime = mServiceManager.getDuration();
            }
            setPlayInfo(mServiceManager.getCurPosition(), data.mMediaTime,
                    data.mMediaName, true);
            showPlay(true);
            break;
        default:
            break;
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
                mAritstImage.setBackgroundDrawable(mMusicImg);
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

    public void registerMusicContentObserver() {
        MusicObserver musicContent = new MusicObserver(new Handler());
        mContext.getContentResolver().registerContentObserver(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, musicContent);
        }

    class MusicObserver extends ContentObserver {

        public MusicObserver(Handler handler) {
            super(handler);
            
        }
        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            super.onChange(selfChange);
        }
    }

    @Override
    public void doAction() {
        // TODO Auto-generated method stub
        exit();

    }

    private BroadcastReceiver mPhoneReceiver = new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent action) {
            TelephonyManager tm = (TelephonyManager) context 
                    .getSystemService(Service.TELEPHONY_SERVICE); 
            if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
                mServiceManager.rePlay();
            } else {
                mServiceManager.pause();
            }
        }
        
    };
}
