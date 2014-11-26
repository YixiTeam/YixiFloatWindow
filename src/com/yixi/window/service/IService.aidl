 // My AIDL file, named IService.aidl
package com.yixi.window.service;
interface IService {
   void stopCalorie();

   void startCalorie();

   void resetData(float sensitivity,int interval);

   void saveData();
}
