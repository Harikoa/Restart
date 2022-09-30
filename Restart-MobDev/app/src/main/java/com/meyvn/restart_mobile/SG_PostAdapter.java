package com.meyvn.restart_mobile;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class SG_PostAdapter extends RecyclerView.Adapter <SG_PostAdapter.PostViewHolder> {

    private List<SG_PostModel> plist;
    private Activity context;
    private FirebaseFirestore firestore;



    public SG_PostAdapter(Activity context, List<SG_PostModel> plist) {
        this.plist = plist;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.post_items, parent, false);
        firestore = FirebaseFirestore.getInstance();
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        SG_PostModel post = plist.get(position);
        String nickname = post.getUser();
        firestore.collection("Support Groups").document("SGForAlcoholics").collection("Post").document(nickname)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String username = task.getResult().getString("user");

                    holder.setpostuser(username);

                }else {
                    Toast.makeText(context,task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });




        holder.setposttitle(post.getTitle());
        holder.setpostdescription(post.getDescription());

        long milliseconds = post.getTime().getTime();
        String date = DateFormat.format("MM/dd/yyyy", new Date(milliseconds)).toString();
        holder.setpostdate(date);



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
            comments = view.findViewById(R.id.pcomment);
        }

        public void setpostuser(String username) {
            name = view.findViewById(R.id.pname);
            name.setText(username);
        }

        public void setpostdate(String pdate) {
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