package com.yixi.window.service;

import com.yixi.window.data.MusicData;
interface MusicConnect {

   void refreshMusicList(in List<MusicData> musicFileList);
   
   void getFileList(out List<MusicData> musicFileList);
   
   boolean rePlay();
   
   boolean play(int position);
   
   boolean pause();
   
   boolean stop();
   
   boolean playNext();
   
   boolean playPre();
   
   boolean seekTo(int rate);
   
   int getCurPosition();
   
   int getDuration();
   
   int getPlayState();
   
   void sendPlayStateBrocast();
   
   void exit();
}


