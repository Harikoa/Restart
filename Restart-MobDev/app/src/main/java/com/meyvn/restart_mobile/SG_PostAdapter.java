package com.meyvn.restart_mobile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SG_PostAdapter extends RecyclerView.Adapter <SG_PostAdapter.PostViewHolder> {

    private List<SG_PostModel> plist;
    private Activity context;

    public SG_PostAdapter(Activity context, List<SG_PostModel> plist) {
        this.plist = plist;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.post_items, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return plist.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView comments;
        TextView title, description, date, name;
        View view;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setpostuser(String username) {
            name = view.findViewById(R.id.pname);
            name.setText(username);
        }

        public void setpostdate(int pdate) {
            date = view.findViewById(R.id.pdate);
            date.setText(pdate);
        }
        public void setposttitle(String ptitle) {
            title = view.findViewById(R.id.ptitle);
            title.setText(ptitle);
        }
        public void setpostdescription(String pdesc) {
            description = view.findViewById(R.id.pdescription);
            description.setText(pdesc);
        }
    }
}