package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
        Button logout  = findViewById(R.id.logoutButton);
        Button journals = findViewById(R.id.viewJournals);
        Button drug = findViewById(R.id.drugTestPatient);
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

        drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewDrugTest.class);
                startActivity(i);
            }
        });
    }
}