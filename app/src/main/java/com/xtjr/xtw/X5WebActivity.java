package com.xtjr.xtw;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import com.base.lib.util.ToastUtil;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.File;
import butterknife.BindView;

/**
 * @copyright : 深圳车发发科技有限公司
 * Created by yixf on 2017/11/14.
 * @description:基础网页类
 */
public class X5WebActivity extends BaseActivity {

    @BindView(R.id.x5_web_view)
    X5WebView x5Web;

    public String web_url;

    private ValueCallback<Uri> mFilePathCallback;

    private ValueCallback<Uri[]> mFilePathCallbackArray;

    private boolean mIsPageLoading;

    @Override
    protected void initView() {
        setViewLayout(R.layout.activity_x5_web_layout);
        hideTitle(R.color.transparent);
        initAgentWeb();
    }
    private void initAgentWeb(){
        x5Web.setWebViewClient(new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Logger.d("the url is "+url);
                //判断重定向的方式二
                if(mIsPageLoading) {
                    return false;
                }
                if (url.startsWith("http") || url.startsWith("https")) { //http和https协议开头的执行正常的流程
                    if(url.endsWith("pdf")||url.endsWith("mp4")){//文件 调用系统浏览器
                        ActivityUtil.callSystem(X5WebActivity.this,url);
                    }else{
                        view.loadUrl(url);
                    }
                }else {
                    if(ActivityUtil.isQQClientAvailable(X5WebActivity.this)){
                        ActivityUtil.callSystem(X5WebActivity.this,url);
                    }else{
                        ToastUtil.showToast(X5WebActivity.this,"您没有安装QQ");
                    }
                }
                return false;
            }
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                mIsPageLoading = true;
            }
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mIsPageLoading = false;
                //js判断是否原生app
                x5Web.loadUrl("javascript:androidCall()");
            }
        });
        x5Web.setWebChromeClient(new WebChromeClient() {
            // Andorid 4.1----4.4
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                mFilePathCallback = uploadFile;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectAlbum();
                    }
                });
            }
            // For Android >= 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallbackArray != null) {
                    mFilePathCallbackArray.onReceiveValue(null);
                }
                mFilePathCallbackArray = filePathCallback;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectAlbum();
                    }
                });
                return true;
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                titleText.setText(title);
                stopProgressDlg();
            }
        });
        x5Web.loadUrl(web_url);
    }
    @Override
    public void onBackPressed() {
        if (!x5Web.canGoBack()) {
            finishSelf();// 返回前一个页面
            return;
        }else {
            x5Web.goBack();
        }
    }
    public static final int REQUEST_ALBUM = 1;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private void selectAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(albumIntent, REQUEST_ALBUM);
    }
    /**
     * 初始化返回按钮，如果网页可以后退这后退，没有则关闭页面
     */
    private void initBackLayout(){
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!x5Web.canGoBack()) {  //表示按返回键  时的操作
                    finishSelf();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ALBUM:
                    Uri uri = data.getData();
                    if (uri != null) {
                        handleCallback(uri);
                    }else{
                        // 取消了照片选取的时候调用
                        handleCallback(null);
                    }
                    break;
            }
        }else{
            // 取消了照片选取的时候调用
            handleCallback(null);
        }
    }
    /**
     * 处理WebView的回调
     *
     * @param uri
     */
    private void handleCallback(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFilePathCallbackArray != null) {
                if (uri != null) {
                    mFilePathCallbackArray.onReceiveValue(new Uri[]{uri});
                } else {
                    mFilePathCallbackArray.onReceiveValue(null);
                }
                mFilePathCallbackArray = null;
            }
        } else {
            if (mFilePathCallback != null) {
                if (uri != null) {
                    String url = getFilePathFromContentUri(uri, getContentResolver());
                    Uri u = Uri.fromFile(new File(url));

                    mFilePathCallback.onReceiveValue(u);
                } else {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = null;
            }
        }
    }
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
    @Override
    protected void onPause() {
        x5Web.onPause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        x5Web.onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        x5Web.destroy();
        super.onDestroy();
    }
}
