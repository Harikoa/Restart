package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    RecyclerView rc;
    SG_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_support_groups);

        ImageButton back = findViewById(R.id.supportBack);

        ImageButton report = findViewById(R.id.postReport);
       array  = new ArrayList<>();
      adapter= new SG_Adapter(this,array,this);
        rc= findViewById(R.id.sgListRecycler);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);

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
        SG_Model model = array.get(position);
        if(model.getNewmem().contains(Login.authACC))
        {
            model.getNewmem().remove(Login.authACC);
            FirebaseFirestore.getInstance().collection("Support Groups").document(model.getSgID()).update("newmem",model.getNewmem());
        }
        i.putExtra("JSON",JSON);
        startActivity(i);
        findViewById(R.id.view_supportGroups).invalidate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
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
                                if(model.getMembers().contains(Login.authACC))
                                    array.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}