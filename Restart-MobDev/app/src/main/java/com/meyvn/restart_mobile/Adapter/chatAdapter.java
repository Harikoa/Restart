package com.meyvn.restart_mobile.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.MessagePOJO;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;
import com.meyvn.restart_mobile.R;
import com.meyvn.restart_mobile.RecyclerViewInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    RecyclerViewInterface rc;
    Context ctx;
    ArrayList<MessagePOJO> list;
    String sender;
    String receiver;
    final static int VIEW_SEND = 1;
    final static int VIEW_RECEIVE = 2;
    public chatAdapter(Context ctx, ArrayList<MessagePOJO> list, RecyclerViewInterface rc,String send, String receive) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
        this.sender = send;
        this.receiver = receive;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType==1)
       {
           View v = LayoutInflater.from(ctx).inflate(R.layout.sent_message_items,parent,false);
           return new MySendHolder(v,rc);
       }
       else {
           View v = LayoutInflater.from(ctx).inflate(R.layout.receive_messages_items, parent, false);
           return new MyReceivedHolder(v, rc);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagePOJO pojo = list.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault());
       if(getItemViewType(position)==VIEW_SEND)
       {
           ((MySendHolder) holder).date.setText(sdf.format(pojo.getDate()));
           ((MySendHolder) holder).message.setText(pojo.getMsgContent());
       }
       else
       {
           ((MyReceivedHolder) holder).date.setText(sdf.format(pojo.getDate()));
           ((MyReceivedHolder) holder).message.setText(pojo.getMsgContent());
       }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getSenderEmail().equals(sender))
            return VIEW_SEND;
        else
            return VIEW_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MySendHolder extends RecyclerView.ViewHolder {
        TextView date, message;
        public MySendHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            date = itemView.findViewById(R.id.timeSent);
            message = itemView.findViewById(R.id.sentMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rc != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                            rc.onItemclick(pos);
                    }
                }
            });
        }
    }

    public static class MyReceivedHolder extends RecyclerView.ViewHolder {
        TextView date, message;
        public MyReceivedHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            date = itemView.findViewById(R.id.timeReceived);
            message = itemView.findViewById(R.id.receiveMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rc != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                            rc.onItemclick(pos);
                    }
                }
            });
        }
    }
}