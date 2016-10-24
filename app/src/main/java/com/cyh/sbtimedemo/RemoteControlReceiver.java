package com.cyh.sbtimedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

public class RemoteControlReceiver extends BroadcastReceiver {
    public RemoteControlReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        使用硬件播放按钮监听音频播放
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)){
            KeyEvent event=intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_MEDIA_PLAY==event.getKeyCode()){

            }
        }else if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)){

        }
    }


}
