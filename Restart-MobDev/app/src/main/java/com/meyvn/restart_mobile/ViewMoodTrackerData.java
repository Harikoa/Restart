package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.meyvn.restart_mobile.POJO.JournalPojo;

import java.util.ArrayList;

public class ViewMoodTrackerData extends AppCompatActivity {
    ArrayList<BarEntry> bar;
    BarDataSet set;
    ArrayList<String> label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mood_tracker_data);
        Button journal = findViewById(R.id.journalData);
        Button urge = findViewById(R.id.urgeDataButton);
        label = new ArrayList<>();
        label.add("Very Sad");
        label.add("Sad");
        label.add("Neutral");
        label.add("Happy");
        label.add("Very Happy");

        putValues();
        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewJournalData.class);
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


    }
    public void putValues()
    {
        bar = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Accounts").document(Login.authACC)
                         .collection("Journal").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int vhappy = 0;
                                    int happy = 0;
                                    int neutral =0;
                                    int sad = 0;
                                    int vsad = 0;
                                    int days=0;
                                    for (DocumentSnapshot ds : task.getResult()) {
                                        JournalPojo pojo = ds.toObject(JournalPojo.class);
                                        String mood = pojo.getMood();
                                        switch (mood) {
                                            case "Very Sad":
                                                vsad++;
                                                break;
                                            case "Sad":
                                                sad++;
                                                break;
                                            case "Neutral":
                                                neutral++;
                                                break;
                                            case "Happy":
                                                happy++;
                                                break;
                                            case "Very Happy":
                                                vhappy++;
                                                break;
                                            default:
                                                System.out.println("ERR");
                                        }
                                    }//for
                                    bar.add(new BarEntry(1f,vsad));
                                    bar.add(new BarEntry(2f,sad));
                                    bar.add(new BarEntry(3f,neutral));
                                    bar.add(new BarEntry(4f,happy));
                                    bar.add(new BarEntry(5f,vhappy));
                                    BarChart chart = findViewById(R.id.moodChart);
                                    set = new BarDataSet(bar,"Mood Data");
                                    BarData data = new BarData(set);
                                    chart.setData(data);
                                    chart.getLegend().setEnabled(false);
                                    set.setColors(ColorTemplate.PASTEL_COLORS);
                                    set.setValueTextColor(Color.BLACK);
                                    set.setValueTextSize(16);
                                    chart.getXAxis().setValueFormatter(new ValueFormatter() {
                                        @Override
                                        public String getFormattedValue(float value) {
                                            return label.get((int)value-1);
                                        }
                                    });
                                    chart.getDescription().setEnabled(false);
                                    chart.getXAxis().setTextSize(13);
                                    chart.getXAxis().setDrawGridLines(false);
                                    chart.getAxisRight().setEnabled(false);
                                    chart.getXAxis().setGranularity(1.0f);
                                    chart.notifyDataSetChanged();
                                    chart.invalidate();
                                    TextView t1 = findViewById(R.id.totalVsad);
                                    TextView t2 = findViewById(R.id.totalsad);
                                    TextView t3 = findViewById(R.id.totalNeutral);
                                    TextView t4 = findViewById(R.id.totalHappy);
                                    TextView t5 = findViewById(R.id.totalVhappy);
                                    t1.setText("Total Very Sad days: " + vsad);
                                    t2.setText("Total Sad days: " + sad);
                                    t3.setText("Total Neutral days: " + neutral);
                                    t4.setText("Total Happy days: " + happy);
                                    t5.setText("Total Very Happy days: " + vhappy);
                                }
                            }
                        })
        ;

    }
}