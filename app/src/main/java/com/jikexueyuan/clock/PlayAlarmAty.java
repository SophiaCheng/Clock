package com.jikexueyuan.clock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by chengcheng on 10/3/15.
 */
public class PlayAlarmAty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_player_aty);
        mp = MediaPlayer.create(this, R.raw.music);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mp.stop();
        mp.release();
    }

    //音乐播放器
    private MediaPlayer mp;
}
