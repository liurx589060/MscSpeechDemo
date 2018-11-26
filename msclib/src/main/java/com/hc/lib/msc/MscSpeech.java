package com.hc.lib.msc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class MscSpeech {
    private final String TAG = "yy";
    private HCMscParams mMscParams;
    private SpeechSynthesizer mTts;
    private boolean isMscInitSuccess;
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;
    private String mSpeechText;
    private Context mContext;
    private boolean isLocalError;

    public MscSpeech() {
        mMscParams = new HCMscParams();
    }

    public void startSpeaking(Context mContext,String content) {
        this.mSpeechText = content;
        this.mContext = mContext;
        if(mTts == null) {
            this.mTts = MscManager.createMscSpeechSynthesizer(mContext, MscManager.MscCreateType.Default, new MyInitListener(mContext));
        }else {
            MscManager.mscStartSpeaking(mContext,mTts,content,mMscParams,mTtsListener);
        }
    }

    public void stopSpeaking() {
        if(!checkStatus()) return;
        MscManager.mscStopSpeaking(mTts);
    }

    public void pauseSpeaking() {
        if(!checkStatus()) return;
        MscManager.mscPauseSpeaking(mTts);
    }

    public void resumeSpeaking() {
        if(!checkStatus()) return;
        MscManager.mscResumeSpeaking(mTts);
    }

    public void selectVoicerSpeaking(Activity activity) {
        MscManager.mscSelecterVoicer(activity, mMscParams, new MscVoicerSelecterListener() {
            @Override
            public void onSelected(String voicer, String language, int index) {
                mMscParams.setVoicer(voicer);
            }
        });
    }

    public void destroy() {
        MscManager.destroy(mTts);
    }

    public void setSynthesizerListener(SynthesizerListener listener) {
        this.mTtsListener = listener;
    }

    public HCMscParams getMscParams() {
        if(mMscParams == null) {
            mMscParams = new HCMscParams();
        }
        return mMscParams;
    }

    public void setMscParams(HCMscParams params) {
        this.mMscParams = params;
    }

    private boolean checkStatus() {
        if(!isMscInitSuccess) {
            Log.e(TAG,"msc init fail");
            return false;
        }

        if(mTts == null) {
            Log.e(TAG,"mTts is null");
            return false;
        }
        return true;
    }

    private class MyInitListener implements InitListener{
        private Context mContext;

        public MyInitListener(Context context) {
            this.mContext = context;
        }

        @Override
        public void onInit(int code) {
            Log.e(TAG, "InitListener init() code = " + code);
            if (code == ErrorCode.SUCCESS) {
                isMscInitSuccess = true;
                MscManager.mscStartSpeaking(mContext,mTts,mSpeechText,mMscParams,mTtsListener);
            }else {
                isMscInitSuccess = false;
                Toast.makeText(mContext,"InitListener init() code = " + code,Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.d(TAG,"开始播放");
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG,"暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG,"继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            Log.d(TAG,String.format("缓冲进度为%d%%，播放进度为%d%%",
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            Log.d(TAG,String.format("缓冲进度为%d%%，播放进度为%d%%",
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.i(TAG,"播放完成");
            } else if (error != null) {
                Log.e(TAG,error.getPlainDescription(true));
                if(mMscParams.getEngineType().equals(HCMscParams.LOCAL)) {
                    isLocalError = true;
                    mMscParams.setEngineType(HCMscParams.CLOUND);
                    startSpeaking(mContext,mSpeechText);
                }else if (mMscParams.getEngineType().equals(HCMscParams.CLOUND)) {
                    if(isLocalError) {
                        Toast.makeText(mContext,error.getPlainDescription(true),Toast.LENGTH_LONG).show();
                    }else {
                        mMscParams.setEngineType(HCMscParams.LOCAL);
                        startSpeaking(mContext,mSpeechText);
                    }
                }
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
}
