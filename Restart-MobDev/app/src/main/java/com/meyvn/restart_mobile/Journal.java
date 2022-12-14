package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.JournalPojo;

import java.time.LocalDate;
import java.util.ArrayList;

import com.meyvn.restart_mobile.Adapter.JournalAdapter;

public class Journal extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<JournalPojo> pojo;
    JournalAdapter adapter;
    FirebaseFirestore fs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);

        ImageButton back = findViewById(R.id.journalBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView rc = findViewById(R.id.journalRecycler);
        rc.setLayoutManager(new LinearLayoutManager(this));
        fs = FirebaseFirestore.getInstance();
        pojo = new ArrayList<JournalPojo>();
        adapter = new JournalAdapter(this,pojo,this);
        rc.setAdapter(adapter);

        FloatingActionButton create = findViewById(R.id.CreateJournal);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fs.collection("Accounts").document(Login.authACC).collection("Journal")
                        .whereEqualTo("date", LocalDate.now().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful())
                           {
                               if(!task.getResult().getDocuments().isEmpty())
                                   Toast.makeText(getApplicationContext(),"You already have journal for today!",Toast.LENGTH_LONG).show();
                               else
                               {
                                   Intent i = new Intent(getApplicationContext(),MoodTracker.class);
                                   startActivity(i);
                               }
                           }
                            }
                        });

            }
        });
    }

    @Override
    public void onItemclick(int position) {
        Intent i = new Intent(this, ViewJournalEntry.class);
        Gson gson = new Gson();
        JournalPojo poj = pojo.get(position);
        String JSON = gson.toJson(poj);
        i.putExtra("JSON",JSON);
        startActivity(i);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        while(!pojo.isEmpty())
            pojo.remove(0);
        fs.collection("Accounts").document(Login.authACC).collection("Journal")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for(DocumentSnapshot ds : value.getDocuments())
                        {
                            JournalPojo jp = ds.toObject(JournalPojo.class);
                            pojo.add(jp);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}