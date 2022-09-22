package com.meyvn.restart_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.ActivityPojo;
import com.meyvn.restart_mobile.POJO.WeatherPojo;

import java.util.ArrayList;
import java.util.Random;


public class ViewSuggestedActivities extends AppCompatActivity {

    private final String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String key ="5cce295106b063888ae8d3f885ddfad9";
    LocationRequest request;
    TextView city;
    TextView weather;
    RecyclerView recyclerView;
    ArrayList <ActivityPojo> act;
    ActivityAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_suggested_activities);
        Switch swc = findViewById(R.id.gpsSwitch);
       weather  = findViewById(R.id.suggestActWeather);
       city = findViewById(R.id.suggestCity);
        request = LocationRequest.create();
        request.setInterval(30000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       recyclerView = findViewById(R.id.activityRecycler);
        act = new ArrayList<>();
        ad = new ActivityAdapter(ViewSuggestedActivities.this,act);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ad);
        swc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swc.isChecked())
                    updateGPS();
                else
                    updateRecycler();
            }
        });
        updateRecycler();
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    updateGPS();

            }
            else
                Toast.makeText(ViewSuggestedActivities.this,"Check Permissions",Toast.LENGTH_LONG).show();
        }
    }

    public void updateGPS()
    {

        turnOnGPS();
        if(ActivityCompat.checkSelfPermission(ViewSuggestedActivities.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {//check if permission granted
            LocationServices.getFusedLocationProviderClient(ViewSuggestedActivities.this)

                    .requestLocationUpdates(request, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            if(!locationResult.getLocations().isEmpty()) {
                                LocationServices.getFusedLocationProviderClient(ViewSuggestedActivities.this).removeLocationUpdates(this);
                                int index = locationResult.getLocations().size()-1;
                                Location lxc =locationResult.getLocations().get(index);
                                double longitude = lxc.getLongitude();
                                double latitude = lxc.getLatitude();
                                String tempURL = URL + "/?lat="+ latitude + "&lon=" + longitude + "&appid=" + key;
                                getWeather(tempURL);
                            }
                        }
                    }, Looper.getMainLooper());

        }//if
        else
        {//get permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ViewSuggestedActivities.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    public void getWeather(String url)
    {
        StringRequest str =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson convert = new Gson();
                WeatherPojo pojo = convert.fromJson(response,WeatherPojo.class);
                String wther = pojo.weather.get(0).main;
                city.setText("CITY: " + pojo.name);
                weather.setText("WEATHER: " + wther);
                updateRecycler(wther);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(str);
    }

    public void updateRecycler(String wther)
    {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("SuggestedActivities").whereEqualTo("weather",wther)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        act.clear();
                        for(DocumentSnapshot ds : queryDocumentSnapshots.getDocuments())
                        {
                            ActivityPojo pojo = ds.toObject(ActivityPojo.class);
                            pojo.setId(ds.getId());
                            act.add(pojo);

                        }
                        if(act.size()>3)
                        {
                            Random rand = new Random();
                            while(act.size()>3)
                            {
                                int limit = act.size()-1;
                                int random =rand.nextInt(limit);
                                System.out.println(act.get(random).getActivityTitle());
                                act.remove(random);
                            }
                        }
                        ad.notifyDataSetChanged();
                    }
                });
    }
    public void updateRecycler()
    {
        weather.setText("Static Suggestion is displayed");
        city.setText("");
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("SuggestedActivities").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        act.clear();
                        for(DocumentSnapshot ds : queryDocumentSnapshots.getDocuments())
                        {
                            ActivityPojo pojo = ds.toObject(ActivityPojo.class);
                            pojo.setId(ds.getId());
                            act.add(pojo);
                        }
                        if(act.size()>3)
                        {
                            Random rand = new Random();
                            while(act.size()>3)
                            {
                                int limit = act.size()-1;
                                int random =rand.nextInt(limit);
                                System.out.println(act.get(random).getActivityTitle());
                                act.remove(random);
                            }
                        }
                        ad.notifyDataSetChanged();
                    }
                });
    }
}