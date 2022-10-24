package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.meyvn.restart_mobile.Adapter.AccountListAdapter;

public class ViewMessages extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<Account>  array;
    int ctr = 0;
    FirebaseFirestore fs;
    ArrayList<String> arr;
    AccountListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_messages);
        RecyclerView rc = findViewById(R.id.msgRecycler);
        rc.setLayoutManager(new LinearLayoutManager(this));
        array = new ArrayList<>();
       arr= new ArrayList<>();
        adapter= new AccountListAdapter(this,array,this);
        rc.setAdapter(adapter);
       fs = FirebaseFirestore.getInstance();
        if(Login.storedAcc.getRole().equals("patient"))
        {
        fs.collection("PhyLink").whereEqualTo("patient",Login.storedAcc.getID())
                .orderBy("phy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.isSuccessful())
                         {
                             System.out.println(Login.storedAcc.getID());
                             for(DocumentSnapshot ds : task.getResult())
                             {
                                 arr.add(ds.get("phy").toString());
                             }
                            doStuff();
                         }
                    }
                });
        fs.collection("AlumniLink").whereEqualTo("patient",Login.storedAcc.getID())
                .orderBy("al")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot ds : task.getResult())
                            {
                                arr.add(ds.get("al").toString());
                            }
                            doStuff();
                        }
                    }
                });
        }
        else
        {
            fs.collection("AlumniLink").whereEqualTo("al",Login.storedAcc.getID())
                    .orderBy("patient")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot ds : task.getResult())
                                {
                                    arr.add(ds.get("patient").toString());
                                }
                                doStuff();
                                doStuff();
                            }
                        }
                    });
        }



    }

    @Override
    public void onItemclick(int position) {
        Intent i = new Intent(getApplicationContext(),chatActivity.class);
        Account acc = array.get(position);
        Gson convert = new Gson();
        String JSON = convert.toJson(acc,Account.class);
        i.putExtra("JSON",JSON);
        startActivity(i);
    }
    public void doStuff()
    {
        ctr++;
        if(ctr==2)
        {
            if(arr.isEmpty())
                arr.add("");
            fs.collection("Accounts").whereIn("id",arr)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot ds : task.getResult())
                                {
                                    array.add(ds.toObject(Account.class));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

        }
    }


}