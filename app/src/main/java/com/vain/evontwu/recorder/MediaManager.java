package com.vain.evontwu.recorder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.IOException;

/**
 * Created by EvontWu on 2015/8/20.
 */
public class MediaManager {

    private static MediaPlayer rMediaPlayer;
    private static boolean isPause;
    public static void playSound(String filePath, OnCompletionListener onCompletionListener) {
if(rMediaPlayer == null){
    rMediaPlayer = new MediaPlayer();
    rMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            rMediaPlayer.reset();
            return false;
        }
    });
}else{
    rMediaPlayer.reset();
}
        try {
        rMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        rMediaPlayer.setOnCompletionListener(onCompletionListener);

            rMediaPlayer.setDataSource(filePath);
            rMediaPlayer.prepare();
            rMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void pause(){
        if(rMediaPlayer != null && rMediaPlayer.isPlaying()){
            rMediaPlayer.pause();
            isPause = true;
        }

    }

    public static void resume(){
        if(rMediaPlayer != null && isPause){
            rMediaPlayer.start();
            isPause= false;
        }
    }

    public static void release(){

        if(rMediaPlayer != null){
            rMediaPlayer.release();
            rMediaPlayer=null;
        }
    }
}
