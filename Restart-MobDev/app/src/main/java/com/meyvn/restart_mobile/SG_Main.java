package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class SG_Main extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseFirestore firestore;
    private RecyclerView rec;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Support Group");

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