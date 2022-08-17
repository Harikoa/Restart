package com.restart;
import java.io.FileInputStream;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


public class FirebaseDatabase {
	private Firestore db;
	public void connect()
	{
	FileInputStream serviceAccount;
	try {
		serviceAccount = new FileInputStream("Restart/src/com/restart/serviceAccountKey.json");
	
			FirebaseOptions options = new FirebaseOptions.Builder()
			  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			  .setDatabaseUrl("https://restart-aa1cc.firebaseio.com/")
			  .build();

			FirebaseApp.initializeApp(options);
			db = FirestoreClient.getFirestore();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public void getAccounts()
	{
		connect();
		DocumentReference docRef = db.collection("Accounts").document("Admin");
		 ApiFuture<DocumentSnapshot> future = docRef.get();
		    // ...
		    // future.get() blocks on response
		    DocumentSnapshot document;
			try {
				document = future.get();
				  if (document.exists()) {
				      System.out.println("Document data: " + document.getData());
				    } else {
				      System.out.println("No such document!");
				    }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
	}
}//class
