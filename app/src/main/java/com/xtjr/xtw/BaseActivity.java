package com.xtjr.xtw;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.base.lib.baseui.AppBaseActivity;
import butterknife.ButterKnife;
import nucleus5.presenter.RxPresenter;

/**
 * @copyright : 深圳车发发科技有限公司
 * Created by yixf on 2017/11/13.
 * @description:有标题栏的activity基类
 */

public abstract class BaseActivity<P extends RxPresenter> extends AppBaseActivity<P> {
    /**
     * 标题view
     */
    protected RelativeLayout base_title_layout;
    /**
     * 标题文本
     */
    protected TextView titleText;
    /**
     * 内容view
     */
    protected RelativeLayout contentLayout;
    /**
     * 后退view
     */
    public RelativeLayout backLayout;
    /**
     * 右边按钮
     */
    protected TextView rightButton;
    /**
     * 右边图标
     */
    protected ImageView rightImage;

    protected Bundle savedBunde;

    private ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatuBarColor(R.color.title_color);
        setContentView(R.layout.activity_base_layout);
        savedBunde = savedInstanceState;
        contentLayout = findViewById(R.id.base_content_layout);

        initTitleView();

        initView();
    }
    /**
     * 设置状态栏颜色
     * @param colorId
     */
    public void setStatuBarColor(int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(colorId));
        }
    }
    /**
     * 隐藏标题栏
     */
    protected void hideTitle(int statuBarColor) {
        setStatuBarColor(statuBarColor);
        base_title_layout.setVisibility(View.GONE);
    }
    protected Activity getActivity() {
        return this;
    }
    /**
     * 抽象方法，初始化子类View
     */
    protected abstract void initView();
    /**
     * 设置右边图标
     *
     * @param icon
     */
    public void setrightIcon(int icon) {
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setBackgroundResource(icon);
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightIconClick();
            }
        });
    }
    /**
     * 显示右侧菜单选择
     */
    public void showRightButton(String text) {
        rightButton.setText(text);
        rightButton.setVisibility(View.VISIBLE);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightButtonClick();
            }
        });
    }
    /**
     * 显示右侧菜单选择
     */
    public void showRightButton(int rightButtonId) {
        rightButton.setText(getResources().getString(rightButtonId));
        rightButton.setVisibility(View.VISIBLE);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightButtonClick();
            }
        });
    }
    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleText.setText(title);
    }
    /**
     * 设置标题以及背景色
     * @param title
     * @param titleColor
     */
    public void setTitle(String title,int titleColor){
        setTitle(title);
        base_title_layout.setBackgroundColor(getResourceColor(titleColor));
        setStatuBarColor(titleColor);
    }
    /**
     * 设置标题
     *
     * @param titleId
     */
    public void setTitle(int titleId) {
        titleText.setText(getResources().getString(titleId));
    }

    /**
     * 设置标题以及背景色
     * @param titleId
     * @param titleColor
     */
    public void setTitle(int titleId,int titleColor){
        setTitle(titleId);
        setStatuBarColor(titleColor);
        base_title_layout.setBackgroundColor(getResourceColor(titleColor));
    }

    /**
     * 显示右侧按钮
     */
    public void showRightButtonView() {
        rightButton.setVisibility(View.VISIBLE);
    }
    /**
     * 隐藏右侧按钮
     */
    public void hideRightButtonView() {
        rightButton.setVisibility(View.GONE);
    }
    /**
     * 初始化view，继承此类的都要重写此方法
     */
    protected void setViewLayout(int titleId, int layoutId) {
        if(titleId!=0){
            pageName = getString(titleId);
            titleText.setText(getResources().getString(titleId));
        }
        View view = getLayoutInflater().inflate(layoutId, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
        contentLayout.addView(view);
    }
    protected boolean isLogin;
    @Override
    public void reLogin() {
        super.reLogin();
        if(!isLogin){
            isLogin = true;
            showToastText("登录失效，请重新登录");
        }
    }

    /**
     * 初始化view，继承此类的都要重写此方法
     */
    protected void setViewLayout(int titleId, View view) {
        if(titleId!=0){
            pageName = getString(titleId);
            titleText.setText(getResources().getString(titleId));
        }
        ButterKnife.bind(this, view);
        contentLayout.addView(view);
    }
    /**
     * 初始化view，继承此类的都要重写此方法
     */
    protected void setViewLayout(int layoutId) {
        View view = getLayoutInflater().inflate(layoutId, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
        contentLayout.addView(view);
    }
    /**
     * 初始化标题栏控件
     */
    private void initTitleView() {
        base_title_layout =  findViewById(R.id.base_title_layout);
        backLayout =  findViewById(R.id.base_back_layout);
        titleText =  findViewById(R.id.title_text);
        rightButton =  findViewById(R.id.tool_bar_right_btn);
        rightImage = findViewById(R.id.tool_bar_right_img);
        /**
         * 后退事件
         */
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSelf();
            }
        });
    }
    protected void rightButtonClick() {

    }
    protected void rightIconClick() {

    }
}
