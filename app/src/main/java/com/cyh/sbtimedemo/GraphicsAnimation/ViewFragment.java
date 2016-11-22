package com.cyh.sbtimedemo.GraphicsAnimation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyh.sbtimedemo.R;

public class ViewFragment extends Fragment {
  //  使用viewpager实现屏幕滑动
    private static final int NUM_PAGES=5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    //创建Fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.activity_view_fragment,container,false);
        return rootView;
    }
    // 添加ViewPager

}
