package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
        Intent in = new Intent(getApplicationContext(),PatientMainMenu.class);
        Gson convert = new Gson();
        if (spf.contains("Account")) {
            startActivity(in);
        }
        setContentView(R.layout.login);
        EditText email = findViewById(R.id.EmailAddress);
        EditText password = findViewById(R.id.Password);
        Button submit = findViewById(R.id.SignInButton);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference doc = db.collection("Accounts").document(email.getText().toString());
                doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String pw =document.get("password").toString();
                            
                            if(password.getText().toString().equals(pw))
                            {

                                SharedPreferences.Editor edit = spf.edit();
                                Account acc = document.toObject(Account.class);
                                String JSON = convert.toJson(acc);
                                System.out.println(JSON);
                                edit.putString("Account", JSON);
                                edit.apply();
                                startActivity(in);
                            }
                            else
                                Toast.makeText(Login.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Login.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences spf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
        Intent in = new Intent(getApplicationContext(),PatientMainMenu.class);
       
        if (spf.contains("Account")) {
            startActivity(in);
        }
    }
}