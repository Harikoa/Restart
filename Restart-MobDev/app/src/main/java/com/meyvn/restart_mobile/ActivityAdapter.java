package com.meyvn.restart_mobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.ActivityPojo;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyActivityHolder> {

    Context ctx;
    ArrayList<ActivityPojo> list;

    public ActivityAdapter(Context ctx, ArrayList<ActivityPojo> list) {
        this.ctx = ctx;
        this.list = list;

    }

    @NonNull
    @Override
    public ActivityAdapter.MyActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.activity_items,parent,false);
        return new MyActivityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.MyActivityHolder holder, int position) {
        ActivityPojo pojo = list.get(position);
        holder.title.setText(pojo.getActivityTitle());
        holder.venue.setText(pojo.getVenue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyActivityHolder extends RecyclerView.ViewHolder{
        TextView title,venue;
        public MyActivityHolder(@NonNull View itemView) {
            super(itemView);
            venue = itemView.findViewById(R.id.activityVenue);
            title = itemView.findViewById(R.id.activityTitle);

        }
    }
}
