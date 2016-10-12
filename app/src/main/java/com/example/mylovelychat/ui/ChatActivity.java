package com.example.mylovelychat.ui;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.R;
import com.example.mylovelychat.adapter.MessageChatAdapter;
import com.example.mylovelychat.model.MessageChatModel;
import com.example.mylovelychat.model.UsersChatModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";


    private RecyclerView mChatRecyclerView;
    private TextView     mUserMessageChatText;
    private MessageChatAdapter mMessageChatAdapter;

    /* Sender and Recipient status*/
    private static final int SENDER_STATUS = 0;
    private static final int RECIPIENT_STATUS = 1;

    /* Recipient uid*/
    private String mRecipientUid;

    /* Sender uid*/
    private String mSenderUid;

    /*unique Firebase ref for this chat*/
    private DatabaseReference mFirebaseMessagesChat;

    /* Listen to change in chat in firebase-remember to remove it*/
    private ChildEventListener mMessageChatListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get information from the previous activity
        Intent getUsersData = getIntent();
        UsersChatModel usersDataModel = getUsersData.getParcelableExtra(ReferenceUrl.KEY_PASS_USERS_INFO);

        // Set recipient uid
        mRecipientUid = usersDataModel.getRecipientUid();

        // Set sender(current) uid
        mSenderUid = usersDataModel.getmCurrentUserUid();

        // Reference to recyclerView and text view
        mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mUserMessageChatText = (TextView) findViewById(R.id.chat_user_message);

        // Set recyclerView and adapter
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setHasFixedSize(true);

        // Initialize adapter
        List<MessageChatModel> emptyMessageChat = new ArrayList<MessageChatModel>();
        mMessageChatAdapter = new MessageChatAdapter(emptyMessageChat);

        // Set adapter to recyclerView
        mChatRecyclerView.setAdapter(mMessageChatAdapter);

        // Initialize firebase for this chat
        mFirebaseMessagesChat = FirebaseDatabase.getInstance().getReference().child(ReferenceUrl.CHILD_CHAT).child(usersDataModel.getChatRef());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Log.e(TAG, " I am onStart");
        mMessageChatListener = mFirebaseMessagesChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()){

                    MessageChatModel newMessage = dataSnapshot.getValue(MessageChatModel.class);
                    if (newMessage.getSender().equals(mSenderUid)){
                        newMessage.setRecipientOrSenderStatus(SENDER_STATUS);
                    } else {
                        newMessage.setRecipientOrSenderStatus(RECIPIENT_STATUS);
                    }
                    mMessageChatAdapter.refillAdapter(newMessage);
                    mChatRecyclerView.scrollToPosition(mMessageChatAdapter.getItemCount() -1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.e(TAG, "I am onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.e(TAG, " I am onStop");

        // remove listener
        if (mMessageChatListener != null){
            mFirebaseMessagesChat.removeEventListener(mMessageChatListener);
        }
        // clean chat message
        mMessageChatAdapter.cleanUp();
    }

    public void sendMessageToFireChat(View sendButton){
        String senderMessage = mUserMessageChatText.getText().toString();
        senderMessage = senderMessage.trim();

        if (!senderMessage.isEmpty()){


            // Send message to firebase
            Map<String, String> newMessage = new HashMap<>();
            newMessage.put("sender", mSenderUid);
            newMessage.put("recipient", mRecipientUid);
            newMessage.put("message", senderMessage);

            mFirebaseMessagesChat.push().setValue(newMessage);

            // Clear text
            mUserMessageChatText.setText("");
        }
    }
}
