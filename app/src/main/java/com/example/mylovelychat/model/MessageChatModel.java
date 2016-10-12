package com.example.mylovelychat.model;

/**
 * Created by Дмитрий on 10.10.2016.
 */

public class MessageChatModel {

    private String message;
    private String recipient;
    private String sender;

    private int mRecipientOrSenderStatus;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }

    public void setRecipientOrSenderStatus(int mRecipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = mRecipientOrSenderStatus;
    }




}
