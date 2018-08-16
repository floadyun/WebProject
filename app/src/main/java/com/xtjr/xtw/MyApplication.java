package com.xtjr.xtw;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/8/16
 * @description:
 */
import android.app.Application;
import android.util.Log;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;
/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/11
 * @description:
 */
public class MyApplication extends Application {

    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        Stetho.initializeWithDefaults(this);

        initLogger();

        initARouter();

        initX5Web();
    }
    public static MyApplication getApplication(){
        return application;
    }
    /**
     * 初始化日志打印
     */
    private void initLogger(){
        AndroidLogAdapter logAdapter = new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        };
        Logger.addLogAdapter(logAdapter);
    }
    private void initARouter(){
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this);
    }
    /**
     * 初始化X5内核
     */
    private void initX5Web(){
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
