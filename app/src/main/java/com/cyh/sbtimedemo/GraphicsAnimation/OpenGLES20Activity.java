package com.cyh.sbtimedemo.GraphicsAnimation;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLES20Activity extends AppCompatActivity {
    /**
      *@cyh      a 建立OpenGL ES 的环境
     *              1 在清单文件中声明OpenGL ES
      *             2  为OpenGL　ES图像创建一个activity   ---GLSurfaceView
     *              3  构建一个 GLSurfaceView对象
     *              4  构建一个渲染类
     *            b 定义 形状
     *               5 定义一个三角形
     *               6 定义一个矩形
     *            c  绘制形状
     *               7 初始化形状
     *               8 画一个形状
     *            d  运用投影与相机视角
     *               9 定义投影   --Renderer类中的onSurfaceChanged()
     *               10 定义一个相机视角 --Matrix.setLoolAtM()
      *created at 2016/11/16 13:56
      */
    //2
    private GLSurfaceView mGLView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //2.1
         mGLView=new GLSurfaceView(this);
        setContentView(mGLView);
    }

    //3
    class MyGLSurfaceView extends GLSurfaceView{
        private final MyGLRenderer myRenderer;
        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            myRenderer=new MyGLRenderer();
            setRenderer(myRenderer);
        }
    }
    //4 渲染类  -- 7 初始化形状  -- 8 辅助方法
    public class MyGLRenderer implements GLSurfaceView.Renderer{
        //7
        private Triangle mTriangle;
        private Square mSquare;
        //  调用一次，配置view的环境
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);
            // 7. 初始化
            mTriangle=new Triangle();
            mSquare=new Square();
        }
       // 每次重新绘制view时被调用
        //9.
         private final float [] mMVPMatrix=new float[16];
         private final float[] mProjectionMatrix=new float[16];
         private final float[] mViewMatrix=new float[16];
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0,0,width,height);
            //9. 投影
            float ratio=(float)width/height;
            Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
        }
        //如果view的几何形体发生变化时会被调用
        @Override
        public void onDrawFrame(GL10 gl) {
          GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            //8.5  调用draw()方法
            mTriangle.draw();
          // 10 相机视角
            Matrix.setLookAtM(mViewMatrix,0,0,0,-3,0f,0f,0f,0f,1.0f,0.0f);
            Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mViewMatrix,0);
            mTriangle.draw();
        }
       // 8.2 辅助方法
        public  int loadShader(int type,String shaderCode){
            int shader=GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader,shaderCode);
            GLES20.glCompileShader(shader);
            return  shader;
        }
    }

    // 5  三角形
    public class Triangle{
        // 8.3
        private final int mProgram;
        private FloatBuffer vertexBuffer;
        static final int COORDS_PER_VERTEX=3;
         float triangleCoords[] = {   // in counterclockwise order:
                0.0f,  0.622008459f, 0.0f, // top
                -0.5f, -0.311004243f, 0.0f, // bottom left
                0.5f, -0.311004243f, 0.0f  // bottom right
        };
        float color[]={0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
        public Triangle(){
            ByteBuffer bb=ByteBuffer.allocateDirect(triangleCoords.length*4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer=bb.asFloatBuffer();
            vertexBuffer.put(triangleCoords);
            vertexBuffer.position(0);
            //8.3 编译着色器代码
//            int vertexShader=MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
//            int fragmentShader=MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
            mProgram=GLES20.glCreateProgram();
//            GLES20.glAttachShader(mProgram,vertexShader);
//            GLES20.glAttachShader(mProgram,fragmentShader);
            GLES20.glLinkProgram(mProgram);
        }

        //8.1 着色器
        private final String vertexShaderCode="attribute vec4 vPosition;"+"void main(){"+" gl_Position=vPosition;"+"}";
        private final String fragmentShaderCode="precision medium float;"+"uniform vec4 vColor;"+"void main(){"+" gl_FragColor=vColor;"+"}";

        // 8.4 draw()绘制图形
        private int mPositionHandle;
        private int mColorHandle;
        private final int vertexCount=triangleCoords.length/COORDS_PER_VERTEX;
        private final int vertexStride=COORDS_PER_VERTEX;
        public void draw(){
            GLES20.glUseProgram(mProgram);
            mPositionHandle=GLES20.glGetAttribLocation(mProgram,"vPositon");
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,false,vertexStride,vertexBuffer);
            mColorHandle=GLES20.glGetUniformLocation(mProgram,"vColor");
            GLES20.glUniform4fv(mColorHandle,1,color,0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
            GLES20.glDisableVertexAttribArray(mPositionHandle);
        }
    }
    //6 矩形
    public class Square{
        private FloatBuffer vertexBuffer;
        private ShortBuffer drawListBuffer;
        static final int COORDS_PER_VERTEX=3;
         float squareCoords[] = {   // in counterclockwise order:
                -0.5f,  0.5f, 0.0f,   // top left
                -0.5f, -0.5f, 0.0f,   // bottom left
                0.5f, -0.5f, 0.0f,   // bottom right
                0.5f,  0.5f, 0.0f// bottom right
        };
        private short drawOrder[]={0,1,2,0,2,3};
        public Square(){
            ByteBuffer bb=ByteBuffer.allocateDirect(squareCoords.length*4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer=bb.asFloatBuffer();
            vertexBuffer.put(squareCoords);
            vertexBuffer.position(0);
            ByteBuffer d1b=ByteBuffer.allocateDirect(drawOrder.length*2);
            d1b.order(ByteOrder.nativeOrder());
            drawListBuffer=d1b.asShortBuffer();
            drawListBuffer.put(drawOrder);
            drawListBuffer.position(0);
        }
    }


}
