package com.hc.lib.msc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * Created by liurunxiong on 2018/5/5.
 */

public class MscManager {
    private static final String TAG = "MSC";
    public enum MscCreateType {
        Default
    }

    public static void initMsc(Context context) {
        StringBuffer param = new StringBuffer();
        param.append("appid=" + context.getString(context.getResources()
                .getIdentifier("msc_id","string",context.getPackageName())));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(context, param.toString());

    }

    public static SpeechSynthesizer createMscSpeechSynthesizer(Context context, MscCreateType createType, final InitListener listener) {
        SpeechSynthesizer mTts = null;
        switch (createType){
            case Default:
                break;
        }
        mTts = SpeechSynthesizer.createSynthesizer(context, listener);
        return mTts;
    }

    public static int mscStartSpeaking(Context context,SpeechSynthesizer mTts, String content, HCMscParams mscParams, SynthesizerListener listener) {
        if(mTts == null) {
            Log.e(TAG,"SpeechSynthesizer is null");
            return -1;
        }
        setParams(context,mTts,mscParams);
        int code = mTts.startSpeaking(content,listener);
        if (code != ErrorCode.SUCCESS) {
            Log.e(TAG,"语音合成失败,错误码: " + code);
        }
        return code;
    }

    public static void mscPauseSpeaking(SpeechSynthesizer mTts) {
        if(mTts == null) {
            Log.e(TAG,"SpeechSynthesizer is null");
            return;
        }
        mTts.pauseSpeaking();
    }

    public static void mscResumeSpeaking(SpeechSynthesizer mTts) {
        if(mTts == null) {
            Log.e(TAG,"SpeechSynthesizer is null");
            return;
        }
        mTts.resumeSpeaking();
    }

    public static void mscStopSpeaking(SpeechSynthesizer mTts) {
        if(mTts == null) {
            Log.e(TAG,"SpeechSynthesizer is null");
            return;
        }
        mTts.stopSpeaking();
    }

    public static void mscSelecterVoicer(Activity activity, HCMscParams params,final MscVoicerSelecterListener listener) {
        String[] mCloudVoicersEntries = activity.getResources().getStringArray(R.array.voicer_cloud_entries);
        final String[] mCloudVoicersValue = activity.getResources().getStringArray(R.array.voicer_cloud_values);

        String[] mLocalVoicersEntries = activity.getResources().getStringArray(R.array.voicer_local_entries);
        final String[] mLocalVoicersValue = activity.getResources().getStringArray(R.array.voicer_local_values);

        final String[] voicerValue = params.getEngineType().equals(HCMscParams.CLOUND)?mCloudVoicersValue:mLocalVoicersValue;
        int index = 0;
        for (int i = 0 ; i < voicerValue.length ; i ++) {
            if(params.getVoicer().equalsIgnoreCase(voicerValue[i])) {
                index = i;
                break;
            }
        }
        String title = params.getEngineType().equals(HCMscParams.CLOUND)?"在线合成发音人选项":"离线合成发音人选项";
        new AlertDialog.Builder(activity).setTitle(title)
                .setSingleChoiceItems(voicerValue, // 单选框有几项,各是什么名字
                        index, // 默认的选项
                        new DialogInterface.OnClickListener() { // 点击单选框后的处理
                            public void onClick(DialogInterface dialog,
                                                int which) { // 点击了哪一项
                                String voicer = voicerValue[which];
                                String language = "";
                                if ("catherine".equals(voicer) || "henry".equals(voicer) || "vimary".equals(voicer)) {
                                    language = "en";
                                }else {
                                    language = "ch";
                                }
                                if(listener != null) {
                                    listener.onSelected(voicer,language,which);
                                }
                                dialog.dismiss();
                            }
                        }).show();
    }

    //获取发音人资源路径
    private static String getResourcePath(Context context,HCMscParams params){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+params.getVoicer()+".jet"));
        return tempBuffer.toString();
    }

    private static void setParams(Context context,SpeechSynthesizer mTts,HCMscParams params) {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if(params.getEngineType().equals(SpeechConstant.TYPE_CLOUD))
        {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,params.getVoicer());
        }else {
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath(context,params));
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,params.getVoicer());
        }
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, params.getSpeed());
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, params.getPitch());
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, params.getVolume());
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, params.getStream());

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, params.isInterruptMusic()?"true":"false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, params.getFormat());
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, params.getSavePath());
    }

    public static void destroy(SpeechSynthesizer mTs) {
        if(mTs != null) {
            mTs.stopSpeaking();
            mTs.destroy();
        }
    }
}
