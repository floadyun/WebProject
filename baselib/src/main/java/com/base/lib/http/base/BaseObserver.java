package com.base.lib.http.base;

import android.content.Context;
import android.text.TextUtils;

import com.base.lib.baseui.AppBaseActivity;
import com.base.lib.http.ApiHelper;
import com.base.lib.http.network.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by yixiaofei on 2017/3/9 0009.
 */
public abstract class BaseObserver<T> implements Observer<T> {
    //请求成功回调
    public abstract void onSuccess(T value);
    //请求失败回调
//    public abstract void onFailured(String e);

    private AppBaseActivity baseActivity;

    public BaseObserver(Context context) {
        this.baseActivity = (AppBaseActivity) context;
    }
    /**
     * 如不需要显示loading 重载此方法
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        try {
            if(!NetworkUtils.isConnected(baseActivity)){
                if(baseActivity!=null){
                    baseActivity.showToastText("请检检查网络连接");
                }
                return;
            }
            if(baseActivity!=null){
                baseActivity.showProgressDlg();
            }
        }catch (Exception exception){

        }
    }
    @Override
    public void onNext(T value) {
        if(baseActivity!=null){
            baseActivity.stopProgressDlg();
        }
        if(value!=null){
            onSuccess(value);
        }
    }
    @Override
    public void onError(Throwable e) {
        if(baseActivity!=null){
            baseActivity.stopProgressDlg();
        }
        try {
            if(e instanceof HttpException){//其他状态异常 输出
                ResponseBody body = ((HttpException) e).response().errorBody();
                Logger.e("错误码:"+((HttpException) e).code());
                String errorStr = "";
                try {
                    errorStr = body.string();
                    Logger.e(errorStr);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    if(baseActivity!=null){
                        baseActivity.showToastText(exception.toString());
                    }
                }
                Gson gson = new Gson();
                switch (((HttpException) e).code()){
                    case ApiHelper.HTTP_401://登录失效，重新登录
                        if(baseActivity!=null){
                            baseActivity.reLogin();
                        }
                        break;
                    case ApiHelper.HTTP_423:
                        BaseEntity baseEntity = gson.fromJson(errorStr,BaseEntity.class);
                        if(baseEntity!=null){
                            if(baseActivity!=null){
                                baseActivity.showToastText(baseEntity.message.msg);
                            }
                        }
                        break;
                    case ApiHelper.HTTP_422:
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(errorStr);
                            JSONObject errorObject = jsonObject.getJSONObject("errors");
                            //通过迭代器获取这段json当中所有的key值
                            Iterator keys = errorObject.keys();
                            if(!keys.hasNext())return;
                            String key = String.valueOf(keys.next());
                            String value = errorObject.optString(key);
                            Logger.e("the value is "+value);
                            if(!TextUtils.isEmpty(value)){
                                if(baseActivity!=null){
                                    baseActivity.showToastText(value);
                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        break;
                }
            }
        }catch (Exception exception){

        }
    }
    @Override
    public void onComplete() {
        if(baseActivity!=null){
            baseActivity.stopProgressDlg();
        }
    }
}
