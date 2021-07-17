package com.ngolamquangtin.appdatvexemphim.Util;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ngolamquangtin.appdatvexemphim.Activity.HomeActivity;
import com.ngolamquangtin.appdatvexemphim.R;
import com.vdx.designertoast.DesignerToast;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public static final String LINK_UPDATEPASSWORDUSER = "https://serverappdatve.herokuapp.com/updatepassuser";

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
        String unit = "K";
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

    public static boolean checkLocationTurnOn(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return (gps_enabled && network_enabled);
    }

    public static String formatTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        try {
            Date date = simpleDateFormat.parse(time);

            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();

            return "";
        }
    }

    public static String formatDateServerToClient(String date) {
        SimpleDateFormat formatDateBooking = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat outputformatDateBooking = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dateAfterFormat = formatDateBooking.parse(date);

            return outputformatDateBooking.format(dateAfterFormat);
        } catch (ParseException e) {
            e.printStackTrace();

            return "";
        }
    }

    public static String formatDateClientToServer(String date) {
        SimpleDateFormat formatDateBooking = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputformatDateBooking = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateAfterFormat = formatDateBooking.parse(date);

            return outputformatDateBooking.format(dateAfterFormat);
        } catch (ParseException e) {
            e.printStackTrace();

            return "";
        }
    }

    public static String formatDateClient(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
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
        } else {
            return;
        }
    }

    public static String formatAmount(int amount) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String amountOutput = formatter.format(amount);

        return amountOutput;
    }

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String formatDateByCalendar(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatDateToServerFoCalendar(int year, int moth, int day) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year).append("-").append(moth).append("-").append(day);
        return stringBuilder.toString();
    }

    public static boolean checkEnablePermisson(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return false;
        }

        return true;
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        Date date = new Date();

        return simpleDateFormat.format(date);
    }

    public static void ShowToastErrorMessage(Context context, String mess) {
        DesignerToast.Error(context, "Lỗi"
                , mess
                , Gravity.TOP, Toast.LENGTH_LONG, DesignerToast.STYLE_DARK);
    }

    public static void ShowToastInforMessage(Context context, String mess) {
        DesignerToast.Info(context, "Thông báo"
                , mess
                , Gravity.TOP, Toast.LENGTH_LONG, DesignerToast.STYLE_DARK);
    }

    public static void createNotifycation(Context context, String title, String content) {
        NotificationCompat.Builder builder;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);

        Intent intent = new Intent(context,  HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chaneChannel = new NotificationChannel("Thông báo", "Thông báo", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);

            manager.createNotificationChannel(chaneChannel);

            builder = new NotificationCompat.Builder(context, chaneChannel.getId());

        }else{
            builder = new NotificationCompat.Builder(context, "Thông báo");
        }

        builder.setSmallIcon(R.drawable.ic_app)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(content)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        managerCompat.notify(1, builder.build());
    }

    public static String formatTimeClient(Date date) {
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");

        return formattime.format(date);
    }

    public static long calulatorDistanceTime(Date time1, Date curentTime) {
        long difference = curentTime.getTime() - time1.getTime();

        return difference;
    }

    public static long calulatorDiffTime(Calendar c1, Calendar currenTime) {
        return currenTime.getTime().getTime() -  c1.getTime().getTime();
    }

    public static void dissableBottomDialogDragging(BottomSheetDialog btDialog){
        try {
            Field behaviorField = btDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(btDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING){
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
