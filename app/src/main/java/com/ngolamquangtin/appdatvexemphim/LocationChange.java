package com.ngolamquangtin.appdatvexemphim;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

public class LocationChange extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Util.checkLocationTurnOn(context)) {
            Intent i = new Intent(context, GPSService.class);
            context.startService(i);
        }
    }
}
