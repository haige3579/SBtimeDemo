package com.cyh.sbtimedemo.sharehandle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cyh.sbtimedemo.R;
 /**
   *@cyh    分享文件 --向接收文件的应用程序发送这个文件的content URI，对该URI授予临时访问权限
   *         FileProvide (V4 Support)  ： getUriForFile()
  *
   *created at 2016/10/19 14:49
   */

public class ShareFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
    }
}
