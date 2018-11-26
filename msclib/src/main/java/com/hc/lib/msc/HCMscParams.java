package com.hc.lib.msc;

import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;

/**
 * Created by liurunxiong on 2018/5/5.
 */

public class HCMscParams {
    public static final String CLOUND = SpeechConstant.TYPE_CLOUD;
    public static final String LOCAL = SpeechConstant.TYPE_LOCAL;
    private String engineType = LOCAL; //引擎，在线/本地
    private String speed = "50";        //音速
    private String pitch = "50";        //音调
    private String volume = "50";       //音量
    private String stream = "3";        //音频流类型
    private boolean interruptMusic = true;  //是否打断音乐播放
    private String format = "wav";       //音频格式
    private String savePath = Environment.getExternalStorageDirectory()+"/msc/tts.wav";            //保持路径
    private String voicer = "xiaoyan";  //合成人

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public boolean isInterruptMusic() {
        return interruptMusic;
    }

    public void setInterruptMusic(boolean interruptMusic) {
        this.interruptMusic = interruptMusic;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getVoicer() {
        return voicer;
    }

    public void setVoicer(String voicer) {
        this.voicer = voicer;
    }
}
