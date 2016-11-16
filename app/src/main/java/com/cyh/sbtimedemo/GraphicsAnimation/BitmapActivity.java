package com.cyh.sbtimedemo.GraphicsAnimation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.cyh.sbtimedemo.R;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Set;

public class BitmapActivity extends AppCompatActivity {
 /**
   *@cyh    高效显示BitMap
  *           a  高效加载大图 --缩小图片  --UI线程处理
   *             1 读取位图的尺寸和类型  --BitmapFactory
  *              2 加载一个按比例缩小的版本到内存中
  *           b  非UI线程处理Bitmap   --AsyncTask 后台处理
  *              3 使用AsyncTask
  *              4 处理并发问题
  *           c  缓存Bitmap   -加载多张Bitmap
  *              5  使用内存缓存     --LruCache类  LinkedHashMap
  *              6  使用磁盘缓存    -- DiskLruCache   (4.0才支持)
  *              7  处理配置改变   --如屏幕方向改变     使用fragment保留LruCache
  *          d  管理Bitmap的内存使用
  *              8 android 2.3.3以下 内存使用  -- recycle() 引用计数的方法来追踪Bitmap目前是否被显示或在缓存中
  *              9 android 3.0以上 管理内存  --BitmapFactory.Options.inBitmap
  *          e  在UI上显示Bitmap
  *             10 实现加载图片到ViewPager  ---Swipe View Pattern  FragmentStatePagerAdapter
   *created at 2016/10/26 17:07
   */
    ImageView mImageView;

    //5 、7
      private LruCache<String,Bitmap> mMemoryCache;
    // 6
    // 8 引用计数为0
    private int mCacheRefCount=0;
    private int mDisplayRefCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        mImageView=(ImageView) findViewById(R.id.imageView);

        // 1 读取尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(getResources(),R.id.imageView,options);
        int imageHeight = options.outHeight;
        int imageWidth=options.outWidth;
        String imageType = options.outMimeType;
        // 2  加载到内存
       mImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.id.imageView,100,100));
        //3
        new BitmapWorkerTask(mImageView);
       loadBitmap(1,mImageView);

        // 5 建立LruCache示例
           // 5.1  得到可用虚拟内存
         final int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
          // 5.2   使用8/1 内存
         final int cacheSize = maxMemory / 8;
         mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
             @Override
             protected int sizeOf(String key, Bitmap value) {
                 return value.getByteCount()/1024;
             }
         };
        //7.1
//        RetainF

    }

    // 9.1 保存bitmap 供以后使用
    Set<SoftReference<Bitmap>> mReusableBitmaps;

    //8.1 显示计数
    public void setIsDisplay(boolean isDisplay){
        synchronized (this){
            if (isDisplay){
                mDisplayRefCount++;
//                mHasBeenDisplayed=true;
            }else {
                mDisplayRefCount--;
            }
        }
        chekState();
    }
    //8.2
    public void setIsCached(boolean isCached){
        synchronized (this){
            if (isCached){
                mCacheRefCount++;
            }else {
                mCacheRefCount--;
            }
        }
        chekState();
    }
    //8.3  为0，回收内存
    private synchronized void chekState(){
//        if (mCacheRefCount<=0&&mDisplayRefCount<==0&&mHasBeenDisplayed&&hasValidBitmap()){
//            getBitmap().recycle();
//        }
    }
    //8.4
    private synchronized boolean hasValidBitmap(){
//        Bitmap bitmap=getBitmap();
//        return bitmap!=null&&!bitmap.isRecycled();
        return  true;
    }

