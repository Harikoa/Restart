package com.meyvn.restart_mobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.JournalPojo;

import java.util.ArrayList;

public class Journal extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<JournalPojo> pojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);
       FloatingActionButton create = findViewById(R.id.CreateJournal);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MoodTracker.class);
                startActivity(i);
            }
        });
        ImageButton back = findViewById(R.id.journalBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView rc = findViewById(R.id.journalRecycler);
        rc.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        pojo = new ArrayList<JournalPojo>();
        JournalAdapter adapter = new JournalAdapter(this,pojo,this);
        rc.setAdapter(adapter);
        fs.collection("Accounts").document(Login.storedAcc.getEmail()).collection("Journal")
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

    @Override
    public void onItemclick(int position) {
        Intent i = new Intent(this, ViewJournalEntry.class);
        Gson gson = new Gson();
        JournalPojo poj = pojo.get(position);
        String JSON = gson.toJson(poj);
        i.putExtra("JSON",JSON);
        startActivity(i);
    }
}