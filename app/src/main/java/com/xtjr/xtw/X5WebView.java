package com.xtjr.xtw;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/24
 * @description:
 */
public class X5WebView extends WebView {

    private Context mContext;

    public X5WebView(Context context) {
        super(context);
        this.mContext = context;
        initX5Web();
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initX5Web();
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
    private void initX5Web(){
        setWebViewClient(new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) { //http和https协议开头的执行正常的流程
                    if(url.endsWith("pdf")||url.endsWith("mp4")){//文件 调用系统浏览器
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(in);
                    }else{
                        view.loadUrl(url);
                    }
                }else {  //其他的URL则会开启一个Acitity然后去调用原生APP
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(in);
                }
                return true;
            }
        });
        WebSettings webSetting = getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
//        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //设置加载进来的页面自适应手机屏幕
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mContext.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mContext.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        CookieSyncManager.createInstance(mContext);
        CookieSyncManager.getInstance().sync();
    }
}
