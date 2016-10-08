package com.example.mylovelychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.adapter.UsersChatAdapter;
import com.example.mylovelychat.model.UsersChatModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    DatabaseReference mFirebaseChatRef;

    DatabaseReference mFireChatUsersRef;

    private RecyclerView mUserFireChatRecyclerView;

    private View mProgressBarForUsers;

    private UsersChatAdapter mUsersChatAdapter;

    private List<String> mUserKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Initialize firebase
        mFirebaseChatRef = FirebaseDatabase.getInstance().getReference();
        // Get a reference to users child in firebase
        mFireChatUsersRef = mFirebaseChatRef.child(ReferenceUrl.CHILD_USERS);

        // Get s reference to recyclerView
        mUserFireChatRecyclerView = (RecyclerView) findViewById(R.id.usersFireChatRecyclerView);

        // Get a reference to progress bar
        mProgressBarForUsers = findViewById(R.id.progress_bar_users);

        // Initialize adapter
        List<UsersChatModel> emptyListChat = new ArrayList<>();


        //Initialize keys list
        mUserKeyList = new ArrayList<>();

    }
}
