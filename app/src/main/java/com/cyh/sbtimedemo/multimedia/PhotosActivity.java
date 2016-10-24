package com.cyh.sbtimedemo.multimedia;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.cyh.sbtimedemo.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhotosActivity extends AppCompatActivity {
  /**
    *@cyh  a  轻松拍摄照片
    *          1 请求使用相机权限  --清单<uses-feature>
   *           2 使用相机拍照
   *           3 获取缩略图
   *           4 保存全尺寸照片
   *           5 将照片添加到相册中
   *           6 解码一幅缩放图片
   *        b  轻松录制视频
   *           7  请求相机权限
   *           8  使用相机录制视频
   *           9   查看视频
   *        c  控制相机
   *           10 打开相机对象
   *           11  创建相机预览界面   --Preview
    *created at 2016/10/24 14:49
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //判断设备是否有照相机
        PackageManager packageManager = getPackageManager();
        boolean b = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);

    }
//    2 捕获照片
    static final int REQUEST_IMAGE_CAPTURE=1;
    private void dispatchTakePictureIntent1(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //检查处理该Intent的Activity
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

//    3 获取图像 ，显示在ImageView
//    8 查看视频
     ImageView mv;
     VideoView vv;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap= (Bitmap) extras.get("data");
            mv.setImageBitmap(imageBitmap);
        }else if (requestCode==REQUEST_VIDEO_CAPTURE&&requestCode==RESULT_OK){
            Uri videoUri = data.getData();
            vv.setVideoURI(videoUri);
        }
    }

//    4 保存全尺寸图片
    // 日期时间做照片的文件名
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath="file:"+image.getAbsolutePath();
        return  image;
    }
    // 给取名的照片创建文件对象
     static final int REQUEST_TAKE_PHOTO=1;
    private void dispatchTakePictureIntent2(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            try {
                photoFile = createImageFile();
            }catch (IOException ex){

            }
            if (photoFile!=null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
            }
        }
    }

//    5 添加到相册中
    private void gallaryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

//    6 解码 缩放图片
    ImageView mImageView;
    private void setPic(){
        //视图的尺寸
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        //位图的尺寸
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        //确定多少缩减图像
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        //解码图像文件到一个位图的大小来填充视图
        bmOptions.inJustDecodeBounds=false;
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inPurgeable=true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

//    8  录制视频
     static final int REQUEST_VIDEO_CAPTURE=1;
    private void dispatchTakeVideoIntent(){
        Intent takeVideaintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeVideaintent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takeVideaintent,REQUEST_VIDEO_CAPTURE);
        }
    }

//   10 打开相机
    @Override
    protected void onResume() {
        super.onResume();
        safeCameraOpen(0);
    }
    Camera mCamera;
    private  boolean safeCameraOpen(int id){
        boolean qOpened=false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened=(mCamera!=null);
        }catch (Exception e){

        }
        return  qOpened;
    }
    Preview mPreview;
    private void releaseCameraAndPreview(){
//     mPreview.setCamera(null);
        if (mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }


//   11.1 Preview类
    class Preview extends ViewGroup implements SurfaceHolder.Callback{
    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    public Preview(Context context) {
        super(context);
        mSurfaceView=new SurfaceView(context);
        addView(mSurfaceView);
        mHolder=mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // 11.2   设置和启动Preview
    public void setmCamera(Camera camera) throws IOException {
        if (mCamera==camera){
            return;
        }
        stopPreviewAndFreeCamere();
        if (mCamera!=null){
            List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
//            mSupportedPreviewSizes=localSizes;
             requestLayout();
             mCamera.setPreviewDisplay(mHolder);
             mCamera.startPreview();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

//    11.3  修改相机设置
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setPreviewSize();
         requestLayout();
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
         if (mCamera!=null){
             mCamera.stopPreview();
         }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    }


   private  void stopPreviewAndFreeCamere(){
       if (mCamera!=null){
           mCamera.stopPreview();
           mCamera.release();
           mCamera=null;
       }
   }


}
