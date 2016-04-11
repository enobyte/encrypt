package com.education.smsencrypt.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.smsencrypt.R;
import com.education.smsencrypt.utils.listItemObject.ListItemRecieveSMS;

import java.util.List;

/**
 * Created by Eno on 3/27/2016.
 */
public class itemReceiveAdapter extends RecyclerView.Adapter<itemReceiveAdapter.ReceiverHolder> {
    private List<ListItemRecieveSMS> receiveAdapterList;
    Activity activity;
    View itemLayoutView;
    ViewGroup mParent;

    public itemReceiveAdapter(Activity activity, List<ListItemRecieveSMS> receiveAdapterList) {
        this.receiveAdapterList = receiveAdapterList;
        this.activity = activity;

    }


    @Override
    public ReceiverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mParent = parent;
        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sendreceivesms, parent, false);
        ReceiverHolder viewHolder = new ReceiverHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReceiverHolder holder, int position) {
        final ListItemRecieveSMS item = receiveAdapterList.get(position);
        if (item != null) {
            holder.numberPhone.setText(item.getFrom());
            holder.konten.setText(item.getContent());
            holder.date.setText(item.getDate());
            holder.firtIdentity.setText(item.getFirtIcon());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ReceiverHolder extends RecyclerView.ViewHolder {
        public TextView numberPhone;
        public TextView konten;
        public TextView date;
        public TextView firtIdentity;

        public ReceiverHolder(View itemView) {
            super(itemView);
            numberPhone = (TextView) itemView.findViewById(R.id.txtHP);
            konten = (TextView) itemView.findViewById(R.id.txtKonten);
            date = (TextView) itemView.findViewById(R.id.txtTgl);
            firtIdentity = (TextView) itemView.findViewById(R.id.txtIcon);
        }
    }
}
