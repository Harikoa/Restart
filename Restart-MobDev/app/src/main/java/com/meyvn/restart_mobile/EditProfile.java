package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.meyvn.restart_mobile.POJO.Account;

import org.w3c.dom.Text;

public class EditProfile extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Account acc = Login.storedAcc;
        Button cancel = findViewById(R.id.btn_cancel);
        Button save = findViewById(R.id.btn_save);
        EditText contact = findViewById(R.id.editContact);
        EditText nickname = findViewById(R.id.editNickname);
        EditText pw = findViewById(R.id.editPassword);
        EditText cpw = findViewById(R.id.editReEnter);
        EditText oldpw = findViewById(R.id.editConfirmPW);
        contact.setText(acc.getContact());
        nickname.setText(acc.getNickname());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.getText().toString().isEmpty()|| nickname.getText().toString().isEmpty()
                || oldpw.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Please enter all necessary fields",Toast.LENGTH_LONG).show();
                else {
                    AuthCredential credential = EmailAuthProvider.getCredential(Login.storedAcc.getEmail(), oldpw.getText().toString());
                    auth.getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (!pw.getText().toString().isEmpty()) {
                                            if (pw.getText().toString().equals(cpw.getText().toString()))
                                                auth.getCurrentUser().updatePassword(pw.getText().toString());
                                            else {
                                                Toast.makeText(getApplicationContext(), "Passwords are not matching", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }
                                        acc.setContact(contact.getText().toString());
                                        acc.setNickname(nickname.getText().toString());
                                        FirebaseFirestore.getInstance().collection("Accounts").document(Login.authACC)
                                                .set(acc, SetOptions.merge())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_LONG).show();
                                                            Login.storedAcc = acc;
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),"Wrong current password",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}