package com.yixi.window.service;

import com.yixi.window.data.IMediaData;
interface IMediaConnect {

   void refreshMusicList(in List<IMediaData> mediaFileList);
   
   void getFileList(out List<IMediaData> mediaFileList);
   
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


