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

import com.meyvn.restart_mobile.Adapter.AccountListAdapter;

public class ViewMessages extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<Account>  array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_messages);
        RecyclerView rc = findViewById(R.id.msgRecycler);
        rc.setLayoutManager(new LinearLayoutManager(this));
        array = new ArrayList<>();
        AccountListAdapter adapter = new AccountListAdapter(this,array,this);
        rc.setAdapter(adapter);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        List<String> email = Login.storedAcc.getConnectedUser();
        fs.collection("Accounts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot ds : task.getResult())
                            {
                                if(email.contains(ds.get("email")))
                                {
                                    array.add(ds.toObject(Account.class));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
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
}