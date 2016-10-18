package com.cyh.sbtimedemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * /保存到sp文件
         */
       //新建
        SharedPreferences shopping = getSharedPreferences("shopping", 1);
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        //写
        SharedPreferences.Editor editor=shopping.edit();
        editor.putInt("You only live once,but if you do it right,once is enough",2);
        editor.commit();
        //读
//        int v1=getResources().getInteger(1);

        /**
         * 存储 file
         *  1 内部internal storage
         *  2 外部External storage
         *  3 查询空间
         *  4 删除
         */

        String  filename="ggg";
        String str="hello world";
        FileOutputStream fos;
        //1.1file 对象
        File file=new File(getFilesDir(),filename);
       //1.2 写
        try {
            //3 查询空间
            long freeSpace = file.getFreeSpace();
            long totalSpace = file.getTotalSpace();
            fos=openFileOutput(filename,Context.MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4.1 删除
        file.delete();

    }

    // 1.3 缓存文件
    public File getTempFile(Context context,String url){
        File file=null;
        String filename;
        try {
         filename= Uri.parse(url).getLastPathSegment();
        file=File.createTempFile(filename,null,context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file;
        //4.2 删除
//        context.deleteFile(filename);
    }

    //2.1  判断External状态  可写--可读
    public boolean iskExternalStorageWritable(){
        String state= Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return  true;
        }
        return  false;
    }
    public boolean iskExternalStorageReadable(){
        String state=Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
       return  false;
    }
    //2.2 保存文件的类型  public file(公开、卸载保留) 和 private file（私有、卸载删除）
    public File getAlbumStorageDir(String albumName){
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),albumName);
        if (!file.mkdirs()){

        }
        return file;
    }
    public File getAlbumStorageDir(Context context,String albumName){
        File file=new File((context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)),albumName);
        if (!file.mkdirs()){

        }
        return file;
    }
    
    /**
      *@cyh  保存数据库
      *     1  定义表名 列名
     *      2  使用SQL helper创建DB
     *      3  添加信息到DB               ContentValues
     *created at 2016/10/18 14:57
      */
    // 1  表名、列名
    public static abstract class FeedEntry implements BaseColumns{
           public static final String TABLE_NAME="entry";
           public static final String Column_NAME_ENTRYID="entryid";
           public static final String Column_NAME_TITLE="title";
           public static final String Column_NAME_SUBTITLE="subtitle";
    }
    //2 创建db
    public class FeedReaderDBHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION=1;
        public static final String DATABASE_NAME="FeedReader.db";
        public FeedReaderDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("");
          onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
        }
    }


}
