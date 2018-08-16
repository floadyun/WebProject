package com.xtjr.xtw;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.base.lib.baseui.AppBaseActivity;

import java.util.List;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/27
 * @description:
 */
public class ActivityUtil {

     /** 拨打电话，统一客服电话
     * @param context
     */
    public static void callPhone(final AppBaseActivity context){
        String[] permissions = {Manifest.permission.CALL_PHONE};
        context.checkPermissions(permissions, 0, new AppBaseActivity.PermissionsResultListener() {
            @Override
            public void onSuccessful(int[] results) {
                startCallPhone(context, "400-788-9566");
            }
            @Override
            public void onFailure() {

            }
        });
    }
    public static void startCallPhone(final AppBaseActivity context, final String phone){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            context.startActivity(intent);
        }catch (Exception e){

        }
    }

    /**
     * 根据url调用相应的系统功能
     * @param url
     */
    public static void callSystem(Context context,String url){
        try{
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(in);
        }catch (Exception e){

        }
    }
    /**
     * 判断qq是否可用
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
