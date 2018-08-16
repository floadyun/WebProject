package com.xtjr.xtw;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.base.lib.pageindicator.indicator.CircleIndicator;
import com.base.lib.update.utils.AppUpdateUtils;
import com.base.lib.util.PreferenceUtil;
import com.base.lib.util.TimerThread;
import butterknife.BindView;

/**
 * @copyright : 深圳车发发科技有限公司
 * Created by yixf on 2017/11/13.
 * @description:APP引导页，首次启动或这更新时显示
 */
@Route(path = RouterManager.GUIDE_ACTIVITY)
public class GuideAcitivity extends BaseActivity implements ViewPager.OnPageChangeListener,TimerThread.OnTimerListerner{

    @BindView(R.id.guide_view_pager)
    ViewPager guidePager;

    @BindView(R.id.guide_point_indicator)
    CircleIndicator circleIndicator;//放置圆点

    //最后一页的按钮
    @BindView(R.id.guide_start_btn)
    ImageButton startBtn;
    //图片资源id
    private int[] resIds = new int[]{
            R.drawable.guide_page_1,R.drawable.guide_page_2,R.drawable.guide_page_3
    };
    private boolean isGo;

    private int pageIndex;

    private TimerThread timerThread;

    @Override
    protected void initView() {
        setViewLayout(R.layout.activity_guide_layout);

        hideTitle(R.color.white);
        //加载ViewPager
        initViewPager();

        timerThread =  new TimerThread(Integer.MAX_VALUE,3000);
        timerThread.setTimerListerner(this);
        new Thread(timerThread).start();
    }
    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        guidePager.setAdapter(new GuidePagerAdapter(this,resIds));
        circleIndicator.setViewPager(guidePager);
        circleIndicator.setVisibility(View.INVISIBLE);
        guidePager.setOnPageChangeListener(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    /**
     * 滑动后的监听
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        pageIndex = position;
        //判断是否是最后一页，若是则显示按钮
        if (position == resIds.length - 1){
            startBtn.setVisibility(View.VISIBLE);
        }else {
            startBtn.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onTimeProgress(int time) {
        if(pageIndex<=resIds.length-1){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    guidePager.setCurrentItem(pageIndex+1,true);
                }
            });
        }
    }
    @Override
    public void onTimeFinish() {

    }
    @Override
    public void onBackPressed() {
        finish();
    }
    /**
     * 跳转至登录页面
     * @param view
     */
    public void goLogin(View view){
        gotoHome();
    }
    /**
     * 跳转至首页
     */
    private void gotoHome(){
        if(!isGo){
            isGo = true;
            PreferenceUtil.getPreference(this).setBoolPreference(Consts.IS_FIRST,false);
            PreferenceUtil.getPreference(this).setStringPreference(Consts.VERSION_NAME, AppUpdateUtils.getVersionName(this));
            RouterManager.startNormalActivity(compat, RouterManager.MAIN_ACTIVITY);
            this.finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerThread.stop();
    }
}
