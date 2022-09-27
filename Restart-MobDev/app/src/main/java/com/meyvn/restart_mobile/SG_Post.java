package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnCompleteListener;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class SG_Post extends AppCompatActivity {

    private EditText addCaption;
    private EditText addTitle;
    private Button addPostbtn;
    private Toolbar postToolbar;
    private ProgressDialog pd;
    private ProgressBar pb1,pb2; int i=0;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_post);


        addPostbtn = findViewById(R.id.save_post_btn);
        addCaption = findViewById(R.id.caption);
        addTitle = findViewById(R.id.title);
        pd = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        addCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = addTitle.getText().toString().trim();
                String caption = addCaption.getText().toString().trim();

                uploadData (title, caption);
            }
        });

    }
    private void uploadData(String title, String caption) {
        pd.setTitle("Uploading");
        pd.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> post = new HashMap<>();
        post.put("user", userId);
        post.put("title",title);
        post.put("description", caption);

        firestore.collection("Support Groups").document("SGForAlcoholics").collection("Post").document(id).set(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(SG_Post.this, "Posted", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),SG_Main.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(SG_Post.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }
}
