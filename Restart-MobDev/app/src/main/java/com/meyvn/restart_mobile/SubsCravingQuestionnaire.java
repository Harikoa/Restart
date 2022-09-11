package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
                if(!number.isEmpty()) {
                    i.setClass(getApplicationContext(), JournalEntry.class);
                    i.putExtra("intensity", Integer.parseInt(intensity.getText().toString()));
                    i.putExtra("freq", Integer.parseInt(freq.getText().toString()));
                    i.putExtra("length", Integer.parseInt(length.getText().toString()));
                    i.putExtra("number", Integer.parseInt(number));
                    startActivity(i);
                }
                else
                    Toast.makeText(getApplicationContext(),"Enter the number of times of cravings for a drug",Toast.LENGTH_LONG).show();
            }
        });
        ImageButton back = findViewById(R.id.subsBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}