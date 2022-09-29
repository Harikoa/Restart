package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SG_Main extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private RecyclerView rec;
    private FloatingActionButton fab;
    private Toolbar mainToolbar;
    private RecyclerView recyclerView;

    List<SG_Model> modelList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    SG_PostAdapter adapter;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);


        recyclerView = findViewById(R.id.recycleview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        showData();




        firestore = FirebaseFirestore.getInstance();
        rec = findViewById(R.id.recycleview);
        fab = findViewById(R.id.fabp);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SG_Main.this , SG_Post.class));
            }
        });
    }

    private void showData() {
        firestore.collection("Support Groups").document("SGForAlcoholics").collection("Post")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                pd.dismiss();
                for (DocumentSnapshot doc: task.getResult()) {
                 SG_Model model = new SG_Model(doc.getString("id"),
                         doc.getString("description"),
                         doc.getString("title"),
                         doc.getString("name"));
                 modelList.add(model);
                }
                adapter = new SG_PostAdapter(SG_Main.this,modelList);

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