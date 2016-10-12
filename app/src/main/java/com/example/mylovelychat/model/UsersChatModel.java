package com.example.mylovelychat.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Дмитрий on 08.10.2016.
 */

public class UsersChatModel implements Parcelable {

    public static final String TAG = "myLogs";

    /**recipient info*/
    private String firstName;
    private String provider; //if you don't include this app crash
    private String userEmail;
    private String createdAt;
    private String connection;
    private int    avatarId;
    private String mRecipientUid;


/**Current user (or sender) info */
    private String mCurrentUserName;
    private String mCurrentUserUid;
    private String mCurrentUserEmail;
    private String mCurrentUserCreatedAt;

    public UsersChatModel(){
        //required empty username
    }

    private UsersChatModel(Parcel in) {
        Log.d(TAG, "UsersChatModel ");

        //Remember the order used to read data is the same used to write them
        firstName = in.readString();
        provider = in.readString();
        userEmail = in.readString();
        createdAt = in.readString();
        connection = in.readString();
        avatarId = in.readInt();
        mRecipientUid = in.readString();
        mCurrentUserName = in.readString();
        mCurrentUserUid = in.readString();
        mCurrentUserEmail = in.readString();
        mCurrentUserCreatedAt = in.readString();
    }

    public String getmCurrentUserName() {
        Log.d(TAG, "getmCurrentUserName ");
        return mCurrentUserName;
    }

    public void setmCurrentUserName(String mCurrentUserName) {
        Log.d(TAG, "setmCurrentUserName ");
        this.mCurrentUserName = mCurrentUserName;
    }

    public String getmCurrentUserUid() {
        Log.d(TAG, "getmCurrentUserUid ");
        return mCurrentUserUid;
    }

    public void setmCurrentUserUid(String mCurrentUserUid) {
        Log.d(TAG, "setmCurrentUserUid ");
        this.mCurrentUserUid = mCurrentUserUid;
    }

    public String getmCurrentUserEmail() {
        Log.d(TAG, "getmCurrentUserEmail ");
        return mCurrentUserEmail;
    }

    public void setmCurrentUserEmail(String mCurrentUserEmail) {
        Log.d(TAG, "setmCurrentUserEmail ");
        this.mCurrentUserEmail = mCurrentUserEmail;
    }

    public String getmCurrentUserCreatedAt() {
        Log.d(TAG, "getmCurrentUserCreatedAt ");
        return mCurrentUserCreatedAt;
    }

    public void setmCurrentUserCreatedAt(String mCurrentUserCreatedAt) {
        Log.d(TAG, "setmCurrentUserCreatedAt ");
        this.mCurrentUserCreatedAt = mCurrentUserCreatedAt;
    }


/**Recipient info*/
    public String getFirstName() {
        Log.d(TAG, "getFirstName ");
        return firstName;
    }

    public String getProvider() {
        Log.d(TAG, "getProvider ");
        return provider;
    }

    public String getUserEmail() {
        Log.d(TAG, "getUserEmail ");
        return userEmail;
    }

    public String getCreatedAt() {
        Log.d(TAG, "getCreatedAt ");
        return createdAt;
    }

    public String getConnection() {
        Log.d(TAG, "getConnection ");
        return connection;
    }

    public int getAvatarId() {
        Log.d(TAG, "getAvatarId ");
        return avatarId;
    }

    public String getRecipientUid() {
        Log.d(TAG, "getRecipientUid ");
        return mRecipientUid;
    }

    public void setRecipientUid(String RecipientUid) {
        Log.d(TAG, "setRecipientUid ");
        this.mRecipientUid = RecipientUid;
    }

    /* Create chat endpoint for firebase*/
    public String getChatRef(){
        return createUniqueChatRef();
    }

    private String createUniqueChatRef(){
        String uniqueChatRef = "";
        if (createdAtCurrentUser() > createdAtRecipient()){
            uniqueChatRef = cleanEmailAddress(getmCurrentUserEmail()) + "-" + cleanEmailAddress(getUserEmail());
        } else {
            uniqueChatRef = cleanEmailAddress(getUserEmail()) + "-" + cleanEmailAddress(getmCurrentUserEmail());
        }
        return uniqueChatRef;
    }

    private long createdAtCurrentUser(){
        return Long.parseLong(getmCurrentUserCreatedAt());
    }

    private long createdAtRecipient(){
        return Long.parseLong(getCreatedAt());
    }

    private String cleanEmailAddress(String email){
        // replace dot with comma since firebase does not allow dot
        return email.replace(".", "-");
    }


/**Parcelable*/
    public static final Creator<UsersChatModel> CREATOR = new Creator<UsersChatModel>() {

        @Override
        public UsersChatModel createFromParcel(Parcel in) {
            Log.d(TAG, "createFromParcel ");
            return new UsersChatModel(in);
        }

        @Override
        public UsersChatModel[] newArray(int size) {
            Log.d(TAG, "newArray ");

            return new UsersChatModel[size];
        }
    };

    @Override
    public int describeContents() {
        Log.d(TAG, "describeContents ");
        return 0; //ignore
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel ");

        // Store information using parcel method
        // the order for writing and reading must be the same
        dest.writeString(firstName);
        dest.writeString(provider);
        dest.writeString(userEmail);
        dest.writeString(createdAt);
        dest.writeString(connection);
        dest.writeInt(avatarId);
        dest.writeString(mRecipientUid);
        dest.writeString(mCurrentUserName);
        dest.writeString(mCurrentUserUid);
        dest.writeString(mCurrentUserEmail);
        dest.writeString(mCurrentUserCreatedAt);

    }
}
