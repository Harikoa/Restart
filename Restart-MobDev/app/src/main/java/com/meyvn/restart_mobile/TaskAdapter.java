package com.meyvn.restart_mobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.ViewTaskPojo;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyTaskHolder> {
    RecyclerViewInterface rc;
    Context ctx;
    ArrayList<ViewTaskPojo> list;

    public TaskAdapter(Context ctx, ArrayList<ViewTaskPojo> list,RecyclerViewInterface rc) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
    }

    @NonNull
    @Override
    public TaskAdapter.MyTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.task_items,parent,false);
        return new MyTaskHolder(v,rc);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyTaskHolder holder, int position) {
        ViewTaskPojo pojo = list.get(position);
        holder.date.setText(pojo.getTaskDate());
        holder.title.setText(pojo.getTitle());
        holder.deadline.setText(pojo.getTaskDeadline());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyTaskHolder extends RecyclerView.ViewHolder{
        TextView date, title, deadline;
        public MyTaskHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            date = itemView.findViewById(R.id.taskDate);
            title = itemView.findViewById(R.id.taskTitle);
            deadline = itemView.findViewById((R.id.taskDeadline));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rc != null)
                    {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION)
                            rc.onItemclick(pos);
                    }
                }
            });
        }
    }
}
