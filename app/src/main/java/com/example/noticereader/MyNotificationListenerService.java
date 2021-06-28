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
            if(SettingsActivity.open)
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

            if(SettingsActivity.only_love&& !name.contains("特别关心"))//是否仅朗读特别关心
                    return;

            int end = name.indexOf("条新消息)");
            if(end!=-1)
            {
                name = name.substring(0,end-2<0?end:end-2);
            }

            int start = name.indexOf("[特别关心]");
            if(start!=-1)
            {
                name = name.substring(start+6);
            }

            read(name+"说"+message);
//
            //Toast.makeText(this, sbn.getPackageName(), Toast.LENGTH_SHORT).show();
        }

    }
    public void read(String str)
    {
        Http http = new Http();
        http.post(str);
    }

}