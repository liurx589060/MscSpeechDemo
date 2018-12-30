package com.hc.lib.msc;

import com.iflytek.cloud.SpeechError;

/**
 * Created by Administrator on 2018/12/28.
 */

public interface ISpeechListener {
    void onInit(boolean isSuccess);
    void onError(int code,String msg);
    void onProgress(int percent, int beginPos, int endPos);
    void onComplete(SpeechError error);
}
