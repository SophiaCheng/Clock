package com.jikexueyuan.clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chengcheng on 10/2/15.
 */
public class AlarmView extends LinearLayout {
    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

//    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }

    private void init(){
        //设置闹钟服务
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);

        adapter = new ArrayAdapter<AlarmData>(getContext(),android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);

        //设置好adapter之后就读取数据
        readSavedAlarmList();

        //adapter.add(new AlarmData(System.currentTimeMillis()));

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });

        //实现长按、删除预定时间
        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消", null).show();

                return true;
            }
        });
    }

    //删除方法
    private void deleteAlarm(int position){
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);

        //重新保存
        saveAlarmList();

        alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getID(), new Intent(getContext(), AlarmReceiver.class), 0));
    }

    private void addAlarm(){
        //TODO

        Calendar c = Calendar.getInstance();

        //时间选择框
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //设置闹钟时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                //描述清零
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND ,0);

                //判断当前时间与闹钟时间关系
                Calendar currentTime = Calendar.getInstance();

                if(calendar.getTimeInMillis() <= currentTime.getTimeInMillis()){
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }

                AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                adapter.add(ad);
                //设置闹钟
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        ad.getTime(),
                        5 * 60 * 1000,
                        PendingIntent.getBroadcast(getContext(), ad.getID(), new Intent(getContext(), AlarmReceiver.class),0));
                saveAlarmList();

            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    //如何存储数据，在activity重启之后也能保存
    private void saveAlarmList(){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

        //存储数据
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < adapter.getCount(); i ++){
            sb.append(adapter.getItem(i).getTime()).append(",");
        }

        if(sb.length() > 1){
            String content =  sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);

            System.out.println(content);
        } else{
            editor.putString(KEY_ALARM_LIST, null);
        }



        //填完数据后提交
        editor.commit();
    }

    //读取数据
    private void readSavedAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        //获取数据内容
        String content = sp.getString(KEY_ALARM_LIST, null);


        if(content != null){
            String[] timeStrings = content.split(",");
            for(String string : timeStrings){
                adapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }


    private Button btnAddAlarm;
    private ListView lvAlarmList;
    private static final String KEY_ALARM_LIST = "alarmList";

    //用系统的闹钟服务设置闹钟
    private AlarmManager alarmManager;


    //为listView添加数据
    private ArrayAdapter<AlarmData> adapter;

    private static class AlarmData{
        public AlarmData(long time){
            this.time = time;

            data = Calendar.getInstance();
            data.setTimeInMillis(time);

            timeLabel  =  String.format("%d月%d日 %d:%d",
                    data.get(Calendar.MONTH) + 1,
                    data.get(Calendar.DAY_OF_MONTH),
                    data.get(Calendar.HOUR_OF_DAY),
                    data.get(Calendar.MINUTE));
        }

        public long getTime() {
            return time;
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }

        public int getID(){
            return (int)(getTime() / 1000 / 60);
        }

        private String timeLabel = "";
        private long time = 0;
        private Calendar data;
    }



}



































