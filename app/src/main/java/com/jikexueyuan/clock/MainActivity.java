package com.jikexueyuan.clock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    private TabHost tabHost;
    private StopWatchView stopWatchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("Clock").setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("Alarm").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("Timer").setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("StopWatch").setContent(R.id.tabStopWatch));

        stopWatchView = (StopWatchView) findViewById(R.id.tabStopWatch);
    }

    @Override
    protected void onDestroy() {

        stopWatchView.onDestroy();
        super.onDestroy();
    }
}










