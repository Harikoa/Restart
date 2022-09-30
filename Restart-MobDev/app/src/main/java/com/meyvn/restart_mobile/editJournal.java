package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.JournalPojo;

public class editJournal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_journal);
        Intent i = getIntent();
        String JSON = i.getStringExtra("JSON");
        Gson convert = new Gson();
        JournalPojo pojo =convert.fromJson(JSON,JournalPojo.class);
        TextView date = findViewById(R.id.editJournalDate);
        EditText content = findViewById(R.id.editJournalText);
        Button submit = findViewById(R.id.editJournalSubmit);
        String contentb4 = pojo.getJournalEntry();
        date.setText(pojo.getDate());
        content.setText(pojo.getJournalEntry());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pojo.setJournalEntry(content.getText().toString());
                FirebaseFirestore.getInstance().collection("Accounts").document(Login.storedAcc.getEmail()).collection("Journal").document(pojo.getDate())
                        .set(pojo, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                            Toast.makeText(getApplicationContext(),"SUCCESS",Toast.LENGTH_LONG).show();
                        else
                        {
                            Toast.makeText(getApplicationContext(),"UNSUCCESSFUL",Toast.LENGTH_LONG).show();
                            pojo.setJournalEntry(contentb4);
                        }
                        Intent i = new Intent(getApplicationContext(),ViewJournalEntry.class);
                        String J = convert.toJson(pojo);
                        i.putExtra("JSON",J);
                        startActivity(i);
                    }
                })
                ;

            }
        });
    }
}