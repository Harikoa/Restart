package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SG_Post extends AppCompatActivity {

    private EditText addCaption;
    private EditText addTitle;
    private Button addPostbtn;
    private ProgressBar progressb;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_post);

        addPostbtn = findViewById(R.id.save_post_btn);
        addCaption = findViewById(R.id.caption);
        addTitle = findViewById(R.id.title);


        progressb = findViewById(R.id.progressBar);
        progressb.setVisibility(View.INVISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        addCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = addTitle.getText().toString().trim();
            }
        });

    }
}
