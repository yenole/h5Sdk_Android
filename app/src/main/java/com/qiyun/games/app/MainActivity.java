package com.qiyun.games.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.qiyun.lib.h5sdk.H5Sdk;
import com.qiyun.lib.h5sdk.H5SdkListener;

public class MainActivity extends Activity {
    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        this.startActivity(new Intent(this,GameActivity.class));
    }
}
