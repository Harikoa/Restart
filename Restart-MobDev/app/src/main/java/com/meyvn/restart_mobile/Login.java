package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.Notification.notificationWorker;
import com.meyvn.restart_mobile.POJO.Account;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    public static Account storedAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
        Intent patient = new Intent(getApplicationContext(),PatientMainMenu.class);
        Intent alumni = new Intent(getApplicationContext(),AlumniMainMenu.class);
        Gson convert = new Gson();
        if (spf.contains("Account")) {
            String savedJson = spf.getString("Account","{}");
           storedAcc = convert.fromJson(savedJson,Account.class);
            if(storedAcc.getRole().equalsIgnoreCase("patient")) {
                checkNotif();
                checkAssessment(patient);
            }
            else
                startActivity(alumni);
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
                                storedAcc = acc;
                                String JSON = convert.toJson(acc);
                                edit.putString("Account", JSON);
                                edit.apply();
                                if(storedAcc.getRole().equalsIgnoreCase("patient")) {
                                    checkNotif();
                                   checkAssessment(patient);
                                }
                                else if(storedAcc.getRole().equalsIgnoreCase("alumni"))
                                    startActivity(alumni);
                                else {
                                    Toast.makeText(Login.this, "Account can't be logged in the mobile app", Toast.LENGTH_LONG).show();
                                    edit.remove("Account");
                                    edit.apply();
                                }
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
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void checkNotif()
    {
        PeriodicWorkRequest prd = new PeriodicWorkRequest.Builder(notificationWorker.class,1, TimeUnit.DAYS)
                .build()
                ;
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("notificationWork", ExistingPeriodicWorkPolicy.REPLACE,prd);
    }
    public void checkAssessment(Intent pt)
    {
        LocalDate lastAssPlusOne = LocalDate.parse(storedAcc.getLastAssessment());
        lastAssPlusOne = lastAssPlusOne.plusMonths(1);
        LocalDate today = LocalDate.now();
        if(lastAssPlusOne.isBefore(today)|| lastAssPlusOne.equals(today)) {
            pt = new Intent(getApplicationContext(),PHQ9Questionnaire.class);
            pt.putExtra("isMonthly",true);
            pt.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Toast.makeText(getApplicationContext(),"Monthly Assessment",Toast.LENGTH_LONG).show();
        }
        startActivity(pt);
    }


}