package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.meyvn.restart_mobile.POJO.Account;
import com.meyvn.restart_mobile.POJO.accountDeletion;

import java.util.Date;

public class deactivate_acc_page extends AppCompatActivity {
    CollectionReference fs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deactivate_acc_page);
        fs = FirebaseFirestore.getInstance().collection("AccountDeactivation");
        Button deactivate = findViewById(R.id.deactivateButton);
        EditText pw  = findViewById(R.id.deletionPassword);
        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pw.getText().toString().equals(Login.storedAcc.getPassword())) {
                    accountDeletion pojo = new accountDeletion();
                    pojo.setFinished(false);
                    pojo.setUserEmail(Login.storedAcc.getEmail());
                    pojo.setDateRequested(new Date());
                    fs.add(pojo)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(getApplicationContext(),"Request submitted",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                    ;
                }
                else
                    Toast.makeText(getApplicationContext(),"WRONG PASSWORD",Toast.LENGTH_LONG).show();
            }
        });

    }
}