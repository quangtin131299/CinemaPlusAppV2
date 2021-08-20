package com.ngolamquangtin.appdatvexemphim;

import android.content.SharedPreferences;

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

        SharedPreferences sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("isNotifyNewMovie", remoteMessage.getData().get("idMovie"));

        editor.apply();

        Util.createNotifycation(
                this,
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body"));
    }


}
