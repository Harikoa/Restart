package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.meyvn.restart_mobile.POJO.JournalPojo;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.util.ArrayList;


public class ViewJournalData extends AppCompatActivity {
    ArrayList<LocalDate> ld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_journal_data);
        Button mood = findViewById(R.id.moodData);
        Button urge = findViewById(R.id.urgeData);
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewMoodTrackerData.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        urge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),view_urge_data.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    setMarkedDates();





    }
    public void setMarkedDates()
    {
        MaterialCalendarView mcv = findViewById(R.id.calendarView);
        mcv.setEnabled(false);
        ld = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Accounts").document(Login.authACC)
                .collection("Journal").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot ds: task.getResult())
                            {
                                JournalPojo pojo = ds.toObject(JournalPojo.class);
                                ld.add(LocalDate.parse(pojo.getDate()));
                            }
                            if(!ld.isEmpty())
                            {
                                for(LocalDate l: ld)
                                {
                                    mcv.setDateSelected(CalendarDay.from(l.getYear(),l.getMonthValue(),l.getDayOfMonth()), true);
                                }
                            }
                        }
                    }
                });
    }
}