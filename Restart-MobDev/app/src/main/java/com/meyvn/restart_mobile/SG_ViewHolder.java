package com.meyvn.restart_mobile;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SG_ViewHolder extends RecyclerView.ViewHolder {

    TextView ptitle, pdescrition, pdate, pcomment, pname;
    View view;


    public SG_ViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pclick.OnItemClick(view, getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pclick.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
        ptitle = itemView.findViewById(R.id.ptitle);
        pname = itemView.findViewById(R.id.pname);
        pdescrition = itemView.findViewById(R.id.pdescription);
        pdate = itemView.findViewById(R.id.pdate);
        pcomment = itemView.findViewById(R.id.pcomment);
    }
    private SG_ViewHolder.ClickListener pclick;
    public interface ClickListener{
        void OnItemClick (View view, int position);
        void onItemLongClick (View view, int position);
    }
    public void setOnClickListener(SG_ViewHolder.ClickListener clicklistener){
        pclick = clicklistener;
    }

}
