package com.cyh.sbtimedemo.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cyh.sbtimedemo.R;

import java.io.FileOutputStream;
import java.io.IOException;

public class PrintingActivity extends AppCompatActivity {

    /**
      *@cyh  打印    a  打印照片 --v4 support （PrintHelper）
      *                1  打印一幅图片  --推荐放在菜单选项里面 ActionBar
     *               b  打印HTML文档  --WebView  (最少API19)
     *                 2 加载一个HTML文档
     *                 3 创建一个打印任务
     *               C 打印自定义文档
     *                 4 连接打印管理器   PrintManager
     *                 5 打印设配器
     *                 6 计算打印文档信息
     *                 7 将打印文档写入文件
     *                 8 绘制PDF页面内容
      *created at 2016/10/25 16:51
      */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);
    }

    //1  打印图片实例
    private void doPhotoPrint(){
        PrintHelper helper = new PrintHelper(this);
        helper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        helper.printBitmap("cyh.jpg",bitmap);
    }

//    2  加载HTML文档
    private WebView mWebView;
    private void doWebViewPrint(){
        //创建对象
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                mWebView=null;
            }
        });
        //动态生成HTML
        String htmlDocument="<html><body><h1>Text Content</h1><p>Testing,"+"testing,testing...</p></body></html>";
        webView.loadDataWithBaseURL(null,htmlDocument,"text/HTML","UTF-8",null);
        //包含图片
        webView.loadDataWithBaseURL("file://android_asset/images/",htmlDocument,"text/HTML","UTF-8",null);
        mWebView=webView;
    }
//    3 打印HTML文档
    private void createWebPrintJob(WebView view) {
        // PrintManager实例
        PrintManager pm= (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        //打印适配器实例
//        PrintDocumentAdapter adapter = view.createPrintDocumentAdapter();
//
//        //打印作业名称和适配器实例
//        String s = getString(R.string.app_name);
//        PrintJob print = pm.print(s, adapter, new PrintAttributes.Builder().build());
//        //保存对象后状态检查
//        mPrintJobs.add(print);
    }

//     4 打印管理器
    private void doPrint(){
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + "Document";
        printManager.print(jobName,new MyPrintDocumentAdapter(this),null);
    }

//     5 打印适配器
   class MyPrintDocumentAdapter extends PrintDocumentAdapter {
       MyPrintDocumentAdapter(Context context){

       }
//   打印开始 被调用
    @Override
    public void onStart() {
        super.onStart();
    }
//  改变输出设置时，被调用，重新计算打印页面的布局
// 6 计算打印信息
    PrintedPdfDocument mPdfDocument;
   @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
      //创建PdfDocument请求页面属性
       mPdfDocument= new PrintedPdfDocument(PrintingActivity.this, newAttributes);
       //回复取消请求
       if (cancellationSignal.isCanceled()){
           callback.onLayoutCancelled();
           return;
       }
       // 计算预期的印刷页数
       int pages=computePageCount(newAttributes);
       //返回的信息打印打印框架
       if (pages>0) {
           PrintDocumentInfo info = new PrintDocumentInfo.Builder("print_output.pdf")
                   .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                   .setPageCount(pages).build();

           //完成内容布局
           callback.onLayoutFinished(info, true);
       } else {
       //打印错误的框架
           callback.onLayoutFailed("Page count calulation failed...");
       }
   }
   //根据打印放心确定页面
    private int  computePageCount(PrintAttributes newAttributes) {
       int itemsPerpage=4;//竖屏模式的默认项数
        PrintAttributes.MediaSize mediaSize = newAttributes.getMediaSize();
        if (!mediaSize.isPortrait()){
            itemsPerpage=6;//在横向每页6个条目
        }
        //确定数量的印刷物品
       int printItemCount=getPrintItemCount();
        return (int) Math.ceil(printItemCount/itemsPerpage);
    }



    // 将打印页面渲染成待打印的文件
//    7 写入文件  讲内容 渲染成多页面的PDF文件
      int totalPages=10;
    PdfDocument.Page page;
    @Override
       public void onWrite(PageRange[] pages,
                           ParcelFileDescriptor destination,
                           CancellationSignal cancellationSignal,
                           WriteResultCallback callback) {
         //遍历文件
        for (int i=0;i<totalPages;i++) {
            //检查范围
            if (containsPage(pages,i)) {
//                writtenPagesArray.append(writtenPagesAyyay.size(),i);
               page=mPdfDocument.startPage(i);
                if (cancellationSignal.isCanceled()){
                    callback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument=null;
                    return;
                }
                drawPage(page);
                mPdfDocument.finishPage(page);
            }
        }
        try{
            mPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        }catch (IOException e){
            callback.onWriteFailed(e.toString());
            return;
        }finally {
            mPdfDocument.close();
            mPdfDocument=null;
        }

       }

    private void drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();
        int titleBaseLine=72;
        int leftMargin=54;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        canvas.drawText("Text Title",leftMargin,titleBaseLine,paint);
        paint.setTextSize(11);
        canvas.drawText("Test paragraph",leftMargin,titleBaseLine+25,paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(100,100,172,172,paint);
    }

    private boolean containsPage(PageRange[] pages, int i) {
        return true;
    }

    // 打印结束后被调用
    @Override
    public void onFinish() {
        super.onFinish();
    }

    public int getPrintItemCount() {
        return 0;
    }
}
}
