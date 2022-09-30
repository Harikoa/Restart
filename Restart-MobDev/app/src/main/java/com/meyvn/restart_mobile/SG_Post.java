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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnCompleteListener;


import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

public class SG_Post extends AppCompatActivity {

    private EditText addCaption;
    private EditText addTitle;
    private Button addPostbtn;
    private ProgressDialog pd;
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

        ImageButton back = findViewById(R.id.sg_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        Map<String, Object> post = new HashMap<>();
        post.put("user", Login.storedAcc.getNickname());
        post.put("title",title);
        post.put("description", caption);
        post.put("time", FieldValue.serverTimestamp());

        firestore.collection("Support Groups").document("SGForAlcoholics").collection("Post").add(post)
              .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentReference> task) {
                      pd.dismiss();
                      Toast.makeText(SG_Post.this, "Posted", Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(getApplicationContext(),SG_Main.class);
                      startActivity(i);
                  }
              });
    }
}
