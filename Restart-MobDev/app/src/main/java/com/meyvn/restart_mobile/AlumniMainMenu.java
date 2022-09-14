package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AlumniMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumni_main_menu);
        TextView welcome = findViewById(R.id.alumniWelcome);
        welcome.setText("Welcome! " + Login.storedAcc.getFirstName() + " " + Login.storedAcc.getLastName());
        Button logout = findViewById(R.id.alumniLogout);
        SharedPreferences prf = getSharedPreferences("AccountLogged",MODE_PRIVATE);
        SharedPreferences.Editor edit = prf.edit();
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
    }
}