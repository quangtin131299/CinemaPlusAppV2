package com.ngolamquangtin.appdatvexemphim;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentCinema;

public class GPSService extends Service {

    private LocationListener listener;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service GPS da dung lai", Toast.LENGTH_SHORT).show();
        if(locationManager!=null){
            locationManager.removeUpdates(listener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
