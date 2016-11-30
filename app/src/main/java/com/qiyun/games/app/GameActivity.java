package com.qiyun.games.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import com.qiyun.lib.h5sdk.H5Sdk;
import com.qiyun.lib.h5sdk.H5SdkListener;


/**
 * Created by Yenole on 2016/11/1.
 */
public class GameActivity  extends Activity{
    ProgressDialog dialog =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dialog = ProgressDialog.show(this, null, "请稍等，游戏加载中...", true, false);
        H5Sdk.shared().init(this);
        H5Sdk.shared().setBackgroundColor(Color.parseColor("#243145"));
        H5Sdk.shared().addListener(new H5SdkListener() {
            @Override
            public void onHandler(String method, String[] args) {
                if ("close".equals(method)) {
                    H5Sdk.shared().dismis();
                    GameActivity.this.finish();
                } else if ("pageFinished".equals(method)) {
                    if (GameActivity.this.dialog!=null){
                        GameActivity.this.dialog.dismiss();
                    }
                }
            }
        });
        H5Sdk.shared().show("http://123.207.250.159:8083/yzdb/?t=974c1e79ca8de5f643f4fa6c28908cb6c34db332&uid=90");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        H5Sdk.shared().dismis();
    }
}
