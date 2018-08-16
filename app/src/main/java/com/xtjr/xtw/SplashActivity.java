package com.xtjr.xtw;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.base.lib.update.utils.AppUpdateUtils;
import com.base.lib.util.PreferenceUtil;
import com.base.lib.util.ScreenUtils;
import com.orhanobut.logger.Logger;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @copyright : 深圳车发发科技有限公司
 * Created by yixf on 2017/11/13.
 * @description:APP启动页
 */
public class SplashActivity extends BaseActivity implements Runnable {
    //倒计时5秒进入
    private static final int TIME_COUNT = 2;

    private boolean isGoHome = true;

    private int time = TIME_COUNT;
    @BindView(R.id.splash_goto_view)
    RelativeLayout gotoView;
    //时间倒计时
    @BindView(R.id.splash_progress_time)
    TextView progressText;
    //广告页
    @BindView(R.id.app_splash_img)
    ImageView app_splash_img;
    //图文广告链接
    private String httpUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressText.setText(msg.arg1 + "s 跳过");
        }
    };
    @Override
    protected void initView() {
        setViewLayout(R.layout.activity_splash);
        hideTitle(R.color.white);
        Logger.d("屏幕宽度为:"+ScreenUtils.getScreenWidth(this)+";屏幕高度为:"+ScreenUtils.getScreenHeight(this));
        startTimeUpdate();
    }
    /**
     * 开始广告倒计时
     */
    private void startTimeUpdate() {
        try {
            startTimeCount();
            getAdvertContent();
        } catch (Exception e) {
            gotoGuideAcvitity();
        }
    }
    /**
     * 加载广告内容
     */
    private void getAdvertContent() {

    }
    /**
     * 开始倒计时
     */
    private void startTimeCount() {
        new Thread(SplashActivity.this).start();
    }
    /**
     * 跳转至广告链接
     */
    @OnClick(R.id.app_splash_img)
    void toHttpUrl() {
        if (null != httpUrl && !httpUrl.isEmpty()) {
            gotoGuideAcvitity();
            RouterManager.startWebActivity(compat,httpUrl);
        }
    }
    /**
     * 不能返回
     */
    @Override
    public void onBackPressed() {

    }
    @Override
    public void run() {
        while (isGoHome) {
            try {
                Thread.sleep(1000);
                if (time > 0) {
                    Message msg = new Message();
                    msg.arg1 = time;
                    handler.sendMessage(msg);
                    time--;
                } else {
                    isGoHome = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gotoGuideAcvitity();
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isGoHome = false;
    }
    /**
     * 跳过广告页
     */
    @OnClick(R.id.splash_goto_view) void gotoAcvitity() {
        gotoGuideAcvitity();
    }
    /**
     * 跳转至向导页面
     */
    private void gotoGuideAcvitity() {
        isGoHome = false;
        String versionName = PreferenceUtil.getPreference(this).getStringPreference(Consts.VERSION_NAME, AppUpdateUtils.getVersionName(this));
        if (PreferenceUtil.getPreference(this).getBoolPreference(Consts.IS_FIRST, true)||!versionName.equals(AppUpdateUtils.getVersionName(this))) {
            RouterManager.startNormalActivity(compat,RouterManager.GUIDE_ACTIVITY);
        } else {
            RouterManager.startNormalActivity(compat,RouterManager.MAIN_ACTIVITY);
        }
        finishSelf();
    }
}
