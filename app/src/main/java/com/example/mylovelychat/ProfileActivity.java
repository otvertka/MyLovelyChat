package com.example.mylovelychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "myLogs";

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;

    private Button buttonLogout;
    private Button buttonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonList = (Button) findViewById(R.id.buttonList);

        buttonLogout.setOnClickListener(this);
        buttonList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout){
            reference.child(firebaseAuth.getCurrentUser().getUid()).child(ReferenceUrl.CHILD_CONNECTION).setValue(ReferenceUrl.KEY_OFFLINE);
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == buttonList){
            //finish();
            startActivity(new Intent(this, ListActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, " onStart - ProfileActivity ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " onStart - ProfileActivity ");
        reference.child(firebaseAuth.getCurrentUser().getUid()).child(ReferenceUrl.CHILD_CONNECTION).setValue(ReferenceUrl.KEY_ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, " OnPause - ProfileActivity ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.child(firebaseAuth.getCurrentUser().getUid()).child(ReferenceUrl.CHILD_CONNECTION).setValue(ReferenceUrl.KEY_OFFLINE);
        Log.e(TAG, " OnDestroy - ProfileActivity ");


    }
}
