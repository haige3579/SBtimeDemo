package com.cyh.sbtimedemo.GraphicsAnimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cyh.sbtimedemo.R;

public class BitmapActivity extends AppCompatActivity {
 /**
   *@cyh    高效显示BitMap
  *           a  高效加载大图 --缩小图片
   *             1 读取位图的尺寸和类型  --BitmapFactory
   *created at 2016/10/26 17:07
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
    }
}