//    //7.2
//      class RetainFragment extends Fragment {
//        private static final String TAG = "RetainFragment";
//        public LruCache<String, Bitmap> mRetainedCache;
//
//        public RetainFragment() {
//        }
//
//        public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
//            RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
//            if (fragment == null) {
//                fragment = new RetainFragment();
//                fm.beginTransaction().add(fragment, TAG).commit();
//            }
//            return fragment;
//        }
//    }
          //5.3
      public void addBitmapToMemoryCache(String key,Bitmap bitmap){
          if (getBitmapFromMemCache(key)==null){
              mMemoryCache.put(key,bitmap);
          }
      }
        //5.4
      public Bitmap getBitmapFromMemCache(String key){
          return mMemoryCache.get(key);
      }
      //5.5   BitmapWorkerTask 需要把解析好的Bitmap添加到内存缓存中
      public void loadBitmap(int resId,ImageView imageView){
          final String imageKey = String.valueOf(resId);
          final Bitmap bitmap=getBitmapFromMemCache(imageKey);
          if (bitmap!=null){
              mImageView.setImageBitmap(bitmap);
          }else {
              mImageView.setImageResource(R.drawable.ggggg);
              BitmapWorkerTask task=new BitmapWorkerTask(mImageView);
              task.execute(resId);
          }
      }


       //2.1 计算缩放比例 inSampleSize
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight){
        //原始的尺寸
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize=1;
        if (height>reqHeight || width>reqWidth){
            final int halfHeight=height/2;
            final int halfWidth=width/2;
            while ((halfHeight/inSampleSize)>reqHeight&&(halfWidth/inSampleSize)>reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize ;
    }
    // 2.2 解码
    public static Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
       final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);
    }


//    3 AsyncTask
class BitmapWorkerTask extends AsyncTask{
    private final WeakReference imageViewReference;
    private int data=0;

    BitmapWorkerTask(ImageView imageView) {
        imageViewReference = new WeakReference(imageView);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        data=(int)params[0];
        final Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), data, 100, 100);
        addBitmapToMemoryCache(String.valueOf(params[0]),bitmap);
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
//       更新
        if (imageViewReference!=null&&bitmap!=null){
            final  ImageView imageView=(ImageView) imageViewReference.get();
            if (imageView!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
//        更新 4.0
        if (isCancelled()){
            bitmap=null;
        }
        if (imageViewReference!=null&&bitmap!=null){
            final ImageView imageView= (ImageView) imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask=new AsyncDrawable().getBitmapWorkerTask(imageView);
            if (this==bitmapWorkerTask&&imageView!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    //3.1 计算缩放比例 inSampleSize
    public  int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight){
        //原始的尺寸
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize=1;
        if (height>reqHeight || width>reqWidth){
            final int halfHeight=height/2;
            final int halfWidth=width/2;
            while ((halfHeight/inSampleSize)>reqHeight&&(halfWidth/inSampleSize)>reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize ;
    }
    // 3.2 解码
    public  Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);
    }
}

//    public void loadBitmap(int resId,ImageView imageView){
//        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
//        task.execute(resId);
//    }

    // 4 并发处理
    class AsyncDrawable extends BitmapDrawable{
        private  WeakReference bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res,Bitmap bitmap,BitmapWorkerTask bitmapWorkerTask) {
            super(res,bitmap);
            bitmapWorkerTaskReference=new WeakReference(bitmapWorkerTask);
        }
        public AsyncDrawable(){

        }
        public BitmapWorkerTask getBitmapWorkerTask(){
            return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
        }
        // 将task绑定到ImageView
        public void loadBitmap(int resId,ImageView imageView){
            if (cancelPotentialWork(resId,imageView)){
                 BitmapWorkerTask task =new BitmapWorkerTask(imageView);
//                new AsyncDrawable(Re)
            }
        }

        public  boolean cancelPotentialWork(int data,ImageView imageView){
            final BitmapWorkerTask bitmapWorkerTask=getBitmapWorkerTask(imageView);
            if (bitmapWorkerTask!=null){
                final  int bitmapData = bitmapWorkerTask.data;
                if (bitmapData==0||bitmapData!=data){
                    bitmapWorkerTask.cancel(true);
                }else {
                    return  false;
                }
            }
            return true;
        }

        private  BitmapWorkerTask getBitmapWorkerTask(ImageView imageView){
            if (imageView!=null){
                final Drawable drawable = imageView.getDrawable();
                if (drawable instanceof AsyncDrawable){
                    final AsyncDrawable asyncDrawable= (AsyncDrawable) drawable;
                    return asyncDrawable.getBitmapWorkerTask();
                }
            }
            return null;
        }
    }


}
