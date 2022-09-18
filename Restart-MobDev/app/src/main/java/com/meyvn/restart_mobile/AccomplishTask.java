package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;

import java.time.LocalDate;

public class AccomplishTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accomplish_task);
        Intent i = getIntent();
        Gson convert = new Gson();
        Button submit = findViewById(R.id.accomplishSubmit);
        ViewTaskPojo pojo = convert.fromJson(i.getStringExtra("JSON"),ViewTaskPojo.class);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        TextView title = findViewById(R.id.accomplishTaskTitle);
        EditText reflection = findViewById(R.id.accomplishReflection);
        title.setText(pojo.getTitle());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reflection.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Write a reflection!",Toast.LENGTH_LONG).show();
                else
                {
                    pojo.setComplete(true);
                    pojo.setDateAccomplished(""+ LocalDate.now());
                    pojo.setTaskReflection(reflection.getText().toString());

                fs.collection("Accounts").document(Login.storedAcc.getEmail()).collection("Task").document(pojo.getID())
                        .set(pojo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    try {
                                        Toast.makeText(getApplicationContext(),"Task Successfully completed!",Toast.LENGTH_LONG).show();
                                        Thread.sleep(1500);
                                        Intent i = new Intent(getApplicationContext(),ViewTasks.class);
                                        startActivity(i);
                                        finish();

                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                ;
                }
            }
        });


    }
}