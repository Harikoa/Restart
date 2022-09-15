package com.meyvn.restart_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;

public class ViewTaskEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task_entry);
        TextView title = findViewById(R.id.taskEntryTitle);
        TextView date = findViewById(R.id.taskEntryDate);
        TextView deadline = findViewById(R.id.taskEntryDeadline);
        TextView details = findViewById(R.id.taskDetailBody);
        Intent i = getIntent();
        Gson convert = new Gson();
        String JSON = i.getStringExtra("JSON");
        ViewTaskPojo pojo = convert.fromJson(JSON,ViewTaskPojo.class);
        title.setText(pojo.getTitle());
        date.setText("Date Assigned: " +pojo.getTaskDate());
        deadline.setText("Deadline: " + pojo.getTaskDeadline());
        details.setText(pojo.getTaskDescription());

    }
}