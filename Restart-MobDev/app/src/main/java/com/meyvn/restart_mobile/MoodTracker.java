package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MoodTracker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_tracker);
        Button b = findViewById(R.id.moodTrackSubmit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup rd = findViewById(R.id.moodRadioGroup);
                RadioButton rb = findViewById(rd.getCheckedRadioButtonId());
                Intent i = new Intent(getApplicationContext(),SubsCravingQuestionnaire.class);
                i.putExtra("mood",rb.getText());
                startActivity(i);

                ImageButton back = findViewById(R.id.moodtrackerBack);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });
        ImageButton back = findViewById(R.id.moodtrackerBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}