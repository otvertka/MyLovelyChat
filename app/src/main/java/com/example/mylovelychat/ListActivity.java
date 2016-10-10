package com.example.mylovelychat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.adapter.UsersChatAdapter;
import com.example.mylovelychat.model.UsersChatModel;
import com.example.mylovelychat.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Reference to firebase
    private DatabaseReference mFirebaseChatRef; // = FirebaseDatabase.getInstance().getReference()

    // Reference to users in firebase
    private DatabaseReference mFireChatUsersRef; // mFirebaseChatRef + .child(ReferenceUrl.CHILD_USERS)

    // Reference connection status
    private DatabaseReference mConnectionStatusRef; // mFireChatUsersRef + .child(mCurrentUserUid).child(ReferenceUrl.CHILD_CONNECTION)

    // Listener for firebase session changes
    private FirebaseAuth mAuthStateListener; /**если что удалить/add ".Auth"*/

    // Data from the authentificated user
    private FirebaseUser mUser;

    //progress bar
    private View mProgressBarForUsers;

    /* fire chat adapter*/
    private UsersChatAdapter mUsersChatAdapter;

    /* current user uid*/
    private String mCurrentUserUid;

    /* current user email*/
    private String mCurrentUserEmail;

    /*Listen to users change in firebase-remember to detach it*/
    private ChildEventListener mListenerUsers;

    /* Listen for user presence*/
    private ValueEventListener mConnectedListener;

    /* List holding user key*/
    private List<String> mUsersKeyList;

    public static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Initialize firebase
        mFirebaseChatRef = FirebaseDatabase.getInstance().getReference();
        // Get a reference to users child in firebase
        mFireChatUsersRef = mFirebaseChatRef.child(ReferenceUrl.CHILD_USERS);

        // Get s reference to recyclerView
        /*or FirebaseUser??*/
        RecyclerView mUserFireChatRecyclerView = (RecyclerView) findViewById(R.id.usersFireChatRecyclerView);

        // Get a reference to progress bar
        mProgressBarForUsers = findViewById(R.id.progress_bar_users);

        // Initialize adapter
        List<UsersChatModel> emptyListChat = new ArrayList<>();
        mUsersChatAdapter = new UsersChatAdapter(this, emptyListChat);

        // Set adapter to recycleView
        mUserFireChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserFireChatRecyclerView.setHasFixedSize(true);
        mUserFireChatRecyclerView.setAdapter(mUsersChatAdapter);

        //Initialize keys list
        mUsersKeyList = new ArrayList<String>();

        // Listen for changes in the authentication state
        mAuthStateListener = FirebaseAuth.getInstance();
        mAuthStateListener.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if  (mUser != null){

                    /* User auth has not expire yet*/

                    // Get unique user ID
                    mCurrentUserUid = mUser.getUid();

                    // Get current user email
                    mCurrentUserEmail = mUser.getEmail();//(String) mUser.getProviderData().get(ReferenceUrl.KEY_EMAIL);

                    // Query all Chat user except current user
                    queryFireChatUsers();
                } else {
                    // Token expires or user log out
                    navigateToLogin();
                }
            }
        });


    }

    private void queryFireChatUsers(){
        Log.d(TAG, "Hello! " );

        // Show progress bar
        showProgressBarForUsers();

        mListenerUsers = mFireChatUsersRef.limitToFirst(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Hide progress bar
                hideProgressBarForUsers();

                if (dataSnapshot.exists()){
                    String userUid = dataSnapshot.getKey();
                    //Log.d(TAG, "UserUid: " + userUid);


                    if (!userUid.equals(mCurrentUserUid)){

                        // Get recipient user name
                        UsersChatModel user = dataSnapshot.getValue(UsersChatModel.class);

                        // Add recipient uid
                        user.setRecipientUid(userUid);

                        // Add current user (or sender) info
                        user.setmCurrentUserEmail(mCurrentUserEmail);
                        //Log.d(TAG, "mCurrentUserEmail: " + mCurrentUserEmail);
                        user.setmCurrentUserUid(mCurrentUserUid);
                        //Log.d(TAG, "mCurrentUserUid: " + mCurrentUserUid);
                        mUsersKeyList.add(userUid);
                        mUsersChatAdapter.refill(user);
                        Log.d(TAG, "if done" );
                        Log.d(TAG, " " );

                    } else{
                        UsersChatModel currentUser = dataSnapshot.getValue(UsersChatModel.class);
                        String userName = currentUser.getFirstName();
                        String createdAt = currentUser.getCreatedAt();
                        mUsersChatAdapter.setNameAndCreatedAt(userName, createdAt);
                        Log.d(TAG, "username: " + userName + " createdAt: " + createdAt);
                        Log.d(TAG, "else done: ");
                        Log.d(TAG, " ");

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.d(TAG, " onChildChanged start");
                if (dataSnapshot.exists()){
                    String userUid = dataSnapshot.getKey();
                    if (!userUid.equals(mCurrentUserUid)){
                        UsersChatModel user = dataSnapshot.getValue(UsersChatModel.class);

                        // Add recipient uid
                        user.setRecipientUid(userUid);

                        // Add current user (or sender) info
                        user.setmCurrentUserEmail(mCurrentUserEmail);
                        user.setmCurrentUserUid(mCurrentUserUid);
                        int index = mUsersKeyList.indexOf(userUid);
                        mUsersChatAdapter.changeUser(index, user);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved ");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled ");
            }
        });
/*
        // Store current user status as online
        mConnectionStatusRef = mFireChatUsersRef.child(mCurrentUserUid).child(ReferenceUrl.CHILD_CONNECTION);

        //Indication of connection status
        mConnectedListener = mFirebaseChatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {

                    mConnectionStatusRef.setValue(ReferenceUrl.KEY_ONLINE);
                    Toast.makeText(ListActivity.this, "Connected to Firebase", Toast.LENGTH_LONG).show();
                } else {

                    // When this device disconnected? remove it
                    mConnectionStatusRef.onDisconnect().setValue(ReferenceUrl.KEY_OFFLINE);
                    Toast.makeText(ListActivity.this, "Disconnected from Firebase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    */
    }

    private void navigateToLogin(){
        Log.d(TAG, "navigateToLogin ");

        // Go to LogIn screen
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task then coming back to this activity should be cleared so we cannot come back to it
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy ");
        // If changing configurations, stop tracking firebase session
        //mFirebaseChatRef.removeEventListener(mAuthStateListener);
        mUsersKeyList.clear();

        // Stop all listeners
        // Make sure to check if they have been initialized
        if (mListenerUsers != null){
            mFirebaseChatRef.removeEventListener(mListenerUsers);
        }

        if (mConnectedListener != null){
            mFirebaseChatRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        }
    }

    private void logout() {
        Log.d(TAG, "logout ");
        if (this.mUser != null){

            /* Logout of Chat*/

            // Store current user status as offline
            mConnectionStatusRef.setValue(ReferenceUrl.KEY_OFFLINE);

            // Finish token
            /**  mFirebaseChatRef.unauth();  */

            /* Update authenticated user and show login screen*/
            mAuthStateListener.signOut();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu ");
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected ");
        if (item.getItemId() == R.id.action_logout){
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Show and hie progress bar
    private void showProgressBarForUsers(){
        //Log.d(TAG, "showProgressBarForUsers ");
        mProgressBarForUsers.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarForUsers(){
        //Log.d(TAG, "hideProgressBarForUsers ");
        if (mProgressBarForUsers.getVisibility() == View.VISIBLE){
            mProgressBarForUsers.setVisibility(View.GONE);
        }
    }
}
