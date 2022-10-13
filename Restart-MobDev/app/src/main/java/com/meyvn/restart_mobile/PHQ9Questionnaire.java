package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PHQ9Questionnaire extends AppCompatActivity {
    public Map <String, Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phq9_questionnaire);

        Button submit = findViewById(R.id.phq9Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendAssessment();
            }
        });

    }
    public void sendAssessment()
    {
        map = new HashMap<>();
        RadioGroup phq1 = findViewById(R.id.phq1);
        RadioGroup phq2 = findViewById(R.id.phq2);
        RadioGroup phq3 = findViewById(R.id.phq3);
        RadioGroup phq4 = findViewById(R.id.phq4);
        RadioGroup phq5 = findViewById(R.id.phq5);
        RadioGroup phq6 = findViewById(R.id.phq6);
        RadioGroup phq7 = findViewById(R.id.phq7);
        RadioGroup phq8 = findViewById(R.id.phq8);
        RadioGroup phq9 = findViewById(R.id.phq9);
        int id1=phq1.getCheckedRadioButtonId();
        int id2=phq2.getCheckedRadioButtonId();
        int id3=phq3.getCheckedRadioButtonId();
        int id4=phq4.getCheckedRadioButtonId();
        int id5=phq5.getCheckedRadioButtonId();
        int id6=phq6.getCheckedRadioButtonId();
        int id7=phq7.getCheckedRadioButtonId();
        int id8=phq8.getCheckedRadioButtonId();
        int id9=phq9.getCheckedRadioButtonId();
        int total = 0;
        switch(id1)
        {
            case R.id.phqlvl1:
                map.put("phq1",0);
                break;
            case R.id.phqlvl2:
                map.put("phq1",1);
                total++;
                break;
            case R.id.phqlvl3:
                map.put("phq1",2);
                total+=2;
                break;
            case R.id.phqlvl4:
                map.put("phq1",3);
                total+=3;
                break;
        }
        switch(id2)
        {
            case R.id.phqlvl21:
                map.put("phq2",0);
                break;
            case R.id.phqlvl22:
                map.put("phq2",1);
                total++;
                break;
            case R.id.phqlvl23:
                map.put("phq2",2);
                total+=2;
                break;
            case R.id.phqlvl24:
                map.put("phq2",3);
                total+=3;
                break;
        }
        switch(id3)
        {
            case R.id.phqlvl31:
                map.put("phq3",0);
                break;
            case R.id.phqlvl32:
                map.put("phq3",1);
                total++;
                break;
            case R.id.phqlvl33:
                map.put("phq3",2);
                total+=2;
                break;
            case R.id.phqlvl34:
                map.put("phq3",3);
                total+=3;
                break;
        }
        switch(id4)
        {
            case R.id.phqlvl41:
                map.put("phq4",0);
                break;
            case R.id.phqlvl42:
                map.put("phq4",1);
                total++;
                break;
            case R.id.phqlvl43:
                map.put("phq4",2);
                total+=2;
                break;
            case R.id.phqlvl44:
                map.put("phq4",3);
                total+=3;
                break;
        }
        switch(id5)
        {
            case R.id.phqlvl51:
                map.put("phq5",0);
                break;
            case R.id.phqlvl52:
                map.put("phq5",1);
                total++;
                break;
            case R.id.phqlvl53:
                map.put("phq5",2);
                total+=2;
                break;
            case R.id.phqlvl54:
                map.put("phq5",3);
                total+=3;
                break;
        }
        switch(id6)
        {
            case R.id.phqlvl61:
                map.put("phq6",0);
                break;
            case R.id.phqlvl62:
                map.put("phq6",1);
                total++;
                break;
            case R.id.phqlvl63:
                map.put("phq6",2);
                total+=2;
                break;
            case R.id.phqlvl64:
                map.put("phq6",3);
                total+=3;
                break;
        }
        switch(id7)
        {
            case R.id.phqlvl71:
                map.put("phq7",0);
                break;
            case R.id.phqlvl72:
                map.put("phq7",1);
                total++;
                break;
            case R.id.phqlvl73:
                map.put("phq7",2);
                total+=2;
                break;
            case R.id.phqlvl74:
                map.put("phq7",3);
                total+=3;
                break;
        }
        switch(id8)
        {
            case R.id.phqlvl81:
                map.put("phq8",0);
                break;
            case R.id.phqlvl82:
                map.put("phq8",1);
                total++;
                break;
            case R.id.phqlvl83:
                map.put("phq8",2);
                total+=2;
                break;
            case R.id.phqlvl84:
                map.put("phq8",3);
                total+=3;
                break;
        }
        switch(id9)
        {
            case R.id.phqlvl91:
                map.put("phq9",0);
                break;
            case R.id.phqlvl92:
                map.put("phq9",1);
                total++;
                break;
            case R.id.phqlvl93:
                map.put("phq9",2);
                total+=2;
                break;
            case R.id.phqlvl94:
                map.put("phq9",3);
                total+=3;
                break;
        }
        Intent in = getIntent();
        boolean monthly = in.getBooleanExtra("isMonthly",false);
        map.put("total",total);
        map.put("dateAccomplished", ""+LocalDate.now());
        map.put("isMonthly", monthly);
        map.put("isInterpreted",false);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("Accounts").document(Login.authACC).collection("Assessment")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"SUCCESS",Toast.LENGTH_LONG).show();

                    }
                });
        if(monthly)
        {
            Login.storedAcc.setLastAssessment(""+LocalDate.now());
            fs.collection("Accounts").document(Login.authACC)
                    .set(Login.storedAcc, SetOptions.merge());

        }
     Intent i = new Intent(getApplicationContext(),PatientMainMenu.class);
    startActivity(i);
    finish();
    }//end  function
}