package com.example.mylovelychat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylovelychat.R;
import com.example.mylovelychat.model.MessageChatModel;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Дмитрий on 10.10.2016.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MessageChatModel> mListOfFireChat;
    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;

    public MessageChatAdapter(List<MessageChatModel> listOfFireChat){
        mListOfFireChat = listOfFireChat;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListOfFireChat.get(position).getRecipientOrSenderStatus() == SENDER){
            return SENDER;
        } else {
            return RECIPIENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SENDER:
                View viewSender = inflater.inflate(R.layout.sender_message, parent, false);
                viewHolder = new ViewHolderSender(viewSender);
                break;
            case RECIPIENT:
                View viewRecipient = inflater.inflate(R.layout.recipient_message, parent, false);
                viewHolder = new ViewHolderRecipient(viewRecipient);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.sender_message, parent, false);
                viewHolder = new ViewHolderSender(viewSenderDefault);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case SENDER:
                ViewHolderSender viewHolderSender = (ViewHolderSender)holder;
                configureSenderView(viewHolderSender, position);
                break;
            case RECIPIENT:
                ViewHolderRecipient viewHolderRecipient = (ViewHolderRecipient)holder;
                configureRecipientView(viewHolderRecipient, position);
                break;
        }
    }

    private void configureSenderView(ViewHolderSender viewHolderSender, int position){
        MessageChatModel senderFireMessage = mListOfFireChat.get(position);
        viewHolderSender.getSenderMessageTextView().setText(senderFireMessage.getMessage());
    }

    private void configureRecipientView(ViewHolderRecipient viewHolderRecipient, int position) {
        MessageChatModel recipientFireMessage = mListOfFireChat.get(position);
        viewHolderRecipient.getRecipientMessageTextView().setText(recipientFireMessage.getMessage());
    }

    @Override
    public int getItemCount() {
        return mListOfFireChat.size();
    }

    public void refillAdapter(MessageChatModel newFireChatModel){

        // add new message chat to list
        mListOfFireChat.add(newFireChatModel);

        /* refresh view*/
        notifyItemInserted(getItemCount() - 1);
    }

    public void refillFirstTimeAdapter(List<MessageChatModel> newFireChatMessage){

        /* add new message chat to list*/
        mListOfFireChat.clear();
        mListOfFireChat.addAll(newFireChatMessage);

        /*refresh view*/
        notifyItemInserted(getItemCount() - 1);
    }

    public void cleanUp(){
        mListOfFireChat.clear();
    }



    /*==============ViewHolder===========*/

    /*ViewHolder for Sender*/

    public class ViewHolderSender extends RecyclerView.ViewHolder{

        private TextView mSenderMessageTextView;

        public ViewHolderSender(View itemView) {
            super(itemView);
            mSenderMessageTextView = (TextView)itemView.findViewById(R.id.senderMessage);
        }

        public TextView getSenderMessageTextView() {
            return mSenderMessageTextView;
        }

        public void setSenderMessageTextView(TextView senderMessage) {
            mSenderMessageTextView = senderMessage;
        }


    }


    /*ViewHolder for Recipient*/
    public class ViewHolderRecipient extends RecyclerView.ViewHolder{

        private TextView mRecipientMessageTextView;

        public ViewHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = (TextView)itemView.findViewById(R.id.recipientMessage);
        }

        public TextView getRecipientMessageTextView() {
            return mRecipientMessageTextView;
        }

        public void setRecipientMessageTextView(TextView recipientMessage) {
            mRecipientMessageTextView = recipientMessage;
        }
    }
}
