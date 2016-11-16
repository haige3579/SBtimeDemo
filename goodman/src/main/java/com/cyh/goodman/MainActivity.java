package com.cyh.goodman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List <String> mlist=new ArrayList<>();
    ListView mlistView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlistView= (ListView) findViewById(R.id.mlistview);
        for (int i=0;i<10;i++)
        mlist.add("刘德华第"+i+"来深圳");
        mlistView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mlist.size();
            }

            @Override
            public Object getItem(int position) {
                return mlist.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv=new TextView(MainActivity.this);
                tv.setText(mlist.get(position));
                return tv;
            }
        });
       mlistView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
           @Override
           public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
               menu.setHeaderTitle("刘德华");
               menu.add(0,0,0,"冰雨");
               menu.add(0,1,0,"笨小孩");
               menu.add(0,2,0,"天意");
           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(MainActivity.this,"you are bitch",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(MainActivity.this,"好小子",Toast.LENGTH_SHORT).show();
                 break;
            case 2:
                Toast.makeText(MainActivity.this,"棒棒哒",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(MainActivity.this, ""+"删除菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view:
                Toast.makeText(MainActivity.this, ""+"查看菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newinfo:
                Toast.makeText(MainActivity.this, ""+"新建菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.viewinfo:
                Toast.makeText(MainActivity.this, ""+"信息菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info:
                Toast.makeText(MainActivity.this, ""+"详情菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newFile:
                Toast.makeText(MainActivity.this, ""+"新建文件菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newDoc:
                Toast.makeText(MainActivity.this, ""+"新建文档菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.newList:
                Toast.makeText(MainActivity.this, ""+"新建列表菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteone:
                Toast.makeText(MainActivity.this, ""+"删除详情菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deletelist:
                Toast.makeText(MainActivity.this, ""+"删除列表菜单", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
