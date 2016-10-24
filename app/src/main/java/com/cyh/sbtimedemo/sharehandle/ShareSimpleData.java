package com.cyh.sbtimedemo.sharehandle;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.cyh.sbtimedemo.R;

import java.util.ArrayList;

/**
 * a：给其他app发送简单数据
 * 1  分享Text Content
 * 2  分享二进制内容 Binary Content
 * 3  分享多块内容 Multiple Pieces of Content
 * b: 接收其他app数据
 * 1 更新manifest文件
 * 2 处理接收到的数据  Handle the Incoming Content
 * c: 添加一个简便的分享功能
 * 1 菜单声明 -- menu res
 * 2 设置 share  intent
 * <p>
 * * Created by CYH on 2016/10/19.
 */
public class ShareSimpleData extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layou);
        // a.1分享文章或网址给好友
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share text to"));

        // a.2 Binary Content --分享图片
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, "********");
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "*****--*****"));

        // a.3 Multiple
        ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(Uri.parse("www.baidu.com"));
        imageUris.add(Uri.parse("www.so.com"));
        Intent fenintent = new Intent();
        fenintent.setAction(Intent.ACTION_SEND_MULTIPLE);
        fenintent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        fenintent.setType("image/*");
        startActivity(Intent.createChooser(fenintent, "share image to ..."));

        /**
         *  b  handle
         */
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                hanleSendText(intent);
            } else if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handSendMultipleImages(intent);
            }
        } else {

        }
    }

    private void handSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUri = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {

        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {

        }
    }

    private void hanleSendText(Intent intent) {
        String stringExtra = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (stringExtra != null) {

        }
    }

    /**
     * c  menu菜单
     */
    private ShareActionProvider mSAP;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mSAP = (ShareActionProvider) item.getActionProvider();
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mSAP != null) {
            mSAP.setShareIntent(shareIntent);
        }
    }

    /**
     * 读取短信
     */
    private void getMsgs() {
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = getContentResolver();
        //读取列的信息
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        while (cursor.moveToFirst()) {
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            System.out.println("address:" + address + ",date:" + date + ",type:" + type + ",body" + body);
        }
        cursor.close();
    }

    /**
     * 加入一条短信
     */
    private void insertMsg(){
        ContentResolver resolver = getContentResolver();
        //新建
        ContentValues cv=new ContentValues();
        cv.put("address","15111255155");
        cv.put("date",System.currentTimeMillis());
        cv.put("type","1");
        cv.put("body","来杯清香的早茶，迎接崭新的一天");
        resolver.insert(Uri.parse("content://sms/"),cv);
    }

    /**
     * 读取手机联系人
     */
    private void getContacts(){
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while (cursor.moveToFirst()){
            String cName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String cNumb=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println("名字："+cName+",手机号："+cNumb);
        }
        cursor.close();
    }

    /**
     *  查询指定电话的联系人
     */
    private void queryContact(String number){
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts.data/phone/filter/" + number);
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
        while (cursor.moveToFirst()){
            String name=cursor.getString(0);
        }
        cursor.close();
    }

    /**
     * 添加联系人
     */
    private void insertContact() throws RemoteException, OperationApplicationException {
        //事物 添加
        Uri uri = Uri.parse("content://com.android.contact/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contact/data");
        ContentResolver resolver = getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri).withValue("accout_name", null).build();
        operations.add(op1);

        ContentProviderOperation op2 = ContentProviderOperation.newInsert(uriData).withValueBackReference("raw_contact_id", 0).
                withValue("mimetype", "vnd.andoird.cursor.item/name")
                .withValue("data1","15111255155")
                .withValue("data2", "cyh")
                .build();
        operations.add(op2);

        resolver.applyBatch("com.android.contacts",operations);

    }

}
