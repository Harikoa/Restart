package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.Account;

import java.util.ArrayList;

public class AlumniMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumni_main_menu);
        updateInfo();
        TextView welcome = findViewById(R.id.alumniWelcome);
        welcome.setText("Welcome! " + Login.storedAcc.getFirstName() + " " + Login.storedAcc.getLastName());
        Button logout = findViewById(R.id.alumniLogout);
        Button messages = findViewById(R.id.alumniMessage);
        SharedPreferences prf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
        SharedPreferences.Editor edit = prf.edit();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.remove("Account");
                edit.apply();
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewMessages.class);
                startActivity(i);
            }
        });
    }
    public void updateInfo()
    {
        Gson gson = new Gson();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        String email = Login.authACC;
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        System.out.println("token"+ s);
                        fs.collection("Accounts").document(email).update("fcm",s);
                        fs.collection("Accounts").document(email)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Login.storedAcc = documentSnapshot.toObject(Account.class);
                                        String JSON = gson.toJson(Login.storedAcc,Account.class);
                                        SharedPreferences spf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
                                        SharedPreferences.Editor edit = spf.edit();
                                        edit.putString("Account",JSON);
                                        edit.apply();
                                    }
                                });
                    }
                });
    }

}