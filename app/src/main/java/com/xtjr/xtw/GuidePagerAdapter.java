package com.xtjr.xtw;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.lib.glide.GlideApp;

/**
 * @copyright : 深圳市喜投金融服务有限公司
 * Created by yixf on 2018/7/19
 * @description:引导页适配器
 */
public class GuidePagerAdapter extends PagerAdapter {

    private Context mContext;

    private int[] resIds;

    public GuidePagerAdapter(Context context, int[] resIds) {
        mContext = context;
        this.resIds = resIds;
    }
    @Override public int getCount() {
        return resIds.length;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
        ImageView pointImage = new ImageView(mContext);
        pointImage.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        pointImage.setScaleType(ImageView.ScaleType.CENTER);
        GlideApp.with(mContext).load(resIds[position]).into(pointImage);
        view.addView(pointImage, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return pointImage;
    }
}