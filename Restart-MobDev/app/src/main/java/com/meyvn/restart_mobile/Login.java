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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    public static String authACC;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(auth.getCurrentUser()!=null) {
            checkNotif();
            getAcc(auth.getCurrentUser().getUid());
        }
        else {
            setContentView(R.layout.login);
            TextView forgotPass = findViewById(R.id.forgotPass);
            EditText email = findViewById(R.id.EmailAddress);
            EditText password = findViewById(R.id.Password);
            Button submit = findViewById(R.id.SignInButton);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            forgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),ForgotPassword.class);
                    startActivity(i);
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(email.getText().toString().isEmpty() || email.getText().toString().isEmpty())) {
                        auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful())
                                   {
                                       String UID = task.getResult().getUser().getUid();
                                       checkNotif();
                                       getAcc(UID);
                                   }
                                   else
                                       Toast.makeText(getApplicationContext(),"Invalid Credentials~!",Toast.LENGTH_LONG).show();
                                    }
                                });


                    }//if
                    else
                        Toast.makeText(getApplicationContext(), "Enter all fields!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void checkNotif()
    {
        System.out.println("HELLO");
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

    public void getAcc(String UID)
    {
        Intent patient = new Intent(getApplicationContext(),PatientMainMenu.class);
        Intent alumni = new Intent(getApplicationContext(),AlumniMainMenu.class);
        FirebaseFirestore.getInstance().collection("Accounts")
                .document(UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot ds = task.getResult();
                        Account acc  = ds.toObject(Account.class);
                        if(acc.getRole().equals("patient")) {
                            storedAcc = acc;
                            acc.setID(auth.getCurrentUser().getUid());
                            authACC = auth.getCurrentUser().getUid();
                            checkAssessment(patient);
                        }
                        else if (acc.getRole().equals("alumni")) {
                            acc.setID(auth.getCurrentUser().getUid());
                            storedAcc = acc;
                            authACC = auth.getCurrentUser().getUid();
                            startActivity(alumni);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Account can't be logged in the Mobile APP", Toast.LENGTH_LONG).show();
                            auth.signOut();
                        }
                    }
                    }
                });
    }

}