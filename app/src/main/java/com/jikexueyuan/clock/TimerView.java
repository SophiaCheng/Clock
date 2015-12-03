package com.jikexueyuan.clock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by chengcheng on 10/3/15.
 */
public class TimerView extends LinearLayout {
    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                stopTimer();

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();

                etHour.setText("0");
                etMin.setText("0");
                etSec.setText("0");

                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
        btnResume = (Button) findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });

        etHour = (EditText) findViewById(R.id.etHour);
        etMin = (EditText) findViewById(R.id.etMin);
        etSec = (EditText) findViewById(R.id.etSec);

        etHour.setText("00");
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(s)){
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("0");
                    }
                }


                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMin.setText("00");
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("0");
                    }
                }



                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSec.setText("00");
        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("0");
                    }
                }


                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);



    }

    //获取文字有值时，启用按钮
    private void checkToEnableBtnStart(){
        btnStart.setEnabled( (!TextUtils.isEmpty(etHour.getText())) && (Integer.parseInt(etHour.getText().toString()) > 0)||
                             (!TextUtils.isEmpty(etMin.getText()))  && (Integer.parseInt(etMin.getText().toString()) > 0) ||
                             (!TextUtils.isEmpty(etSec.getText()))  && (Integer.parseInt(etSec.getText().toString()) > 0));
    }
    // 启动Timer
    private void startTimer(){
        if(timerTask == null){
            //一共需要执行的时间
            allTimerCount = Integer.parseInt(etHour.getText().toString()) * 60 * 60 +
                            Integer.parseInt(etMin.getText().toString()) * 60 +
                            Integer.parseInt(etSec.getText().toString());
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    allTimerCount --;

                    //执行倒计时每走1秒显示在文本框里
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);


                    if(allTimerCount <= 0){
                        handler.sendEmptyMessage(MSG_WAHT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };

            timer.schedule(timerTask, 1000, 1000);


        }
    }

    //计时停止
    private void stopTimer(){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

     private android.os.Handler handler = new android.os.Handler(){
         @Override
         public void handleMessage(Message msg) {
             switch (msg.what){
                 case (MSG_WHAT_TIME_TICK):

                     int hour = allTimerCount / 60 / 60;
                     int min = (allTimerCount / 60) % 60;
                     int sec = allTimerCount % 60;

                     etHour.setText(hour + "");
                     etMin.setText(min + "");
                     etSec.setText(sec + "");
                        break;
                 case (MSG_WAHT_TIME_IS_UP):
                     new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Timer is up").setNegativeButton("Cancel", null).show();
                     btnReset.setVisibility(View.GONE);
                     btnResume.setVisibility(View.GONE);
                     btnPause.setVisibility(View.GONE);
                     btnStart.setVisibility(View.VISIBLE);

                     break;
                 default:

                     break;
             }
         }
     };

    private static final int MSG_WHAT_TIME_TICK = 2;
    private static final int MSG_WAHT_TIME_IS_UP = 1;

    private int allTimerCount = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Button btnStart, btnPause, btnReset, btnResume;
    private EditText etHour, etMin, etSec;
}
























