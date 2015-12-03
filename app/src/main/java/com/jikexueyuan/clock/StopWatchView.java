package com.jikexueyuan.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chengcheng on 10/4/15.
 */
public class StopWatchView extends LinearLayout {
    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvHour = (TextView) findViewById(R.id.timeHour);
        tvHour.setText("0");
        tvMin = (TextView) findViewById(R.id.timeMin);
        tvMin.setText("0");
        tvSec = (TextView) findViewById(R.id.timeSec);
        tvSec.setText("0");
        tvMSec = (TextView) findViewById(R.id.timeMSec);
        tvMSec.setText("0");

        btnLap = (Button) findViewById(R.id.btnSWLap);
        btnLap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.insert(String.format("%d:%d:%d:%d", tenMSecs / 100 / 60 / 60, tenMSecs / 100 / 60 % 60, tenMSecs / 100 % 60, tenMSecs % 100 ),0);
            }
        });
        btnPause = (Button) findViewById(R.id.btnSWPause);
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime();

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });
        btnReset = (Button) findViewById(R.id.btnSWReset);
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 stopTime();

                tenMSecs = 0;
                adapter.clear();

                btnReset.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnLap.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);

            }
        });
        btnResume = (Button) findViewById(R.id.btnSWResume);
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });
        btnStart = (Button) findViewById(R.id.btnSWStart);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnLap.setVisibility(View.VISIBLE);
            }
        });

        btnReset.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnLap.setVisibility(View.GONE);


        lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);

        //显示刷新
        showTimeTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        //1s刷新5次
        timer.schedule(showTimeTask, 200, 200);
    }

    private void startTimer(){
        //精确到0.01ms,每隔10ms执行一次，不能以这个频率刷新呈现，呈现时候不宜这么快
        if(timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    tenMSecs++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }

    private void stopTime(){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    private int tenMSecs = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    //呈现
    private TimerTask showTimeTask = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSecs / 100 / 60 / 60 + "");
                    tvMin.setText(tenMSecs / 100 / 60  % 60+ "");
                    tvSec.setText(tenMSecs / 100 % 60 + "");
                    tvMSec.setText(tenMSecs % 100 + "");
                    break;

            }
        }
    };

    private static final int MSG_WHAT_SHOW_TIME = 1;

    private ListView lvTimeList;
    private TextView tvHour, tvMin, tvSec, tvMSec;
    private Button btnStart, btnResume, btnReset, btnLap, btnPause;
    private ArrayAdapter<String> adapter;

    public void onDestroy() {
        timer.cancel();
    }
}






































