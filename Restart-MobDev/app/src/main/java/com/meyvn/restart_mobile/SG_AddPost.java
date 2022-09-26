package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SG_AddPost extends AppCompatActivity {

    private EditText addCaption;
    private Button addPostbtn;
    private ProgressBar progressb;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_add_post);

        addPostbtn = findViewById(R.id.save_post_btn);
        addCaption = findViewById(R.id.caption);

        progressb = findViewById(R.id.progressBar);
        progressb.setVisibility(View.INVISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();



        }
    }
