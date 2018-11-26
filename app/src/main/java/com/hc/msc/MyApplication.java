package com.hc.msc;

import android.app.Application;

import com.hc.lib.msc.MscManager;
import com.hc.lib.msc.MscSpeech;
import com.iflytek.cloud.SpeechUtility;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
//        MscManager.initMsc(this);
        MscManager.initMsc(this);
        super.onCreate();
    }
}
