package com.example.mylovelychat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Дмитрий on 08.10.2016.
 */

public class UsersChatModel implements Parcelable {


/**recipient info*/
    private String firstName;
    private String provider; //if you don't include this app crash
    private String userEmail;
    private String createAt;
    private String connection;
    private int    avatarId;
    private String mRecipientUid;


/**Current user (or sender) info */
    private String mCurrentUserName;
    private String mCurrentUserUid;
    private String mCurrentUserEmail;
    private String mCurrentUserCreatedAt;

    private UsersChatModel(Parcel in) {

        //Remember the order used to read data is the same used to write them
        firstName = in.readString();
        provider = in.readString();
        userEmail = in.readString();
        createAt = in.readString();
        connection = in.readString();
        avatarId = in.readInt();
        mRecipientUid = in.readString();
        mCurrentUserName = in.readString();
        mCurrentUserUid = in.readString();
        mCurrentUserEmail = in.readString();
        mCurrentUserCreatedAt = in.readString();
    }

    public String getmCurrentUserName() {
        return mCurrentUserName;
    }

    public void setmCurrentUserName(String mCurrentUserName) {
        this.mCurrentUserName = mCurrentUserName;
    }

    public String getmCurrentUserUid() {
        return mCurrentUserUid;
    }

    public void setmCurrentUserUid(String mCurrentUserUid) {
        this.mCurrentUserUid = mCurrentUserUid;
    }

    public String getmCurrentUserEmail() {
        return mCurrentUserEmail;
    }

    public void setmCurrentUserEmail(String mCurrentUserEmail) {
        this.mCurrentUserEmail = mCurrentUserEmail;
    }

    public String getmCurrentUserCreatedAt() {
        return mCurrentUserCreatedAt;
    }

    public void setmCurrentUserCreatedAt(String mCurrentUserCreatedAt) {
        this.mCurrentUserCreatedAt = mCurrentUserCreatedAt;
    }


/**Recipient info*/
    public String getFirstName() {
        return firstName;
    }

    public String getProvider() {
        return provider;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getConnection() {
        return connection;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public String getRecipientUid() {
        return mRecipientUid;
    }

    public void setRecipientUid(String RecipientUid) {
        this.mRecipientUid = RecipientUid;
    }


/**Parcelable*/
    public static final Creator<UsersChatModel> CREATOR = new Creator<UsersChatModel>() {
        @Override
        public UsersChatModel createFromParcel(Parcel in) {
            return new UsersChatModel(in);
        }

        @Override
        public UsersChatModel[] newArray(int size) {
            return new UsersChatModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0; //ignore
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // Store information using parcel method
        // the order for writing and reading must be the same
        dest.writeString(firstName);
        dest.writeString(provider);
        dest.writeString(userEmail);
        dest.writeString(createAt);
        dest.writeString(connection);
        dest.writeInt(avatarId);
        dest.writeString(mRecipientUid);
        dest.writeString(mCurrentUserName);
        dest.writeString(mCurrentUserUid);
        dest.writeString(mCurrentUserEmail);
        dest.writeString(mCurrentUserCreatedAt);

    }
}
