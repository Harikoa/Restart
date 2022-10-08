package com.meyvn.restart_mobile.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.Login;
import com.meyvn.restart_mobile.POJO.Account;
import com.meyvn.restart_mobile.POJO.ViewTaskPojo;
import com.meyvn.restart_mobile.POJO.WeatherPojo;
import com.meyvn.restart_mobile.R;

import java.time.LocalDate;

public class notificationWorker extends Worker {


    public notificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Gson convert = new Gson();
        SharedPreferences prf = getApplicationContext().getSharedPreferences("AccountLogged",getApplicationContext().MODE_PRIVATE);
        Account acc  = convert.fromJson(prf.getString("Account","{}"),Account.class);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("Accounts").document(acc.getEmail()).collection("DrugTest")
                .whereEqualTo("completion",false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty())
                        {
                            useNotif(queryDocumentSnapshots.getDocuments().get(0),"Drug Test");
                        }

                    }
                });
        getApplicationContext().getSystemService(NotificationManager.class)
                .createNotificationChannelGroup(new NotificationChannelGroup("TaskGrp","TaskGrp"));

        fs.collection("Accounts").document(Login.storedAcc.getEmail()).collection("Task")
                .whereEqualTo("complete",false)
                .orderBy("taskDeadline", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int x = 1;
                            for (DocumentSnapshot ds : task.getResult()) {
                              useTask(ds,"Task",x);
                              x++;
                            }
                        }
                        else
                            System.out.println("Task unsuccessful");
                    }
                });
        System.out.println("Notification success");
        return Result.success();
    }
    public void useNotif(DocumentSnapshot ds,String type)
    {

        NotificationChannel nc = new NotificationChannel("drugTestNotif","drugTestNotif",NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager nmc = getApplicationContext().getSystemService(NotificationManager.class);
        nmc.createNotificationChannel(nc);
        LocalDate ld = LocalDate.parse(ds.get("deadline").toString());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"drugTestNotif")
                .setContentTitle("Deadline Approaching!")
                .setContentText("You have a deadline for your " + type + " at " + ld)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                ;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1611,builder.build());
    }
    public void useTask(DocumentSnapshot ds,String type,int ctr)
    {

        NotificationChannel nc = new NotificationChannel("taskNotif" + ctr,"taskNotif" + ctr,NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager nmc = getApplicationContext().getSystemService(NotificationManager.class);
        nmc.createNotificationChannel(nc);
        LocalDate ld = LocalDate.parse(ds.get("taskDeadline").toString());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"taskNotif" + ctr)
                .setContentTitle("Deadline Approaching!")
                .setContentText("You have a deadline for your " + type + " at " + ld)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setGroup("TaskGrp")
                ;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(ctr,builder.build());
    }
}
