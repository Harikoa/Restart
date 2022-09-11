package com.meyvn.restart_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.JournalPojo;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.MyJournalHolder> {
    Context ctx;
    ArrayList<JournalPojo> list;

    public JournalAdapter(Context ctx, ArrayList<JournalPojo> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public JournalAdapter.MyJournalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item,parent,false);
        return new MyJournalHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.MyJournalHolder holder, int position) {
        JournalPojo pojo = list.get(position);
        holder.date.setText(pojo.getDate());
        holder.mood.setText(pojo.getMood());
    }

    @Override
    public int getItemCount() {
       return list.size();
    }
    public static class MyJournalHolder extends RecyclerView.ViewHolder{
        TextView date, mood;
        public MyJournalHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.journalDate);
            mood = itemView.findViewById(R.id.moodRating);
        }
    }
}
