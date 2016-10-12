package com.example.mylovelychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovelychat.FireChatHelper.ChatHelper;
import com.example.mylovelychat.FireChatHelper.ReferenceUrl;
import com.example.mylovelychat.R;
import com.example.mylovelychat.model.UsersChatModel;
import com.example.mylovelychat.ui.ChatActivity;

import java.util.List;

/**
 * Created by Дмитрий on 08.10.2016.
 */

public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolderUsers> {

    private List<UsersChatModel> mFireChatUsers;
    private Context mContext;
    private String mCurrentUserName;
    private String mCurrentUserCreatedAt;

    public UsersChatAdapter(Context context, List<UsersChatModel> fireChatUsers){
        mFireChatUsers = fireChatUsers;
        mContext =  context;
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout for each row
        return new ViewHolderUsers(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderUsers holder, int position) {
        UsersChatModel fireChatUser = mFireChatUsers.get(position);

        // Set avatar
        int userAvatarId = ChatHelper.getDrawableAvatarId(fireChatUser.getAvatarId());
        Drawable avatarDrawable = ContextCompat.getDrawable(mContext, userAvatarId);
        holder.getmUserPhoto().setImageDrawable(avatarDrawable);

        // Set username
        holder.getmUserFirstName().setText(fireChatUser.getFirstName());

        // Set presence status
        holder.getmStatusConnection().setText(fireChatUser.getConnection());

        // Set presence text color
        if (fireChatUser.getConnection().equals(ReferenceUrl.KEY_ONLINE)){
            // green color
            holder.getmStatusConnection().setTextColor(Color.parseColor("#00FF00"));
        } else{
            // red color
            holder.getmStatusConnection().setTextColor(Color.parseColor("#FF0000"));
        }

    }

    @Override
    public int getItemCount() {
        return mFireChatUsers.size();
    }

    public void refill(UsersChatModel users){

        // Add each user and notify recyclerView about change
        mFireChatUsers.add(users);
        notifyDataSetChanged();
    }

    public void setNameAndCreatedAt(String userName, String createdAt){

        // Set current user name and time account created at
        mCurrentUserName = userName;
        mCurrentUserCreatedAt = createdAt;
    }

    public void changeUser (int index, UsersChatModel user){

        // Handle change on each user and notify change
        mFireChatUsers.set(index, user);
        notifyDataSetChanged();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mUserPhoto; //User avatar
        private TextView mUserFirstName; // User first name
        private TextView mStatusConnection; // User presence
        private Context mContextViewHolder;

        public ViewHolderUsers(Context context, View itemView) {
            super(itemView);
            mUserPhoto = (ImageView) itemView.findViewById(R.id.userPhotoProfile);
            mUserFirstName = (TextView) itemView.findViewById(R.id.userFirstNameProfile);
            mStatusConnection = (TextView) itemView.findViewById(R.id.connectionStatus);
            mContextViewHolder = context;

            // Attach a click listener to the entire row view

            itemView.setOnClickListener(this);
        }

        public ImageView getmUserPhoto() {
            return mUserPhoto;
        }
        public TextView getmUserFirstName() {
            return mUserFirstName;
        }
        public TextView getmStatusConnection() {
            return mStatusConnection;
        }

        @Override
        public void onClick(View v) {

            //Handle click on each row

            int position = getLayoutPosition(); // Get row position

            UsersChatModel user = mFireChatUsers.get(position);

            // Provide current user username and time created at
            user.setmCurrentUserName(mCurrentUserName);
            user.setmCurrentUserCreatedAt(mCurrentUserCreatedAt);

            // Create a chat activity
            Intent chatIntent = new Intent(mContextViewHolder, ChatActivity.class);

            // Attach data to activity as a parcelable object
            chatIntent.putExtra(ReferenceUrl.KEY_PASS_USERS_INFO, user);

            // Start new activity
            mContextViewHolder.startActivity(chatIntent);
        }
    }

}
