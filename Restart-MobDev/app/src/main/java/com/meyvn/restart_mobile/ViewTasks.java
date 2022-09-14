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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.JournalPojo;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;

import java.util.ArrayList;

public class ViewTasks extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<ViewTaskPojo> pojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_tasks);

        ImageButton back = findViewById(R.id.tasksBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView rc = findViewById(R.id.taskRecycler);
        rc.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        pojo = new ArrayList<ViewTaskPojo>();
        TaskAdapter adapter = new TaskAdapter(this,pojo,this);
        rc.setAdapter(adapter);
        fs.collection("Accounts").document(Login.storedAcc.getEmail()).collection("Task")
                .whereEqualTo("isComplete",false)
                .orderBy("taskDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot ds : task.getResult()) {
                                ViewTaskPojo poj = ds.toObject(ViewTaskPojo.class);
                                pojo.add(poj);

                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            System.out.println("Task unsuccessful");
                    }
                });
    }


        @Override
        public void onItemclick(int position) {
            Intent i = new Intent(this, ViewTaskEntry.class);
            Gson gson = new Gson();
            ViewTaskPojo poj = pojo.get(position);
            String JSON = gson.toJson(poj);
            i.putExtra("JSON",JSON);
            startActivity(i);
    }
}