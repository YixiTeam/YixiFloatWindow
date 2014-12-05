package com.yixi.window.service;

import java.util.List;

import com.yixi.window.service.MusicConnect;
import com.yixi.window.service.MusicConnect.Stub;
import com.yixi.window.data.MusicData;
import com.yixi.window.data.MusicPlayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MusicService extends Service {

    private static final String TAG = "MusicService";

    private MusicPlayer mMusicPlayer;

    private SDStateBrocast mSDStateBrocast;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
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

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        unregisterReceiver(mSDStateBrocast);

        super.onDestroy();

    }

    private MusicConnect.Stub mBinder = new Stub() {

        @Override
        public void refreshMusicList(List<MusicData> musicFileList)
                throws RemoteException {
            // TODO Auto-generated method stub
            mMusicPlayer.refreshMusicList(musicFileList);
        }

        @Override
        public void getFileList(List<MusicData> musicFileList)
                throws RemoteException {
            // TODO Auto-generated method stub
            List<MusicData> tmp = mMusicPlayer.getFileList();
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
