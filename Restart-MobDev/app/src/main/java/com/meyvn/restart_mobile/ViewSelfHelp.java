package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class ViewSelfHelp extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_self_help);

        ImageButton back = findViewById(R.id.selfBack);
        TextView author = findViewById(R.id.motivationalAuthor);
        TextView content = findViewById(R.id.motivationalQuote);

        FirebaseFirestore.getInstance().collection("MotivQuotes")
                .orderBy("timeAccessed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    DocumentSnapshot ds = task.getResult().getDocuments().get(0);
                    author.setText("- " + ds.get("author").toString());
                    content.setText(ds.get("body").toString());
                    ds.getReference().update("timeAccessed",new Date());
                    }
                });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button actsuggest = findViewById(R.id.viewActSuggest);
        actsuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewSuggestedActivities.class);
                startActivity(i);
            }
        });
    }
}