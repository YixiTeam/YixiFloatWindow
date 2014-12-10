package com.yixi.window.service;

import java.util.ArrayList;
import java.util.List;

import com.yixi.window.service.IMediaConnect;
import com.yixi.window.data.IMediaData;
import com.yixi.window.data.MediaPlayState;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ServiceManager {

    private final static String TAG = "ServiceManager";

    private final static String SERVICE_NAME = "com.yixi.window.service.mediaservices";

    private Boolean mConnectComplete;

    private ServiceConnection mServiceConnection;

    private IMediaConnect mMediaConnect;

    private IOnServiceConnectComplete mIOnServiceConnectComplete;

    private Context mContext;

    public ServiceManager(Context context) {
        mContext = context;
        defaultParam();
    }

    private void defaultParam() {
        Log.d(TAG, ">>>>defaultParam ");
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                Log.i(TAG, "onServiceDisconnected");

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                Log.i(TAG, "onServiceConnected");

                mMediaConnect = IMediaConnect.Stub.asInterface(service);
                if (mMediaConnect != null) {
                    if (mIOnServiceConnectComplete != null) {
                        mIOnServiceConnectComplete.OnServiceConnectComplete();
                    }
                }
            }
        };

        mConnectComplete = false;
        mMediaConnect = null;
    }

    public boolean connectService() {
        if (mConnectComplete == true) {
            return true;
        }

        Intent intent = new Intent(SERVICE_NAME);
        if (mContext != null) {
            Log.i(TAG, "begin to connectService	");
            mContext.bindService(intent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);
            mContext.startService(intent);
            mConnectComplete = true;
            return true;
        }

        return false;
    }

    public boolean disconnectService() {
        if (mConnectComplete == false) {
            return true;
        }

        Intent intent = new Intent(SERVICE_NAME);
        if (mContext != null) {
            Log.i(TAG, "begin to disconnectService");
            mContext.unbindService(mServiceConnection);
            mMediaConnect = null;
            mConnectComplete = false;
            return true;
        }

        return false;
    }

    public void exit() {
        if (mConnectComplete) {
            try {
                mMediaConnect.exit();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Intent intent = new Intent(SERVICE_NAME);
            mContext.unbindService(mServiceConnection);
            mContext.stopService(intent);
            mMediaConnect = null;
            mConnectComplete = false;

        }
    }

    public void reset() {
        if (mConnectComplete) {
            try {
                mMediaConnect.exit();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
            }
        }
    }

    public void setOnServiceConnectComplete(
            IOnServiceConnectComplete IServiceConnect) {
        mIOnServiceConnectComplete = IServiceConnect;
    }

    public void refreshMusicList(List<IMediaData> FileList) {
        if (mMediaConnect != null) {
            try {
                mMediaConnect.refreshMusicList(FileList);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public List<IMediaData> getFileList() {
        List<IMediaData> musicFileList = new ArrayList<IMediaData>();
        try {
            Log.i(TAG, "getFileList	begin...");
            mMediaConnect.getFileList(musicFileList);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return musicFileList;
    }

    public boolean rePlay() {
        if (mMediaConnect != null) {
            try {
                Log.i(TAG, "mMediaConnect.rePlay()");
                return mMediaConnect.rePlay();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean play(int position) {
        if (mMediaConnect != null) {
            try {
                Log.i(TAG, "mMediaConnect.play = " + position);
                return mMediaConnect.play(position);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;

    }

    public boolean pause() {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.pause();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean stop() {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.stop();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean playNext() {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.playNext();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean playPre() {

        if (mMediaConnect != null) {
            try {
                return mMediaConnect.playPre();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean seekTo(int rate) {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.seekTo(rate);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public int getCurPosition() {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.getCurPosition();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return 0;
    }

    public int getDuration() {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.getDuration();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return 0;
    }

    public int getPlayState() {
        if (mMediaConnect != null) {
            try {
                return mMediaConnect.getPlayState();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return MediaPlayState.MPS_NOFILE;
    }

    public void sendPlayStateBrocast() {
        if (mMediaConnect != null) {
            try {
                mMediaConnect.sendPlayStateBrocast();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
