package com.vain.evontwu.recorder.view;

import android.media.MediaRecorder;

import java.io.File;
import java.util.UUID;

public class AudioManager {
private MediaRecorder rMediaRecorder;
    private String rDir;
    private String rCurrentFilePath;
    private boolean isPrepared;

    private static AudioManager rInstance;
    private AudioManager(String dir){ rDir = dir;}

    public String getCurrentFilePath() {
        return rCurrentFilePath;
    }

    public interface AudioStateListener{
        void wellPrepard();
    }
    public AudioStateListener rListener;
    public void setOnAudioStateListener(AudioStateListener listener){
      rListener = listener;
    }
    public static AudioManager getInstance(String dir){
        if(rInstance == null){
            synchronized (AudioManager.class){
                if(rInstance == null){
                    rInstance = new AudioManager(dir);
                }
            }
        }
        return rInstance;
    }

    public void prepareAudio() {
        try {
            isPrepared = false;
        File dir = new File(rDir);
        if(!dir.exists()){dir.mkdirs();}
        String fileName = generateFileName();
        File file = new File(dir,fileName);
        rCurrentFilePath = file.getAbsolutePath();
        rMediaRecorder = new MediaRecorder();
        rMediaRecorder.setOutputFile(file.getAbsolutePath());
        rMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        rMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        rMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            rMediaRecorder.prepare();
        rMediaRecorder.start();
        isPrepared = true;
        if(rListener != null){rListener.wellPrepard();}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel){
        if(isPrepared){
            try {
                return maxLevel * rMediaRecorder.getMaxAmplitude()/32768 +1;
            }catch (Exception e){
e.printStackTrace();
            }
        }
        return 1;
    }
    public void release(){
        rMediaRecorder.stop();
        rMediaRecorder.release();
        rMediaRecorder = null;
    }

    public void cancel(){
        release();
        if(rCurrentFilePath != null){
            File file = new File(rCurrentFilePath);
            file.delete();
            rCurrentFilePath = null;
        }

    }
}
