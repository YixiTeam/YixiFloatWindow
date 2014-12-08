package com.yixi.window.service;

import com.yixi.window.data.IMusicData;
interface IMusicConnect {

   void refreshMusicList(in List<IMusicData> musicFileList);
   
   void getFileList(out List<IMusicData> musicFileList);
   
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


