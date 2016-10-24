package com.cyh.lockpatternview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * 图形锁
 *
 * Created by CYH on 2016/10/24.
 */
public class LockPatternView extends View {
    //9个点
    private Point[][] points=new Point[3][3];
    boolean isInit;
    int width ,height;
    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit){
            initPoints();
        }
        points2Canvas(canvas);
    }

    private void initPoints() {
         width = getWidth();
         height = getHeight();
//        横屏
        if (width>height){

        }else {
//        竖屏
        }
    }

    private void points2Canvas(Canvas canvas) {

    }
}
