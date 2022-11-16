package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deactivate_acc_page);
        fs = FirebaseFirestore.getInstance().collection("AccountDeactivation");
        Button deactivate = findViewById(R.id.deactivateButton);
        EditText pw  = findViewById(R.id.deletionPassword);

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCredential credential = EmailAuthProvider.getCredential(Login.storedAcc.getEmail(), pw.getText().toString());
                auth.getCurrentUser().reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    accountDeletion pojo = new accountDeletion();
                                    pojo.setFinished(false);
                                    pojo.setUserID(Login.authACC);
                                    pojo.setDateRequested(new Date());
                                    pojo.setRole(Login.storedAcc.getRole());
                                    fs.whereEqualTo("userID",auth.getCurrentUser().getUid())
                                            .whereEqualTo("finished",false)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful())
                                                {
                                                    QuerySnapshot qs = task.getResult();
                                                    if(qs.getDocuments().isEmpty())
                                                    {
                                                        fs.add(pojo)
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                        Toast.makeText(getApplicationContext(), "Request submitted", Toast.LENGTH_LONG).show();
                                                                        finish();
                                                                    }
                                                                })
                                                        ;
                                                    }
                                                    else
                                                        Toast.makeText(getApplicationContext(),"You have existing request!",Toast.LENGTH_LONG).show();
                                                }
                                                }
                                            });
                                } else
                                    Toast.makeText(getApplicationContext(), "WRONG PASSWORD", Toast.LENGTH_LONG).show();
                            }
                        });


    }
});
    }
}
