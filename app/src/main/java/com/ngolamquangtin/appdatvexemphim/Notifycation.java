package com.ngolamquangtin.appdatvexemphim;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ngolamquangtin.appdatvexemphim.Util.Util;


public class Notifycation extends FirebaseMessagingService {

    public Notifycation() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Util.createNotifycation(
                this,
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body"));
    }


}
