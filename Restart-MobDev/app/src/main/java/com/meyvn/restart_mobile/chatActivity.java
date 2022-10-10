package com.meyvn.restart_mobile;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.meyvn.restart_mobile.Adapter.chatAdapter;
import com.google.gson.Gson;
import com.meyvn.restart_mobile.POJO.Account;
import com.meyvn.restart_mobile.POJO.MessagePOJO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class chatActivity extends AppCompatActivity implements RecyclerViewInterface{
    ArrayList<MessagePOJO>array;
    CollectionReference cf = FirebaseFirestore.getInstance().collection("Chat");
    chatAdapter adapter;
    RecyclerView rc;
    final String serverKey = "AAAAmgjgKhQ:APA91bGLJ2Q8gbhNXqRbM6QcJEIDP_8irZQvL6FAzLVeAxooHX033muWBxzV_xEitkxGEIgLM8K_kzFNfOOpv7pXg2szxLNdCWQnj5hdI5ZAOoKJFUphD_OtM8OYVvXLyzXVwlZ9lTCP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        TextView nickname = findViewById(R.id.chatNickname);
      rc = findViewById(R.id.chatRecycler);
        EditText newMessage = findViewById(R.id.createMessage);
        ImageButton send = findViewById(R.id.sendMessage);
        rc.setLayoutManager(new LinearLayoutManager(this));
         array = new ArrayList<>();
        Intent i = getIntent();
        Gson gson = new Gson();
        String JSON = i.getStringExtra("JSON");
        Account receiverAcc = gson.fromJson(JSON,Account.class);
        Account senderAcc = Login.storedAcc;
        nickname.setText("@ " + receiverAcc.getNickname());
        adapter  = new chatAdapter(this,array,this,senderAcc.getID(),receiverAcc.getID());
        rc.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newMessage.getText().length()>0)
                {
                    MessagePOJO message = new MessagePOJO();
                    message.setMsgContent(newMessage.getText().toString().trim());
                    message.setSenderEmail(senderAcc.getID());
                    message.setReceiverEmail(receiverAcc.getID());
                    message.setDate(new Date());
                    cf.add(message);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("to",receiverAcc.getFCM());
                        JSONObject notification = new JSONObject();
                        notification.put("title",senderAcc.getNickname() + " has sent you a message");
                        notification.put("body","Check your messages!");
                        object.put("notification",notification);
                        JSONObject data = new JSONObject();
                        data.put("title",senderAcc.getNickname() + " has sent you a message");
                        data.put("body","Check your messages!");
                        object.put("data",data);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send",
                            object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "key=" + serverKey);

                            return params;
                        }
                    };

                    RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                    rq.add(objectRequest);
                }
            newMessage.setText("");
            }
        });
        cf.whereEqualTo("senderEmail",senderAcc.getID())
                .whereEqualTo("receiverEmail",receiverAcc.getID())
                .orderBy("date")
                .addSnapshotListener(eventListener);
        cf.whereEqualTo("senderEmail",receiverAcc.getID())
                .whereEqualTo("receiverEmail", senderAcc.getID())
                .orderBy("date")
                .addSnapshotListener(eventListener);


    }

    @Override
    public void onItemclick(int position) {

    }
    public final EventListener<QuerySnapshot> eventListener =(value, error) ->
    {
        if(error!=null)
            error.printStackTrace();
        if(value!=null && error==null)
        {

            for(DocumentChange ds : value.getDocumentChanges())
            {
                if(ds.getType()== DocumentChange.Type.ADDED) {
                    array.add(ds.getDocument().toObject(MessagePOJO.class));
                    array.sort(new Comparator<MessagePOJO>() {
                        @Override
                        public int compare(MessagePOJO messagePOJO, MessagePOJO t1) {
                            return messagePOJO.getDate().compareTo(t1.getDate());
                        }
                    });
                }
            }
            if(array.size()==0)
            adapter.notifyDataSetChanged();
            else {
                adapter.notifyItemRangeInserted(array.size(), array.size());
                rc.smoothScrollToPosition(array.size() - 1);
            }
        }
    };


}