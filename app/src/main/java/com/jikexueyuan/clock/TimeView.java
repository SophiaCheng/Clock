package com.jikexueyuan.clock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by chengcheng on 10/2/15.
 */
public class TimeView extends LinearLayout {

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public TimeView(Context context) {
        super(context);
    }

    @Override
    //after initialize 在初始化之后执行的操作
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText("Hello");

        timerHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == View.VISIBLE){
            timerHandler.sendEmptyMessage(0);
        } else{
            timerHandler.removeMessages(0);
        }
    }

    private TextView tvTime;

    private void refreshTime(){
        Calendar c = Calendar.getInstance();

        tvTime.setText(String.format("%d:%d:%d",c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),c.get(Calendar.SECOND)));
    }

    private Handler timerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            refreshTime();

            if(getVisibility() == View.VISIBLE){
                //如果当前可现，每隔1秒刷新一次
                timerHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
}