package com.ngolamquangtin.appdatvexemphim;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ngolamquangtin.appdatvexemphim.Util.Util;


public class Notifycation extends FirebaseMessagingService {

    public Notifycation() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Util.createNotifycation(Notifycation.this, "Thông báo mới", "Cinema Plus vừa công chiếu một phim mới, Mời bạn nhanh chân đến rạp để xem");
    }


}
