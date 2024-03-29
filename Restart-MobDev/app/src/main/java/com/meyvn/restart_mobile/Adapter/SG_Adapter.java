package com.meyvn.restart_mobile.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.Login;
import com.meyvn.restart_mobile.POJO.SG_Model;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;
import com.meyvn.restart_mobile.R;
import com.meyvn.restart_mobile.RecyclerViewInterface;

import java.util.ArrayList;

public class SG_Adapter extends RecyclerView.Adapter<SG_Adapter.MySGHolder> {
    RecyclerViewInterface rc;
    Context ctx;
    ArrayList<SG_Model> list;

    public SG_Adapter(Context ctx, ArrayList<SG_Model> list,RecyclerViewInterface rc) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
    }

        public void clear() {
        int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }
    @NonNull
    @Override
    public SG_Adapter.MySGHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.sg_items,parent,false);
        return new MySGHolder(v,rc);
    }

    @Override
    public void onBindViewHolder(@NonNull SG_Adapter.MySGHolder holder, int position) {
        SG_Model pojo = list.get(position);
        holder.title.setText(pojo.getTitle());

        if(pojo.getNewmem().contains(Login.authACC))
            holder.title.setBackground(ctx.getDrawable(R.drawable.sg_new_mem));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MySGHolder extends RecyclerView.ViewHolder{
        TextView title;
        public MySGHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            title = itemView.findViewById(R.id.sg_item_name);
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
