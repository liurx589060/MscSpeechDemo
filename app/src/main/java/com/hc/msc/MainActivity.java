package com.hc.msc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hc.lib.msc.HCMscParams;
import com.hc.lib.msc.MscDefaultSpeech;
import com.hc.msc.demo.R;

import java.util.Random;

public class MainActivity extends Activity {
    private Button mBtnMerge;
    private Button mBtnPause;
    private Button mBtnResume;
    private Button mBtnStop;
    private Button mBtnSelecter;
    private EditText mEdit;

    private String mContent[] = {
//            "报道称，中国男队尽情地将又一个团体冠军带回家。中国女队也在庆祝获得团体冠军。此次世乒赛证明" +
//                    "，中国仍是世界毫无对手的乒乓霸主。只有最勇敢的乐观主义者才相信能战胜中国",
//            "5月5日，是马克思诞辰200周年纪念日。让我们跟随习近平总书记的讲述，走进欧洲，追寻马克思的足迹，探寻这位“千年第一思想家”的一生",
//            "“1843年移居巴黎后，马克思积极参与工人运动，在革命实践和理论探索的结合中完成了从唯心主义到唯物主义、从革命民主主义到共产主义的转变。”——习近平",
//            "On May 5, is the 200th anniversary of Marx. Let's follow Mr Xi general secretary, walked into Europe, pursuit of Marx's footsteps, to explore the life \"in one thousand, the first thinker\"\n"
            "尊敬的用户请注意，现在软件园一期5栋发生火警，起火部位是x层x房间，请不要惊慌，已根据火情位置为您规划了逃生路线，" +
                    "按照逃生路线指引可有序疏散撤离到安全地带，疏散时不要乘坐电梯，如遇烟火，请保持低姿前行，如有可能请用打湿的毛巾或其他物品堵住口鼻，保持冷静"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }



    private void init() {
        mBtnMerge = findViewById(R.id.btn_merge);
        mEdit = findViewById(R.id.editText);
        mBtnPause = findViewById(R.id.btn_pause);
        mBtnResume = findViewById(R.id.btn_resume);
        mBtnStop = findViewById(R.id.btn_stop);
        mBtnSelecter = findViewById(R.id.btn_select);

        MscDefaultSpeech.getInstance().getMscParams().setEngineType(HCMscParams.LOCAL);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_merge:
//                        MscManager.mscStartSpeaking(mTts,mContent,hcMscParams,mySynthesizerListener);
                        Random random = new Random();
                        int index = random.nextInt(mContent.length);
                        MscDefaultSpeech.getInstance().startSpeaking(MainActivity.this,mContent[index]);
                        break;

                    case R.id.btn_pause:
                        MscDefaultSpeech.getInstance().pauseSpeaking();
                        break;

                    case R.id.btn_resume:
                        MscDefaultSpeech.getInstance().resumeSpeaking();
                        break;

                    case R.id.btn_stop:
                        MscDefaultSpeech.getInstance().stopSpeaking();
                        break;

                    case R.id.btn_select:
                        MscDefaultSpeech.getInstance().selectVoicerSpeaking(MainActivity.this);
                        break;
                }
            }
        };

        mBtnStop.setOnClickListener(listener);
        mBtnMerge.setOnClickListener(listener);
        mBtnPause.setOnClickListener(listener);
        mBtnResume.setOnClickListener(listener);
        mBtnSelecter.setOnClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MscDefaultSpeech.getInstance().destroy();
    }
}
