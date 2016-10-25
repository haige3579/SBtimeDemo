package com.cyh.sbtimedemo.multimedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cyh.sbtimedemo.R;

public class PrintingActivity extends AppCompatActivity {
   
    /**
      *@cyh  打印    a  打印照片 --v4 support （PrintHelper）
      *                1  打印一幅图片  --推荐放在菜单选项里面 ActionBar
     *               b  打印HTML文档  --WebView  (最少API19)
     *                 2 加载一个HTML文档
     *                 3 创建一个打印任务
     *               C 打印自定义文档
     *                 4 连接打印管理器   PrintManager
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



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
       public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

       }

    @Override
       public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

       }

    @Override
    public void onFinish() {
        super.onFinish();
    }
   }
}
