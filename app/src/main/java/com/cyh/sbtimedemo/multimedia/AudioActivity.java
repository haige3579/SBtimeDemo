package com.cyh.sbtimedemo.multimedia;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cyh.sbtimedemo.R;
import com.cyh.sbtimedemo.RemoteControlReceiver;

public class AudioActivity extends AppCompatActivity {

    /**
      *@cyh  a 控制音量和音频播放
     *         对音频流的操作：play/stop/pause/skip/previous
      *         1 鉴别使用哪种音频流 --STREAM_MUSIC
     *          2 使用硬件音量键控制应用音量 --setVolumeControlStream() onCreate()中调用
     *          3 使用硬件播放控制按键来监控应用播放 --线控或无线耳机点击播放、停止等系统广播带有ACTION_MEDIA_BUTTON的intent，监听广播实现
      *       b 管理音频焦点  --Audio Focus
     *          4 请求获取音频焦点
     *          5 处理失去音频焦点 --OnAudioFocusChangeListener()
     *          6  Duck
     *        c 兼容音频输出设备
     *          7 检测使用的硬件设备
     *          8 处理音频输出设备的改变  --Action_audio_becoming_noisy
     * created at 2016/10/24 11:05
      */

    RemoteControlReceiver receiver=new RemoteControlReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        // 2 硬件音量键控制应用音量
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //3
        AudioManager am= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        am.registerMediaButtonEventReceiver(receiver);
//        am.unregisterMediaButtonEventReceiver(receiver);


        // 5   失去焦点 :允许Ducking的短暂失去、暂时失去、永久失去 --Duck
        AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                 if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){

                 }else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){

                 }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){

                 }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){

                 }
            }
        };

        // 4 播放音乐时请求永久音频焦点
        int result = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
//            am.registerMediaButtonEventReceiver(RemoteControlReceiver);
        }
        //播放结束  告知系统不再获取焦点
        am.abandonAudioFocus(afChangeListener);
        //获取短暂音频焦点 如导航指示
        int i = am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        if (i==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

        }

        // 7
//        if (AudioManager)

    }

    private IntentFilter intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private void startPlayback(){
       registerReceiver(receiver,intentFilter);
    }
    private void stopPlayback(){
        unregisterReceiver(receiver);
    }
}
