package com.yixi.window.service;

import java.util.List;

import com.yixi.window.data.IMediaData;
import com.yixi.window.data.MusicPlayer;
import com.yixi.window.service.IMediaConnect.Stub;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MediaService extends Service {

    private static final String TAG = "MusicService";

    private MusicPlayer mMusicPlayer;

    private SDStateBrocast mSDStateBrocast;
    private AudioManager mAudioManager;

    private boolean mPausedByTransientLossOfFocus;
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        mMusicPlayer = new MusicPlayer(this);

        mSDStateBrocast = new SDStateBrocast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addDataScheme("file");
        registerReceiver(mSDStateBrocast, intentFilter);
        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        unregisterReceiver(mSDStateBrocast);

        super.onDestroy();

    }

    private IMediaConnect.Stub mBinder = new Stub() {

        @Override
        public void refreshMusicList(List<IMediaData> musicFileList)
                throws RemoteException {
            // TODO Auto-generated method stub
            mMusicPlayer.refreshMusicList(musicFileList);
        }

        @Override
        public void getFileList(List<IMediaData> musicFileList)
                throws RemoteException {
            // TODO Auto-generated method stub
            List<IMediaData> tmp = mMusicPlayer.getFileList();
            int count = tmp.size();
            for (int i = 0; i < count; i++) {
                musicFileList.add(tmp.get(i));
            }
        }

        @Override
        public int getCurPosition() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.getCurPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.getDuration();
        }

        @Override
        public boolean pause() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.pause();
        }

        @Override
        public boolean play(int position) throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.play(position);
        }

        @Override
        public boolean playNext() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.playNext();
        }

        @Override
        public boolean playPre() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.playPre();
        }

        @Override
        public boolean rePlay() throws RemoteException {
            // TODO Auto-generated method stub
            mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            return mMusicPlayer.replay();
        }

        @Override
        public boolean seekTo(int rate) throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.seekTo(rate);
        }

        @Override
        public boolean stop() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.stop();
        }

        @Override
        public int getPlayState() throws RemoteException {
            // TODO Auto-generated method stub
            return mMusicPlayer.getPlayState();
        }

        @Override
        public void exit() throws RemoteException {
            // TODO Auto-generated method stub
            mMusicPlayer.exit();
        }

        @Override
        public void sendPlayStateBrocast() throws RemoteException {
            // TODO Auto-generated method stub
            mMusicPlayer.sendPlayStateBrocast();
        }

    };


    private  OnAudioFocusChangeListener mAudioFocusChangeListener = new OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d("apple", ">>>>>>> focusChange = " + focusChange);
            Log.d("apple", ">>>>>>> mPausedByTransientLossOfFocus = " + mPausedByTransientLossOfFocus);
            if (mMusicPlayer == null) {
                Log.d("apple", ">>>>>>>>>>>>222>> ");
                mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
                
            }
            switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                mMusicPlayer.pause();
                mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mMusicPlayer.isPlaying()) {
                    Log.d("apple", ">>>>>>>>>>>>>> ");
                    mPausedByTransientLossOfFocus = true;
                    mMusicPlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mPausedByTransientLossOfFocus) {
                    mPausedByTransientLossOfFocus = false;
                    mMusicPlayer.replay();
                    mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }
                break;
            default:
                break;
            }

        }
        
    };

    class SDStateBrocast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {

            } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {

            } else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {

            } else if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
                mMusicPlayer.exit();
            }

        }
    }
}
