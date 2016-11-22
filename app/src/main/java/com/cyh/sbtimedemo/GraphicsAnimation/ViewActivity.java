package com.cyh.sbtimedemo.GraphicsAnimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cyh.sbtimedemo.R;

public class ViewActivity extends AppCompatActivity {
   /**
     *@cyh    添加动画
    *         a  View间渐变
    *         b  ViewPage实现屏幕滑动
     *
     *created at 2016/11/16 17:11
     */
    private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mContentView = findViewById(R.id.content);
        mLoadingView = findViewById(R.id.loading_spinner);

        mContentView.setVisibility(View.GONE);
        mShortAnimationDuration=getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    private void crossfade(){
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);
        mContentView.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);
        mLoadingView.animate().alpha(0f).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingView.setVisibility(View.GONE);
            }
        });

    }
   }
