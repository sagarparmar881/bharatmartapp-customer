package com.freshfastfoods;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

import io.customerly.Customerly;

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //Customerly.configure(this, "YOUR_CUSTOMERLY_APP_ID");
        Customerly.configure(this, "b63cf0fd");
    }
}