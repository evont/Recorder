package com.vain.evontwu.recorder.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vain.evontwu.recorder.R;

public class DialogManager {

    private Dialog rDialog;
    private ImageView rIcon;
    private ImageView rVoice;
    private TextView rLabel;
    //上下文引用
    private Context rContext;

    public DialogManager(Context context)
    {
        rContext = context;
    }

    public void showRecordingDialog(){
rDialog = new Dialog(rContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater=LayoutInflater.from(rContext);
        View view = inflater.inflate(R.layout.dialog_recorder,null);
        rDialog.setContentView(view);

        rIcon = (ImageView) rDialog.findViewById(R.id.recorder_dialog_icon);
        rVoice = (ImageView) rDialog.findViewById(R.id.recorder_dialog_voice);
        rLabel = (TextView) rDialog.findViewById(R.id.recorder_dialog_label);


        rDialog.show();

    }

    public void recording(){
        if(rDialog != null && rDialog.isShowing())
        {
            rIcon.setVisibility(View.VISIBLE);
            rVoice.setVisibility(View.VISIBLE);
            rLabel.setVisibility(View.VISIBLE);

rIcon.setImageResource(R.drawable.recorder);
            rLabel.setText(R.string.dialog_normal);
        }

    }


    public void wantToCancel(){
        if(rDialog != null && rDialog.isShowing())
        {
            rIcon.setVisibility(View.VISIBLE);
            rVoice.setVisibility(View.GONE);
            rLabel.setVisibility(View.VISIBLE);

            rIcon.setImageResource(R.drawable.cancel);
            rLabel.setText(R.string.dialog_cancel);
        }

    }

    public void tooShort(){
        if(rDialog != null && rDialog.isShowing())
        {
            rIcon.setVisibility(View.VISIBLE);
            rVoice.setVisibility(View.GONE);
            rLabel.setVisibility(View.VISIBLE);

            rIcon.setImageResource(R.drawable.voice_to_short);
            rLabel.setText(R.string.dialog_tooshort);
        }
    }

    public void dismissDialog(){
        if(rDialog != null && rDialog.isShowing())
        {
            rDialog.dismiss();
            rDialog = null;
        }

    }

    public void updateVoiceLevel(int level){
        if(rDialog != null && rDialog.isShowing())
        {
            /*rIcon.setVisibility(View.VISIBLE);
            rVoice.setVisibility(View.VISIBLE);
            rLabel.setVisibility(View.VISIBLE);*/

            int imageId = rContext.getResources().getIdentifier("v"+level,"drawable",rContext.getPackageName());
            rVoice.setImageResource(imageId);
        }

    }

}
