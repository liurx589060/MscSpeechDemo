package com.hc.lib.msc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class MscDefaultSpeech extends MscSpeech{
    private static final class MscSpeechHoler {
        private static final MscDefaultSpeech INSTANCE = new MscDefaultSpeech();
    }

    public static MscDefaultSpeech getInstance() {
        return MscSpeechHoler.INSTANCE;
    }

    private MscDefaultSpeech() {
    }
}
