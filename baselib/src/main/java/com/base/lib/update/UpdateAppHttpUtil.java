package com.base.lib.update;

import android.support.annotation.NonNull;
import com.base.lib.http.okhttputils.OkHttpUtils;
import com.base.lib.http.okhttputils.callback.FileCallBack;
import com.base.lib.http.okhttputils.callback.StringCallback;
import java.io.File;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/11
 * @description:
 */
public class UpdateAppHttpUtil implements HttpManager {

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final HttpManager.Callback callBack) {
        OkHttpUtils.get()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e.toString());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response);
                    }
                });
    }
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e.toString());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response);
                    }
                });
    }
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e.toString());
                    }
                    @Override
                    public void onResponse(File response, int id) {
                        callback.onResponse(response);

                    }
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onBefore();
                    }
                });
    }
}
