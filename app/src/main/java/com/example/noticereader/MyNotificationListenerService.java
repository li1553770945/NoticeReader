package com.example.noticereader;

import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyNotificationListenerService extends NotificationListenerService {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getNotification().tickerText != null)
        {
            handleMsg(sbn);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleMsg(StatusBarNotification sbn)
    {
        if(sbn.getPackageName().equals("com.tencent.mobileqq"))
        {
            Bundle bundle = sbn.getNotification().extras;
            String name = bundle.getString("android.title");
            String message = bundle.getString("android.text");
            if(name.indexOf("昵称",0)!=-1)
            {
                read(name+"说"+message);
            }
            //Toast.makeText(this, sbn.getPackageName(), Toast.LENGTH_SHORT).show();
        }

    }
    public void read(String str)
    {
        Http http = new Http();
        http.post(str);
    }

}