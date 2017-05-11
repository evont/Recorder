package com.vain.evontwu.recorder;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vain.evontwu.recorder.MainActivity.Recorder;

import java.util.List;

/**
 * Created by EvontWu on 2015/8/20.
 */
public class RecorderAdapter extends ArrayAdapter<Recorder>{

    private int rMinItemWidth;
    private int rMaxItemWidth;
    private LayoutInflater rInflater;

    public RecorderAdapter(Context context, List<Recorder> datas) {
        super(context, -1,datas);

rInflater = LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        rMaxItemWidth = (int)(outMetrics.widthPixels * 0.7f);
        rMinItemWidth = (int)(outMetrics.widthPixels * 0.1f);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = rInflater.inflate(R.layout.item_recorder,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.rSeconds = (TextView) convertView.findViewById(R.id.recorder_time);
            viewHolder.Length = convertView.findViewById(R.id.record_length);
            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.rSeconds.setText(Math.round(getItem(position).time)+"\"");
        ViewGroup.LayoutParams lp =viewHolder.Length.getLayoutParams();
        lp.width = (int)(rMinItemWidth + (rMaxItemWidth/60f * getItem(position).time));
        return convertView;
    }

    private class ViewHolder{
        TextView rSeconds;
        View Length;

    }
}
