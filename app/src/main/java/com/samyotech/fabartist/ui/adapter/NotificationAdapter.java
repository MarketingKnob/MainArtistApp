package com.samyotech.fabartist.ui.adapter;

/**
 * Created by mayank on 31/10/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samyotech.fabartist.DTO.NotificationDTO;
import com.samyotech.fabartist.utils.CustomTextViewBold;
import com.samyotech.fabartist.R;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.utils.ProjectUtils;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<NotificationDTO> notificationDTOlist;


    public NotificationAdapter(Context mContext, ArrayList<NotificationDTO> notificationDTOlist) {
        this.mContext = mContext;
        this.notificationDTOlist = notificationDTOlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapternotification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvTitle.setText(notificationDTOlist.get(position).getTitle());
        holder.tvMsg.setText(notificationDTOlist.get(position).getMsg());
        try {
            holder.tvDay.setText(ProjectUtils.getDisplayableDay(ProjectUtils.correctTimestamp(Long.parseLong(notificationDTOlist.get(position).getCreated_at()))));
            holder.tvDate.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(notificationDTOlist.get(position).getCreated_at()))));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        return notificationDTOlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold tvTitle;
        public CustomTextView tvDay, tvDate, tvMsg;

        public MyViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvDay = view.findViewById(R.id.tvDay);
            tvDate = view.findViewById(R.id.tvDate);
            tvMsg = view.findViewById(R.id.tvMsg);

        }
    }

}