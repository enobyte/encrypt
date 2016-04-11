package com.education.smsencrypt.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.education.smsencrypt.R;
import com.education.smsencrypt.utils.listItemObject.ListItemRecieveSMS;

import java.util.List;

/**
 * Created by Eno on 3/29/2016.
 */
public class RoomSMSAdapter extends RecyclerView.Adapter<RoomSMSAdapter.SMSHolder> {
    private List<ListItemRecieveSMS> itemData;
    View itemLayoutView;
    ViewGroup mParent;
    Activity activity;

    public RoomSMSAdapter(Activity activity, List<ListItemRecieveSMS> itemData) {
        this.activity = activity;
        this.itemData = itemData;
    }

    @Override
    public SMSHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent             = parent;
        itemLayoutView      = LayoutInflater.from(parent.getContext()).inflate(R.layout.textroom_activity, parent, false);
        SMSHolder smsHolder = new SMSHolder(itemLayoutView);
        return smsHolder;
    }

    @Override
    public void onBindViewHolder(SMSHolder holder, int position) {
        final ListItemRecieveSMS item = itemData.get(position);
        holder.leftSMS.setText(item.getContent());
        if (item != null) {
            if (item.getStatus().equalsIgnoreCase("1")) {
                holder.leftSMS.setGravity(Gravity.RIGHT);
                holder.linerChat.setGravity(Gravity.RIGHT);
                holder.relativeLayout.setGravity(Gravity.RIGHT);
                holder.relativeLayout.setBackgroundResource(R.drawable.speech_bubble_out);
            } else {
                holder.leftSMS.setGravity(Gravity.LEFT);
                holder.linerChat.setGravity(Gravity.LEFT);
                holder.relativeLayout.setGravity(Gravity.LEFT);
                holder.relativeLayout.setBackgroundResource(R.drawable.speech_bubble_in);
            }
        }
    }


    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class SMSHolder extends RecyclerView.ViewHolder {
        TextView leftSMS, dateSMS;
        LinearLayout linerChat;
        RelativeLayout relativeLayout;

        public SMSHolder(View itemView) {
            super(itemView);
            leftSMS         = (TextView) itemView.findViewById(R.id.leftSMS);
            dateSMS         = (TextView) itemView.findViewById(R.id.tgljam);
            linerChat       = (LinearLayout) itemView.findViewById(R.id.linierChat);
            relativeLayout  = (RelativeLayout) itemView.findViewById(R.id.rtextroom);
        }
    }

}