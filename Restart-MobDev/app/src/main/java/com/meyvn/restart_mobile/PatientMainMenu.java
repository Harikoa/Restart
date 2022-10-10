package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.Account;

import java.util.List;

public class PatientMainMenu extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_main_menu);
        TextView welcome = findViewById(R.id.welcomeText);
        updateInfo();
        welcome.setText("Welcome! " + Login.storedAcc.getFirstName() + " " + Login.storedAcc.getLastName());
        Button data = findViewById(R.id.viewData);
        Button logout  = findViewById(R.id.logoutButton);
        Button journals = findViewById(R.id.viewJournals);
        Button tasks = findViewById(R.id.viewTasks);
        Button forums = findViewById(R.id.viewForum);
        Button drug = findViewById(R.id.drugTestPatient);
        Button messages = findViewById(R.id.messages);
        Button selfhelp = findViewById(R.id.viewSelfhelp);
        Button profile = findViewById(R.id.viewProfile);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.storedAcc.setFCM(null);
                FirebaseFirestore.getInstance().collection("Accounts").document(auth.getCurrentUser().getUid())
                        .set(Login.storedAcc, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        auth.signOut();

                        Intent i = new Intent(getApplicationContext(),Login.class);
                        startActivity(i);
                        finish();
                    }
                });

            }
        });
        journals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Journal.class);
                startActivity(i);
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewTasks.class);
                startActivity(i);
            }
        });

        forums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewSupportGroups.class);
                startActivity(i);
            }
        });

        drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore ref = FirebaseFirestore.getInstance();
                CollectionReference crf = ref.collection("Accounts").document(Login.authACC).collection("DrugTest");
            Query query =   crf.whereEqualTo("completion",false);
                                query.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot qr =task.getResult();
                                        if (qr.isEmpty())
                                            Toast.makeText(getApplicationContext(),"NO NEW DRUG REQUEST FROM PHYSICIAN",Toast.LENGTH_LONG).show();
                                        else
                                        {
                                            List<DocumentSnapshot> ds = task.getResult().getDocuments();
                                            String docID = ds.get(0).getId();

                                            Intent i = new Intent(getApplicationContext(),ViewDrugTest.class);
                                            i.putExtra("drugID",docID);
                                            startActivity(i);

                                        }
                                    }
                                });


            }
        });
        selfhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewSelfHelp.class);
                startActivity(i);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewProfile.class);
                startActivity(i);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewMessages.class);
                startActivity(i);
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewJournalData.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
                        fs.collection("Accounts").document(UID).set(Login.storedAcc,SetOptions.merge())
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