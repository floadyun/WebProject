package com.xtjr.xtw;

import android.support.v4.app.ActivityOptionsCompat;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/17
 * @description:页面路由管理器
 */
public class RouterManager {
    //引导页
    public static final String GUIDE_ACTIVITY = "/activity/GuideActivity";
    //主页
    public static final String MAIN_ACTIVITY = "/activity/MainActivity";
    //基础WebView
    public static final String WEB_ACTIVITY = "/activity/BaseWebActivity";
    //参数传递
    public static final String INTENT_WEB_URL = "intent_web_url";
    /**
     * 普通跳转，不需要传递参数
     * @param compat 跳转动画属性
     * @param path 路由路径
     */
    public static final void startNormalActivity(ActivityOptionsCompat compat, String path){
        ARouter.getInstance().build(path).withOptionsCompat(compat).navigation();
    }
    /**
     * 跳转至Web基类
     * @param compat
     * @param webUrl 跳转链接
     */
    public static final void startWebActivity(ActivityOptionsCompat compat, String webUrl){
        ARouter.getInstance().build(WEB_ACTIVITY).withOptionsCompat(compat).withString(INTENT_WEB_URL,webUrl).navigation();
    }
}
