package com.meyvn.restart_mobile;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SG_PostAdapter extends RecyclerView.Adapter<SG_ViewHolder> {

    SG_Main listActivity;
    List<SG_Model> modelList;


    public SG_PostAdapter(SG_Main listActivity, List<SG_Model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public SG_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sg_post_items, parent, false);

        SG_ViewHolder viewHolder = new SG_ViewHolder(itemView);
        viewHolder.setOnClickListener(new SG_ViewHolder.ClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                String title = modelList.get(position).getTitle();
                String description = modelList.get(position).getDescription();
                String name = modelList.get(position).getName();


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });



        return viewHolder;



    }

    @Override
    public void onBindViewHolder(@NonNull SG_ViewHolder holder, int position) {
        holder.ptitle.setText(modelList.get(position).getTitle());
        holder.pdescrition.setText(modelList.get(position).getDescription());
        holder.pname.setText(modelList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
