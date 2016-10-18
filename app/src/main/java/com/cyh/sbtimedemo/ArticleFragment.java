package com.cyh.sbtimedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 1 基础Fragment ，重写onCreateView回调方法
 * 2 添加到activity
 * Created by CYH on 2016/10/17.
 */
public class ArticleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.article_view,container,false);
    }
}
