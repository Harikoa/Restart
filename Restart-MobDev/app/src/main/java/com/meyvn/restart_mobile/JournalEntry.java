package com.meyvn.restart_mobile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class JournalEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_entry);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText journal = findViewById(R.id.journalEntry);
        Button submit = findViewById(R.id.journalEntrySubmit);
        Map <String,Object> map = new HashMap<>();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
                Intent i = getIntent();
                map.put("date", ""+LocalDate.now());
                map.put("journalEntry",journal.getText().toString());
                map.put("mood",i.getStringExtra("mood"));
                map.put("substanceIntensity",i.getIntExtra("intensity",-1));
                map.put("substanceFrequency",i.getIntExtra("freq",-1));
                map.put("substanceLength",i.getIntExtra("length",-1));
                map.put("substanceNumber",i.getIntExtra("number",-1));
                map.put("important",false);
            db.collection("Accounts").document(Login.authACC).collection("Journal").document(""+LocalDate.now())
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                            Intent in;

                            if(i.getStringExtra("mood").equals("Very Sad")) {
                                in = new Intent(getApplicationContext(), PHQ9Questionnaire.class);
                                in.putExtra("isMonthly",false);
                            }
                            else
                                in = new Intent(getApplicationContext(),Journal.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(in);
                             finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
            }
        });

    }
}