package com.example.mylovelychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.mylovelychat.R.id.textViewUserEmail;
import static com.example.mylovelychat.R.id.tvOnline;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "myLogs";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference reference;

    private Button buttonLogout;
    private Button buttonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        /** вроде как уже эта проверка не нужна, но это не точно... :) **/
        /*if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }*/

        user = firebaseAuth.getCurrentUser();

        TextView textViewUserName = (TextView) findViewById(textViewUserEmail);
        textViewUserName.setText("Welcome " + user.getEmail());

        final TextView tvOnline = (TextView) findViewById(R.id.tvOnline);
        //reference.orderByChild("connection").equalTo(ReferenceUrl.KEY_ONLINE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int onlineCount = 0;
                long maxCount = dataSnapshot.getChildrenCount();
                int count = (int)maxCount;

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    if (snapshot.child(ReferenceUrl.CHILD_CONNECTION).getValue(String.class).equals("online")){
                        onlineCount++;
                    }
                }

                tvOnline.setText("Всего пользователей: " + count + "\nОнлайн: " + onlineCount);
                //Log.d(TAG, " " + onlineCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonList = (Button) findViewById(R.id.buttonList);

        buttonLogout.setOnClickListener(this);
        buttonList.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout){
            reference.child(user.getUid()).child(ReferenceUrl.CHILD_CONNECTION).setValue(ReferenceUrl.KEY_OFFLINE);
            firebaseAuth.signOut();
            //finish();
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
        Log.e(TAG, " onResume - ProfileActivity ");
        reference.child(user.getUid()).child(ReferenceUrl.CHILD_CONNECTION).setValue(ReferenceUrl.KEY_ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, " OnPause - ProfileActivity ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.child(user.getUid()).child(ReferenceUrl.CHILD_CONNECTION).setValue(ReferenceUrl.KEY_OFFLINE);
        Log.e(TAG, " OnDestroy - ProfileActivity ");
    }
}
