package com.meyvn.restart_mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.meyvn.restart_mobile.POJO.commentPOJO;
import com.meyvn.restart_mobile.R;
import com.meyvn.restart_mobile.RecyclerViewInterface;
import com.meyvn.restart_mobile.commentRecyclerInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.MyCommentHolder> {
    commentRecyclerInterface rc;
    Context ctx;
    ArrayList<commentPOJO> list;

    public commentAdapter(Context ctx, ArrayList<commentPOJO> list, commentRecyclerInterface rc) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
    }

    @NonNull
    @Override
    public commentAdapter.MyCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.comments_items,parent,false);
        return new MyCommentHolder(v,rc);
    }

    @Override
    public void onBindViewHolder(@NonNull commentAdapter.MyCommentHolder holder, int position) {
        commentPOJO pojo = list.get(position);
        holder.content.setText(pojo.getCommentContent());
        holder.nickname.setText("@" + pojo.getNickName());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        holder.date.setText(sdf.format(pojo.getDatePosted()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyCommentHolder extends RecyclerView.ViewHolder{
        TextView date, nickname, content;
        public MyCommentHolder(@NonNull View itemView, commentRecyclerInterface rc) {
            super(itemView);
            date = itemView.findViewById(R.id.commentDate);
            nickname = itemView.findViewById(R.id.commentNickName);
            content = itemView.findViewById((R.id.commentContent));
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (rc != null)
                    {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            {
                                rc.onItemclick(pos,itemView);
                            }
                        }
                    }

                    return true;
                }
            });

        }
    }
}
