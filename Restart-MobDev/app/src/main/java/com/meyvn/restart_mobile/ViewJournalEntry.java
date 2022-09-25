package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.JournalPojo;

import java.time.LocalDate;

public class ViewJournalEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_journal_entry);
        TextView Date = findViewById(R.id.journalEntryDate);
        TextView body = findViewById(R.id.journalEntryBody);
        Button edit = findViewById(R.id.editJournal);
        Intent i = getIntent();
        String JSON= i.getStringExtra("JSON");
        Gson convert = new Gson();
        JournalPojo pojo = convert.fromJson(JSON,JournalPojo.class);
        Date.setText(pojo.getDate());
        body.setText(pojo.getJournalEntry());
        ImageButton back = findViewById(R.id.journalEntryBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LocalDate date = LocalDate.parse(pojo.getDate());
        if(date.equals(LocalDate.now()))
            edit.setVisibility(View.VISIBLE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),editJournal.class);
                i.putExtra("JSON",JSON);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    }
}