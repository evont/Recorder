package com.vain.evontwu.recorder.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.vain.evontwu.recorder.R;

public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {
    private static final int DISTACE_CANCEL = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING =2;
    private static final int STATE_CANCEL=3;
    private int CurState = STATE_NORMAL;
    private boolean isRecording = false;

    private DialogManager rDialogManager;
    private AudioManager rAudioManager;
    private float rTime;
    private boolean rReady;

    public AudioRecorderButton(Context context) {
        super(context);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        rDialogManager = new DialogManager(getContext());
        String dir = Environment.getExternalStorageDirectory()+"/evont_recorder";
        rAudioManager = AudioManager.getInstance(dir);
        rAudioManager.setOnAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                rReady = true;
                rAudioManager.prepareAudio();
                return false;
            }
        });
    }

    public interface AudioFinishRecorderListener{
        void onFinish(float seconds,String filePath);
    }

    private AudioFinishRecorderListener rListener;
    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
        rListener = listener;
    }
private Runnable rGetVoiceLevelRunnable = new Runnable() {
    @Override
    public void run() {
        while (isRecording){
            try {
                Thread.sleep(100);
                rTime += 0.1f;
                rHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
};
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGED = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;
    private Handler rHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    rDialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(rGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    rDialogManager.updateVoiceLevel(rAudioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    rDialogManager.dismissDialog();
                    break;
            }
        }
    };

    @Override
    public void wellPrepard() {
            rHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                isRecording = true;
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isRecording){
                    if(wantToCancel(x,y)){
                        changeState(STATE_CANCEL);
                    }else{
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!rReady){
                    reset();
                    return super.onTouchEvent(event);
                }
                if(!isRecording || rTime < 0.6f){
                    rDialogManager.tooShort();
                    rAudioManager.cancel();
                    rHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                }else if(CurState == STATE_RECORDING){
                    rDialogManager.dismissDialog();
                    if(rListener != null){
                        rListener.onFinish(rTime,rAudioManager.getCurrentFilePath());
                    }
                    rAudioManager.release();
                }else if(CurState == STATE_CANCEL){
                    rDialogManager.dismissDialog();
                    rAudioManager.cancel();
                }
                reset();
                break;

        }
        return super.onTouchEvent(event);
    }

    private void reset() {
        rReady = false;
        isRecording = false;

        rTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel(int x, int y) {
        if(x <0 || x > getWidth()){
            return true;
        }
        if(y < -DISTACE_CANCEL || y>getHeight()+DISTACE_CANCEL){
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if(CurState != state){
            CurState = state;
            switch (state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_record_normal);
                    setText(R.string.btn_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.btn_recording);
                    if(isRecording){
                        rDialogManager.recording();
                    }
                    break;
                case STATE_CANCEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.btn_cancel);
                    if(isRecording){
                        rDialogManager.wantToCancel();
                    }
                    break;

            }
        }
    }



}
