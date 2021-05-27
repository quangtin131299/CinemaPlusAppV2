package com.ngolamquangtin.appdatvexemphim.Util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    public static final String LINK_LOGIN = "https://serverappdatve.herokuapp.com/login";
    public static final String LINK_REGISTER = "https://serverappdatve.herokuapp.com/register";
//    public static final String LINK_LOADPHIMDANGCHIEU = "https://serverappdatve.herokuapp.com/loadphimdangchieu";
//    public static final String LINK_LOADPHIMSAPCHIEU = "https://serverappdatve.herokuapp.com/loadphimsapchieu";
//    public static final String LINK_LOADRAPPHIM = "https://serverappdatve.herokuapp.com/loadrapphim";
//    public static final String LINK_MOVIEFAVOURITE = "https://serverappdatve.herokuapp.com/loadphimyeuthich";
//    public static final String LINK_MOVIEDETAIL = "https://serverappdatve.herokuapp.com/loadchitietphim?idmovie=%d";
    public static final String LINK_LOADSUATCHIEUTHEORAP = "https://serverappdatve.herokuapp.com/loadxuatchieu?idrap=%d&idphim=%d&ngayhientai=%s";
    public static final String LINK_LOADDSVE = "https://serverappdatve.herokuapp.com/loadve?iduser=%d";
    public static final String LINK_LOADGHE = "https://serverappdatve.herokuapp.com/loadghe";
    public static final String LINK_LOADPHONG = "https://serverappdatve.herokuapp.com/loadphong?suatchieu=%s&idphim=%d&idrap=%d&ngayhientai=%s";
    public static final String LINK_UPDATEUSER = "https://serverappdatve.herokuapp.com/capnhatthongtinkhach";
    public static final String LINK_DATVE = "http://192.168.55.104:3000/datvephim?ngaydat=%s&idsuat=%d&idghe=%d&idphim=%d&idkhachhang=%d&idrap=%d&status=%s&idphong=%d";
    public static final String LINK_UPDATESTATUSVE = "https://serverappdatve.herokuapp.com/capnhattrangthaidatve?idphong=%d&idghe=%d&ngaydat=%s&idsuat=%d&idve=%d&idhoadon=%d";
    public static  final  String LINK_UPDATEPASSWORDUSER = "https://serverappdatve.herokuapp.com/updatepassuser";

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            return input;

        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        String unit="K";
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    public static boolean checkLocationTurnOn(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return(gps_enabled && network_enabled);
    }

    public static void turnOnLocation(Context context) {
        if (Util.checkLocationTurnOn(context) == false) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(2000);

            LocationSettingsRequest.Builder builderlocation = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builderlocation.setAlwaysShow(true);

            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context.getApplicationContext())
                    .checkLocationSettings(builderlocation.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        Toast.makeText(context, "Location on", Toast.LENGTH_SHORT).show();
                    } catch (ApiException e) {
                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                try {
                                    resolvableApiException.startResolutionForResult((Activity) context, 1001);
                                } catch (IntentSender.SendIntentException sendIntentException) {
                                    sendIntentException.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;
                        }

                    }

                }
            });
        }else{
            return;
        }
    }
}
