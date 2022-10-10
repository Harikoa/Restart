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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.Account;

import java.util.ArrayList;

public class AlumniMainMenu extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
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
        Button sg = findViewById(R.id.alumniSupportGroup);
        Button prof = findViewById(R.id.alumniViewProfile);
        sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewSupportGroups.class);
                startActivity(i);
            }
        });
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewProfile.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
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
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        System.out.println("token"+ s);
                        Login.storedAcc.setFCM(s);
                        Login.storedAcc.setID(UID);
                        fs.collection("Accounts").document(UID).set(Login.storedAcc, SetOptions.merge())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            if(!Login.storedAcc.isActivated())
                                            {
                                                Toast.makeText(getApplicationContext(),"Account is Deactivated",Toast.LENGTH_LONG).show();
                                                auth.signOut();
                                                finish();
                                            }
                                        }
                                    }
                                });
                    }
                });

    }
}