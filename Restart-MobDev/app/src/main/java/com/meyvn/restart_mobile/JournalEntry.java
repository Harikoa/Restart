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
                map.put("Journal Entry",journal.getText().toString());
                map.put("Mood",i.getStringExtra("mood"));
                map.put("SubstanceIntensity",i.getIntExtra("intensity",-1));
                map.put("SubstanceFrequency",i.getIntExtra("freq",-1));
                map.put("SubstanceLength",i.getIntExtra("length",-1));
                map.put("Substance number",i.getIntExtra("number",-1));
            db.collection("Accounts").document("restart@gmail.com").collection("Journal").document(""+LocalDate.now())
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
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