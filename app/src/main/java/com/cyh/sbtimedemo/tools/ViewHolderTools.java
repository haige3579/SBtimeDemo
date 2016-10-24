package com.cyh.sbtimedemo.tools;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 改善ViewHolder写法
 * 1  常用的写法
 * 2  优化的写法
 * Created by CYH on 2016/10/24.
 */
public class ViewHolderTools {
//    1 常用写法
    //1.1 内部类
    private  class ViewHolder1{
         TextView tvXXX;
    }
    //1.2 使用内部类
    /**
        ViewHolder holder=null;
        if (convertView==null){
             convertView=mInflater.inflate(R.layout.xxx,null);
             holder=new ViewHolder();
             holder.txXXX=(TextView)findViewById（R.id.xxx）；
            convertView.setTag(holder);
    } else{
         holder=(ViewHolder)convertView.getTag();
     }
     */

//    优化的写法
    //1.1 ViewHolder类
    public static class ViewHolder2{
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view,int id){
            SparseArray<View> viewHolder= (SparseArray<View>) view.getTag();
            if(viewHolder==null){
                viewHolder=new SparseArray<>();
                view.setTag(viewHolder);
            }
            View childView=viewHolder.get(id);
            if (childView==null){
                childView=view.findViewById(id);
                viewHolder.put(id,childView);
            }
            return (T) childView;
        }
    }

    //使用
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView==null){
//            convertView= LayoutInflater.from(context).inflate(R.layout.activity_audio,parent,false);
        }
//       ImageView bananaView= ViewHolder2.get(convertView,R.id.banana);
//       TextView phoneView= ViewHolder2.get(convertView,R.id.phone);
//       BananaPhone banana=getItem(position);
//       phoneView.setText(banana.getPhone());
//        bananaView.setImageResource(banana.getBanana());
            return convertView;
        }
    }
