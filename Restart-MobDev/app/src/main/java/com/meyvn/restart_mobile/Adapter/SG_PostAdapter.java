package com.meyvn.restart_mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.SGPostPOJO;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;
import com.meyvn.restart_mobile.R;
import com.meyvn.restart_mobile.RecyclerViewInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SG_PostAdapter extends RecyclerView.Adapter<SG_PostAdapter.MySGPOSTHolder> {
    RecyclerViewInterface rc;
    Context ctx;
    ArrayList<SGPostPOJO> list;

    public SG_PostAdapter(Context ctx, ArrayList<SGPostPOJO> list,RecyclerViewInterface rc) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
    }

    @NonNull
    @Override
    public SG_PostAdapter.MySGPOSTHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.sg_post_items,parent,false);
        return new MySGPOSTHolder(v,rc);
    }

    @Override
    public void onBindViewHolder(@NonNull SG_PostAdapter.MySGPOSTHolder holder, int position) {
        SGPostPOJO pojo = list.get(position);
        holder.nickname.setText(pojo.getUserNickname());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault());
        holder.date.setText(sdf.format(pojo.getDatePosted()));
        holder.description.setText(pojo.getContent());
        holder.title.setText(pojo.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MySGPOSTHolder extends RecyclerView.ViewHolder{
        TextView date, title, description,nickname;
        public MySGPOSTHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            date = itemView.findViewById(R.id.pdate);
            title = itemView.findViewById(R.id.ptitle);
            description = itemView.findViewById((R.id.pdescription));
            nickname = itemView.findViewById(R.id.pname);
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
