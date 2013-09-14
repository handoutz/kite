package com.hatch.kite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hatch.kite.api.TesterApplication;

/**
 * Created by vince on 9/14/13.
 */
public class AppDetailActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist_detail);
        Intent i = getIntent();
        TesterApplication app = (TesterApplication)i.getSerializableExtra("app");
        ((TextView) findViewById(R.id.ald_appName)).setText(app.getAppName());
        ((TextView) findViewById(R.id.ald_appDesc)).setText(app.getAppDesc());
    }
}