package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ViewDrugTest extends AppCompatActivity {
    private Uri uri;
    private ImageView drugPhoto;
    private Map<String, Object> details = new HashMap<>();
    private DocumentReference doc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_drug_test);
        Intent intent = getIntent();

        getDetails(intent);
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        ImageButton upload = findViewById(R.id.uploadDrug);
        Button submit = findViewById(R.id.submitDrugTest);
        drugPhoto = findViewById(R.id.drugImage);
        ImageButton back = findViewById(R.id.backDrug);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,1);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri!=null) {
                  StorageReference drugTest = storage.child(Login.authACC+ "/" + intent.getStringExtra("drugID"));
                    drugTest.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            details.put("completion",true);
                            details.put("dateAccomplished",""+LocalDate.now());
                            doc.update(details)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    try {
                                        Thread.sleep(3000);
                                        Toast.makeText(getApplicationContext(),"Success! Returning to main menu",Toast.LENGTH_LONG).show();
                                        finish();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            ;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(),"ERROR CHECK LOGS!",Toast.LENGTH_LONG).show();
                        }
                    })

                    ;

                }
                else
                    Toast.makeText(getApplicationContext(),"Please select an image of proof! ",Toast.LENGTH_LONG).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }//oncreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            uri = data.getData();
            drugPhoto.setImageURI(uri);
            drugPhoto.setVisibility(View.VISIBLE);
        }
    }
    private void  getDetails(Intent i)
    {
        TextView dateIssued = findViewById(R.id.drugDateIssued);
        TextView deadline = findViewById(R.id.drugDeadline);
        String docID = i.getStringExtra("drugID");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        doc = firestore.collection("Accounts").document(Login.authACC).collection("DrugTest").document(docID);
        doc.get()

                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snap = task.getResult();
                      Map<String,Object> map =  snap.getData();
                      details = map;
                        dateIssued.setText("Date Issued: " + details.get("dateAssigned"));
                        deadline.setText("Deadline: " + details.get("deadline"));

                    }
                });


    }

}
