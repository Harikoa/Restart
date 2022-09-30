package com.meyvn.restart_mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.JournalPojo;
import com.meyvn.restart_mobile.R;
import com.meyvn.restart_mobile.RecyclerViewInterface;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.MyJournalHolder> {
    RecyclerViewInterface rc;
    Context ctx;
    ArrayList<JournalPojo> list;

    public JournalAdapter(Context ctx, ArrayList<JournalPojo> list,RecyclerViewInterface rc) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
    }

    @NonNull
    @Override
    public JournalAdapter.MyJournalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item,parent,false);
        return new MyJournalHolder(v,rc);
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
        public MyJournalHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            date = itemView.findViewById(R.id.journalDate);
            mood = itemView.findViewById(R.id.moodRating);
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
