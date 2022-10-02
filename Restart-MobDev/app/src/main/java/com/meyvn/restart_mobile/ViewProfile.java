package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meyvn.restart_mobile.POJO.Account;

import java.time.LocalDate;
import java.util.Date;

public class ViewProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        setText();
        Button edit = findViewById(R.id.editaccount);
        ImageButton back = findViewById(R.id.profileBack);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setText();
    }
    public void setText()
    {
        Account acc = Login.storedAcc;
        TextView fn = findViewById(R.id.viewProfileFN);
        TextView ln = findViewById(R.id.viewProfileLN);
        TextView mn = findViewById(R.id.viewProfileMN);
        TextView bday = findViewById(R.id.viewProfileBD);
        TextView nickname = findViewById(R.id.viewProfileNickname);
        TextView email = findViewById(R.id.viewProfileEmail);
        TextView sex = findViewById(R.id.viewSex);
        TextView contact = findViewById(R.id.viewContact);
        LinearLayout layout = findViewById(R.id.lastAssessmentVP);
        fn.setText(acc.getFirstName());
        ln.setText(acc.getLastName());
        mn.setText(acc.getMiddleName());
        bday.setText("" + acc.getBirthDay());
        nickname.setText(acc.getNickname());
        email.setText(acc.getEmail());
        sex.setText(acc.getSex());
        contact.setText(acc.getContact());
        if(acc.getRole().equals("patient")) {
            layout.setVisibility(View.VISIBLE);
            TextView assessment = findViewById(R.id.viewLastAssessment);
            assessment.setText(acc.getLastAssessment());
        }
    }

}