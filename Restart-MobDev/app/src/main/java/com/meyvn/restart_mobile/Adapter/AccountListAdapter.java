package com.meyvn.restart_mobile.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meyvn.restart_mobile.POJO.Account;
import com.meyvn.restart_mobile.R;
import com.meyvn.restart_mobile.RecyclerViewInterface;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyAccountHolder> {
    RecyclerViewInterface rc;
    Context ctx;
    ArrayList<Account> list;

    public AccountListAdapter(Context ctx, ArrayList<Account> list, RecyclerViewInterface rc) {
        this.ctx = ctx;
        this.list = list;
        this.rc = rc;
    }

    @NonNull
    @Override
    public AccountListAdapter.MyAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.account_items,parent,false);
        return new MyAccountHolder(v,rc);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListAdapter.MyAccountHolder holder, int position) {
        Account pojo = list.get(position);
        holder.nickname.setText(pojo.getNickname());
        holder.type.setText(pojo.getRole());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyAccountHolder extends RecyclerView.ViewHolder{
        TextView nickname, type;
        public MyAccountHolder(@NonNull View itemView, RecyclerViewInterface rc) {
            super(itemView);
            nickname = itemView.findViewById(R.id.accNickname);
            type = itemView.findViewById(R.id.accType);
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