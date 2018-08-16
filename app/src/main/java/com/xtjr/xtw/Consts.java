package com.xtjr.xtw;

import android.os.Environment;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/11
 * @description:静态变量
 */
public class Consts {
    //sherfrences参数
    public static final String IS_FIRST = "is_first";
    public static final String VERSION_NAME = "version_name";
    //缓存文件路径
    private static final String CATCHE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xtjr";
    //缓存文件路径
    public static final String APK_FILE_PATH = CATCHE_FILE_PATH+"/version";
    //图片缓存路径
    public static final String IMAGE_FILE_PATH = CATCHE_FILE_PATH+"/image";
}
