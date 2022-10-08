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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.meyvn.restart_mobile.POJO.JournalPojo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class view_urge_data extends AppCompatActivity {
    ArrayList<BarEntry> bar;
    BarDataSet set;
    ArrayList<String> label;
    ArrayList<String> ylabel;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_urge_data);
        Button journal = findViewById(R.id.journalDataButton);
        Button mood = findViewById(R.id.moodDataButton);
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewMoodTrackerData.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewJournalData.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        label=new ArrayList<>();
        label.add("Intensity");
        label.add("Frequency");
        label.add("Length");
        ylabel = new ArrayList<>();
        ylabel.add("NOT AT ALL");
        ylabel.add("SLIGHT");
        ylabel.add("MODERATE");
        ylabel.add("CONSIDERABLE");
        ylabel.add("EXTREME");
        putValues();
    }
    public void putValues()
    {
        bar = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Accounts").document(Login.storedAcc.getEmail())
                .collection("Journal").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            float intensity = 0;
                            float freq = 0;
                            float length =0;
                            float number =0;
                            int days=0;
                            for (DocumentSnapshot ds : task.getResult()) {
                                JournalPojo pojo = ds.toObject(JournalPojo.class);
                                intensity += pojo.getSubstanceIntensity();
                                freq+= pojo.getSubstanceFrequency();
                                length+= pojo.getSubstanceLength();
                                number+=pojo.getSubstanceNumber();
                                days++;
                            }//for
                            if(days>0)
                            {
                                intensity=Float.parseFloat(df.format(intensity/days));
                                freq = Float.parseFloat(df.format(freq/days));
                                length=Float.parseFloat(df.format(length/days));
                                number=Float.parseFloat(df.format(number/days));
                            }
                            else
                                return;
                            bar.add(new BarEntry(1f,intensity));
                            bar.add(new BarEntry(2f,freq));
                            bar.add(new BarEntry(3f,length));;
                            BarChart chart = findViewById(R.id.urgeChart);
                            set = new BarDataSet(bar,"Urge Data");
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
                            chart.getXAxis().setTextSize(13);
                            chart.getDescription().setEnabled(false);
                            chart.getXAxis().setDrawGridLines(false);

                            chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    if(value>0)
                                    return ylabel.get((int)value-1);
                                    else
                                        return "";
                                }
                            });

                            chart.getAxisRight().setEnabled(false);
                            chart.getXAxis().setGranularity(1.0f);
                            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            chart.getAxisLeft().setGranularityEnabled(true);
                            chart.getAxisLeft().setGranularity(1.0f);
                            chart.getAxisLeft().setAxisMinimum(1);
                            chart.getAxisLeft().setAxisMaximum(5);
                            chart.notifyDataSetChanged();
                            chart.invalidate();
                            TextView t1 = findViewById(R.id.totalIntensity);
                            TextView t2 = findViewById(R.id.totalFrequency);
                            TextView t3 = findViewById(R.id.totalLength);
                            TextView t4 = findViewById(R.id.totalNumber);

                            t1.setText("Average Intensity: " + intensity);
                            t2.setText("Average Frequency: " + freq);
                            t3.setText("Average Length: " + length);
                            t4.setText("Average Number: " + number);

                        }
                    }
                })
        ;

    }
}