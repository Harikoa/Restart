package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.Adapter.SG_Adapter;
import com.meyvn.restart_mobile.POJO.SG_Model;

import java.security.acl.Group;
import java.util.ArrayList;

public class ViewSupportGroups extends AppCompatActivity implements RecyclerViewInterface {
    ArrayList<SG_Model> array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_support_groups);

        ImageButton back = findViewById(R.id.supportBack);
        RecyclerView rc = findViewById(R.id.sgListRecycler);
        ImageButton report = findViewById(R.id.postReport);
       array  = new ArrayList<>();
        SG_Adapter adapter = new SG_Adapter(this,array,this);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        FirebaseFirestore.getInstance().collection("Support Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot ds : task.getResult())
                        {
                            SG_Model model = ds.toObject(SG_Model.class);
                            model.setSgID(ds.getId());
                            if(model.getMembers().contains(Login.storedAcc.getEmail()))
                                array.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    }
                });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onItemclick(int position) {
        Intent i = new Intent(getApplicationContext(),SG_Main.class);
        Gson convert = new Gson();
        String JSON = convert.toJson(array.get(position),SG_Model.class);
        i.putExtra("JSON",JSON);
        startActivity(i);

    }
}