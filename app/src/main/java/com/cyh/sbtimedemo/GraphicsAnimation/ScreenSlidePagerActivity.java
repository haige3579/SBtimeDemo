package com.cyh.sbtimedemo.GraphicsAnimation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cyh.sbtimedemo.R;

/**
 *     滑动动画   --  左右抵消默认动画
 * Created by CYH on 2016/11/22.
 */
public class ScreenSlidePagerActivity extends FragmentActivity {
    private static final int NUM_PAGES=5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager= (ViewPager) findViewById(R.id.pager);
        mPagerAdapter=new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem()==0){
            super.onBackPressed();
        }else {
            mPager.setCurrentItem(mPager.getCurrentItem()-1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
        public ScreenSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return new ViewFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    //  滑动使页面收缩并褪色
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer{
        private static final float MIN_SCALE=0.85f;
        private static final float MIN_ALPHA=0.5f;

        @Override
        public void transformPage(View page, float position) {
            int pageWidht=page.getWidth();
            int pageHeight=page.getHeight();
            //屏幕左边
            if (position<-1){
                page.setAlpha(0);
            }else if (position<=1){
                float scaleFactor=Math.max(MIN_SCALE,1-Math.abs(position));
                float vertMargin=pageHeight*(1-scaleFactor)/2;
                float horzMargin=pageHeight*(1-scaleFactor)/2;
                if (position<0){
                   page.setTranslationX(horzMargin-vertMargin/2);
                }else {
                    page.setTranslationX(-horzMargin+vertMargin/2);
                }
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setAlpha(MIN_ALPHA+(scaleFactor-MIN_SCALE)/(1-MIN_SCALE)*(1-MIN_ALPHA));
            }else {
                page.setAlpha(0);
            }
        }
    }

    // 滑动 ，抵消默认滑动动画
    public class DepthPageTransformer implements ViewPager.PageTransformer{
       private static final float MIN_SCALE=0.75f;
        @Override
        public void transformPage(View page, float position) {
            int pageWidth=page.getWidth();
            if (position<-1){
                page.setAlpha(0);
            }else if(position<=0){
                page.setAlpha(1);
                page.setTranslationX(0);
                page.setScaleX(1);
                page.setY(1);
            }else if (position<=1){
                page.setAlpha(1-position);
                page.setTranslationX(pageWidth*-position);
                float scaleFactor=MIN_SCALE+(1-MIN_SCALE)*(1-Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            }else {
                page.setAlpha(0);
            }

        }
    }
}
