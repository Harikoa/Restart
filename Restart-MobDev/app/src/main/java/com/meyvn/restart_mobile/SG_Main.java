package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SG_Main extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private RecyclerView rec;
    private FloatingActionButton fab;
    private Toolbar mainToolbar;
    private RecyclerView recyclerView;
    private SG_PostAdapter adapter;
    private List<SG_PostModel> list;
    private Query query;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(SG_Main.this));

        adapter = new SG_PostAdapter(SG_Main.this, list);
        list = new ArrayList<>();
        recyclerView.setAdapter(adapter);



        firestore = FirebaseFirestore.getInstance();
        rec = findViewById(R.id.recycleview);
        fab = findViewById(R.id.fabp);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SG_Main.this , SG_Post.class));
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Boolean isBottom = !recyclerView.canScrollVertically(1);
                if (isBottom)
                    Toast.makeText(SG_Main.this, "Reached Bottom", Toast.LENGTH_SHORT);
            }
        });
        query = firestore.collection("Support Groups").document("SGForAlcoholics").collection("Post").orderBy("time", Query.Direction.DESCENDING);
        listenerRegistration = query.addSnapshotListener(SG_Main.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : value.getDocumentChanges()){
                   if (doc.getType() == DocumentChange.Type.ADDED){
                       SG_PostModel post = doc.getDocument().toObject(SG_PostModel.class);
                       list.add(post);
                       adapter.notifyDataSetChanged();
                   }else {
                       adapter.notifyDataSetChanged();
                   }
                }
                listenerRegistration.remove();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.my_profile){
            startActivity(new Intent(SG_Main.this , ViewProfile.class));
        }else if(item.getItemId() == R.id.main_menu){
            startActivity(new Intent(SG_Main.this , PatientMainMenu.class));
            finish();
        }
        return true;
    }
}