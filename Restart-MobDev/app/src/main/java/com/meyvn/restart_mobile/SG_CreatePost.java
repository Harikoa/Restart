package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.meyvn.restart_mobile.POJO.SGPostPOJO;


import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SG_CreatePost extends AppCompatActivity {

    private EditText addCaption;
    private EditText addTitle;
    private Button addPostbtn;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_createpost);
        addPostbtn = findViewById(R.id.save_post_btn);
        addCaption = findViewById(R.id.caption);
        addTitle = findViewById(R.id.title);
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        ImageButton back = findViewById(R.id.sg_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addPostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = addTitle.getText().toString().trim();
                String caption = addCaption.getText().toString().trim();
                if(Login.storedAcc.getLastSuspensionDay()!=null && Login.storedAcc.getLastSuspensionDay().before(new Date())) {
                    if (!(title.isEmpty() || caption.isEmpty()))
                        uploadData(title, caption);
                    else
                        Toast.makeText(getApplicationContext(), "Input all fields", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You are currently suspended", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
    private void uploadData(String title, String caption) {

        Intent i = getIntent();
        String SGID =  i.getStringExtra("SGID");
        SGPostPOJO pojo = new SGPostPOJO();
        pojo.setTitle(title);
        pojo.setContent(caption);
        pojo.setDatePosted(new Date());
        pojo.setSGID(SGID);
        pojo.setUserNickname(Login.storedAcc.getNickname());
        pojo.setReported(false);
        pojo.setUserID(Login.storedAcc.getEmail());
        firestore.collection("Support Groups").document(SGID).collection("Post").add(pojo)
              .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentReference> task) {
                      Toast.makeText(SG_CreatePost.this, "Posted", Toast.LENGTH_SHORT).show();
                     finish();
                  }
              });
    }
}
