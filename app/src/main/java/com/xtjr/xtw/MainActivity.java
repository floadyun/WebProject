package com.xtjr.xtw;

import android.Manifest;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.lib.http.ApiHelper;
import com.base.lib.update.UpdateAppBean;
import com.base.lib.update.UpdateAppHttpUtil;
import com.base.lib.update.UpdateAppManager;
import com.base.lib.update.UpdateCallback;
import com.base.lib.update.listener.ExceptionHandler;
import com.base.lib.util.AppManager;
import com.base.lib.util.FileUtil;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

@Route(path = RouterManager.MAIN_ACTIVITY)
public class MainActivity extends X5WebActivity {

    private String WEB_URL = "http://business.xitouwang.com:8888/user/login";

    //更新链接
    private String mUpdateUrl = "http://www.xitouwang.com/public/getLastestVersionInfo";

    private long firstTime;

    private  String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void initView() {
        web_url = WEB_URL;
        super.initView();

        updateApp();
    }
    /**
     * 最简方式
     */
    public void updateApp() {
        Map<String,String> params = new HashMap<>();
        params.put("type","1");
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(getActivity())
                .setPost(true)
                .setParams(params)
                //更新地址
                .setUpdateUrl(mUpdateUrl)
                .setTargetPath(Consts.APK_FILE_PATH)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .checkNewApp(new UpdateCallback(){
                    private String versionPath = "";
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        Logger.d("update json is "+json);
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            final String newVersion = jsonObject.optString("version_number");
                            versionPath = Consts.APK_FILE_PATH+"/"+newVersion;
                            int force = jsonObject.getInt("is_force_update");
                            boolean isForceUpdate;//是否强制更新
                            if(force==0){
                                isForceUpdate = false;
                            }else{
                                isForceUpdate = true;
                            }
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate("Yes")
                                    //（必须）新版本号，
                                    .setNewVersion(newVersion)
                                    //（必须）下载地址
                                    .setApkFileUrl(jsonObject.optString("apk_url"))
                                    .setUpdateDefDialogTitle(jsonObject.optString("title"))
                                    .setUpdateLog(jsonObject.optString("content"))
                                    .setTargetSize(jsonObject.optString("packet_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(isForceUpdate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }
                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        super.hasNewApp(updateApp, updateAppManager);
                    }
                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                        Logger.d("没有需要更新的版本...");
                        FileUtil.deleteDir(versionPath);//没有新版本时删除下载的文件
                    }
                });
    }
    @Override
    public void onBackPressed() {
        if (!x5Web.canGoBack()) {
            exitApp();// 返回前一个页面
            return;
        }else {
            x5Web.goBack();
        }
    }
    /**
     * 退出程序
     */
    private void exitApp(){
        if((System.currentTimeMillis()-firstTime) > 3000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
        {
            showToastText("再按一次退出程序");
            firstTime = System.currentTimeMillis();
        }else{
            AppManager.getInstance().AppExit(this);
        }
    }
}
