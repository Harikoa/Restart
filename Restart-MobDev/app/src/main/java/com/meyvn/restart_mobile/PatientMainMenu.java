package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.List;

public class PatientMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_main_menu);
        Gson gson = new Gson();
        SharedPreferences prf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
        TextView welcome = findViewById(R.id.welcomeText);

        welcome.setText("Welcome! " + Login.storedAcc.getFirstName() + " " + Login.storedAcc.getLastName());
        SharedPreferences.Editor edit = prf.edit();
        Button data = findViewById(R.id.viewData);
        Button logout  = findViewById(R.id.logoutButton);
        Button journals = findViewById(R.id.viewJournals);
        Button tasks = findViewById(R.id.viewTasks);
        Button forums = findViewById(R.id.viewForum);
        Button drug = findViewById(R.id.drugTestPatient);

        Button selfhelp = findViewById(R.id.viewSelfhelp);
        Button profile = findViewById(R.id.viewProfile);
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
                CollectionReference crf = ref.collection("Accounts").document(Login.storedAcc.getEmail()).collection("DrugTest");
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



    }
}