package com.cyh.sbtimedemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class IntentActivity extends AppCompatActivity {
   /**
     *@cyh   一 与其他页面的交互
     *       1  intent的发送   1.1 隐式Intent
    *        2  验证是否有app接收
    *        3  启动Activity
    *        4  显示app的选择界面
    *        二  接收Activity的返回结果
    *         1 启动
    *         2 接收
    *         三 Intent过滤
    *         1 添加Intent Filter
    *         2 Handle发送过来的Intent
    *
     *created at 2016/10/18 16:47
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentctivity);
//        一
        //1.1 指定电话
        Uri number=Uri.parse("tel:15111255155");
        Intent callIntent=new Intent(Intent.ACTION_DIAL,number);
        //1.2 查看网页
        Uri web=Uri.parse("http://www.baidu.com");
        Intent webIntent=new Intent(Intent.ACTION_VIEW,web);
        //2.1 验证
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(webIntent, 0);
        boolean isIntentSafe=resolveInfos.size()>0;
        //3 启动
        if (isIntentSafe){
            startActivity(webIntent);
        }
        //4  选择界面
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title="请选择一个应用";
        Intent chooser = Intent.createChooser(intent, title);
        startActivity(chooser);
        pickContact();
    }

    /**
     *  得到 电话号码
     */
    public static final int PICK_CONTACT_REQUEST=1;//标识码

    private void pickContact(){
        Intent pickContactIntent=new Intent(Intent.ACTION_PICK,Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent,PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PICK_CONTACT_REQUEST){
            if (requestCode==RESULT_OK){
                Uri contactUrl = data.getData();
                String [] projection={ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver().query(contactUrl, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(columnIndex);
                //go..

            }
        }
    }
}
