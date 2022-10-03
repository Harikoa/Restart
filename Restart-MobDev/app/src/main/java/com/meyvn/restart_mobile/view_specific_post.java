package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.Adapter.commentAdapter;
import com.meyvn.restart_mobile.POJO.SGPostPOJO;
import com.meyvn.restart_mobile.POJO.commentPOJO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class view_specific_post extends AppCompatActivity implements RecyclerViewInterface {
    ArrayList<commentPOJO> array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_specific_post);
        TextView Title = findViewById(R.id.specific_Title);
        TextView content = findViewById(R.id.specific_Content);
        Intent i = getIntent();
        Gson convert = new Gson();
        SGPostPOJO pojo = convert.fromJson(i.getStringExtra("JSON"),SGPostPOJO.class);
        Title.setText(pojo.getTitle() + " by " + pojo.getUserNickname());
        content.setText(pojo.getContent());
        EditText comment = findViewById(R.id.writeComment);
        ImageButton send = findViewById(R.id.commentSend);
        RecyclerView rc = findViewById(R.id.comment_Recycler);
        array = new ArrayList<>();
        commentAdapter adapter = new commentAdapter(this,array,this);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment.getText().toString().isEmpty())
                    return;
                else
                {
                    commentPOJO poj = new commentPOJO();
                    poj.setDatePosted(new Date());
                    poj.setCommentContent(comment.getText().toString().trim());
                    poj.setNickName(pojo.getUserNickname());
                    poj.setUserEmail(pojo.getUserID());
                    poj.setReported(false);
                    FirebaseFirestore.getInstance().collection("Support Groups").document(pojo.getSGID()).collection("Post")
                            .document(pojo.getPostID()).collection("Comments").add(poj)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(),"Comment Successfully posted!",Toast.LENGTH_LONG).show();
                                }
                            });
                    comment.setText("");
                }
            }
        });
        FirebaseFirestore.getInstance().collection("Support Groups").document(pojo.getSGID()).collection("Post")
                .document(pojo.getPostID()).collection("Comments")
                .orderBy("datePosted", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error==null && value!=null)
                    {
                        for(DocumentChange ds : value.getDocumentChanges())
                        {
                            if(ds.getType().equals(DocumentChange.Type.ADDED)) {
                                commentPOJO poj = ds.getDocument().toObject(commentPOJO.class);
                                if (!poj.isReported() && !array.contains(poj))
                                    array.add(poj);
                            }
                        }

                        adapter.notifyDataSetChanged();
                        if(array.size()>0)
                            rc.smoothScrollToPosition(array.size() - 1);

                    }

                    }
                });
    }

    @Override
    public void onItemclick(int position) {

    }
}