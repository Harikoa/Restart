package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;

public class SubsCravingQuestionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subs_craving_questionnaire);
        Button submit= findViewById(R.id.submitSubstance);
        RadioGroup rgIntensity = findViewById(R.id.substanceIntensity);
        RadioGroup rgFreq = findViewById(R.id.substanceFreq);
        RadioGroup rgLength = findViewById(R.id.substanceLength);
        EditText txt = findViewById(R.id.substanceNumber);
        Intent i = getIntent();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton intensity = findViewById(rgIntensity.getCheckedRadioButtonId());
                RadioButton freq = findViewById(rgFreq.getCheckedRadioButtonId());
                RadioButton length = findViewById(rgLength.getCheckedRadioButtonId());
                String number = txt.getText().toString();
                i.setClass(getApplicationContext(),JournalEntry.class);
               i.putExtra("intensity",intensity.getText());
               i.putExtra("freq",freq.getText());
               i.putExtra("length",length.getText());
               i.putExtra("number",number);
               startActivity(i);
            }
        });



    }
}