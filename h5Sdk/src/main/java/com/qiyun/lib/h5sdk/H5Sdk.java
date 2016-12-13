package com.qiyun.lib.h5sdk;

import android.app.Activity;
import android.graphics.Color;
import android.os.PowerManager;
import android.view.ViewGroup;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import android.widget.FrameLayout;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Calvin on 2016/3/17.
 */
public class H5Sdk {
    static H5Sdk instance = null;
    Activity activity;
    H5SdkListener listener;
    int backgroundColor = Color.TRANSPARENT;
    String url;
    WebView webView;

    private H5Sdk() {

    }

    public static H5Sdk shared() {
        return instance == null ? (instance = new H5Sdk()) : instance;
    }

    public void init(Activity activity) {
        this.activity = activity;
    }

    public void addListener(H5SdkListener listener) {
        this.listener = listener;
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        if (null != activity && null != webView) {
            webView.setBackgroundColor(backgroundColor);
        }
    }

    public void show(String url, int x, int y, int width, int height) {
        PowerManager pm = (PowerManager) activity.getSystemService(activity.POWER_SERVICE);
        if (!pm.isScreenOn())return;
        this.url = url;
        if (null != activity) {
            if(null==webView){
                webView = new WebView(this.activity);
                webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setDatabaseEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                webView.getSettings().setAppCacheEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView webView, String s) {
                        super.onPageFinished(webView, s);
                        handlerGMCall("gmcall://pageFinished");
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (url.startsWith("gmcall")) {
                            handlerGMCall(url);
                            return true;
                        }
                        view.loadUrl(url);
                        return true;
                    }
                });
            }
            webView.setBackgroundColor(backgroundColor);
            webView.loadUrl(url);
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(width, height);
            flp.leftMargin = x;
            flp.topMargin = y;
            if (webView.getParent()!=null){
                ((ViewGroup) webView.getParent()).removeView(webView);
            }
            activity.getWindow().addContentView(webView, flp);
        }
    }

    public void show(String url) {
        this.show(url, 0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void dismis() {
        if (null != activity && webView != null) {
            this.webView.loadUrl("about:block");
            ((ViewGroup) webView.getParent()).removeView(webView);
        }
    }

    private void handlerGMCall(String url) {
        url = url.substring(9);
        String method;
        String[] args;
        if (url.indexOf("?") >= 0) {
            method = url.substring(0, url.indexOf("?"));
            args = url.substring(method.length() + 1).split("&");
        } else {
            method = url;
            args = new String[0];
        }
        if (null != listener) {
            listener.onHandler(method, args);
        }
    }
}
