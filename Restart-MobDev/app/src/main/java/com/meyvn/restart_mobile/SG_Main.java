package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.Adapter.SG_PostAdapter;
import com.meyvn.restart_mobile.POJO.SGPostPOJO;
import com.meyvn.restart_mobile.POJO.SG_Model;

import java.util.ArrayList;
import java.util.List;

public class SG_Main extends AppCompatActivity implements RecyclerViewInterface{


    private FirebaseFirestore firestore;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    List<SG_Model> modelList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    SG_PostAdapter adapter;
    ArrayList<SGPostPOJO> array;
    SG_Model pojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg_main);
        Gson gson  = new Gson();
        Intent i = getIntent();
        String JSON = i .getStringExtra("JSON");
        pojo = gson.fromJson(JSON,SG_Model.class);
        recyclerView = findViewById(R.id.sgmainRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        firestore = FirebaseFirestore.getInstance();
        array = new ArrayList<>();
        adapter = new SG_PostAdapter(this,array,this);
        recyclerView.setAdapter(adapter);
        fab = findViewById(R.id.fabp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SG_CreatePost.class);
                i.putExtra("SGID",pojo.getSgID());
                startActivity(i);
            }
        });
    }



    @Override
    public void onItemclick(int position) {
    Intent i = new Intent(getApplicationContext(),view_specific_post.class);
    Gson convert = new Gson();
    String JSON = convert.toJson(array.get(position));
    i.putExtra("JSON",JSON);
    startActivity(i);
    }

    @Override
    protected void onPostResume() {
        while(!array.isEmpty())
            array.remove(0);
        firestore.collection("Support Groups").document(pojo.getSgID()).collection("Post")
                .whereEqualTo("reported",false)
                .orderBy("datePosted", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot ds : task.getResult())
                            {
                                SGPostPOJO pojo = ds.toObject(SGPostPOJO.class);
                                pojo.setPostID(ds.getId());
                                array.add(pojo);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        super.onPostResume();
    }
}