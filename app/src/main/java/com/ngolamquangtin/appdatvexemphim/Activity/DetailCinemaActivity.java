package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.GPSService;
import com.ngolamquangtin.appdatvexemphim.LocationChange;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailCinemaActivity extends AppCompatActivity implements OnMapReadyCallback {

    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    LocationChange locationChange;
    static Location currentLocation;
    static ImageButton ibtnvitri;
    static Cinema detailCinema;
    TextView txtTenRap;
    TextView txtDiaChi;
    TextView txtchiduong;
    static TextView txtkhoancach;
    ImageView imgrap;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cinema);

        registerLocation();
        addControls();
        loadDataCinema();
        initMap();
        addEvents();

    }

    public void registerLocation(){
        locationChange = new LocationChange();
        IntentFilter intentFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(locationChange, intentFilter);
    }

    private void addEvents() {
        txtchiduong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailCinema != null) {
                    Util.turnOnLocation(DetailCinemaActivity.this);
                    startGPSService();
                    updateUIDistance(currentLocation, detailCinema);
                    openAppGoogleMap(currentLocation, detailCinema);
                } else {
                    Toast.makeText(DetailCinemaActivity.this, "Bạn chưa bật định vị", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibtnvitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ibtnvitri.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_turnofflocation, null).getConstantState()) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                DetailCinemaActivity.this
                                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_CODE_LOCALTION_PERMISSION);
                    } else {
                        Util.turnOnLocation(DetailCinemaActivity.this);
                        startGPSService();
                        updateUIDistance(currentLocation, detailCinema);
                    }
                } else {
                    updateStatusBtnLocation(false);
                }
            }
        });
    }

    private void loadDataCinema() {
        Intent i = getIntent();
        detailCinema = (Cinema) i.getSerializableExtra("CINEMA");
        if (detailCinema != null) {
            txtTenRap.setText(detailCinema.getTenrap());
            txtDiaChi.setText(detailCinema.getDiachi());
            Picasso.with(DetailCinemaActivity.this).load(detailCinema.getHinh()).into(imgrap);
        }
    }

    private void addControls() {
        txtkhoancach = findViewById(R.id.txtkhoancach);
        txtchiduong = findViewById(R.id.txtchiduong);
        ibtnvitri = findViewById(R.id.ibtnvitri);
        txtTenRap = findViewById(R.id.txtenrap);
        txtDiaChi = findViewById(R.id.txtdiachi);
        imgrap = findViewById(R.id.imgrap);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Util.checkLocationTurnOn(DetailCinemaActivity.this)) {
            setCurrentLocation(DetailCinemaActivity.this);
            updateUIDistance(currentLocation, detailCinema);
        } else {
            txtkhoancach.setVisibility(View.INVISIBLE);
            updateStatusBtnLocation(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (detailCinema == null) {
            return;
        }

        double vd = Double.parseDouble(detailCinema.getVido());
        double kd = Double.parseDouble(detailCinema.getKinhdo());
        mMap = googleMap;

        LatLng lnglatCinema = new LatLng(vd, kd);
        mMap.addMarker(new MarkerOptions().position(lnglatCinema).title(detailCinema.getTenrap()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lnglatCinema, 18));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCALTION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGPSService();
                updateStatusBtnLocation(true);
            } else {
                updateStatusBtnLocation(false);
                Toast.makeText(this, "Tu choi Quyen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void setCurrentLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationProviderClient localClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = localClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setCurrentLocation(location);
                    updateUIDistance(location, detailCinema);
                    ibtnvitri.setImageResource(R.drawable.ic_turnonlocation);
                }
            }
        });
    }

    public static void updateUIDistance(Location location, Cinema cinema) {
        if (location != null && cinema != null) {
            txtkhoancach.setVisibility(View.VISIBLE);
            double resultDistance = Util.distance(location.getLatitude(), location.getLongitude(), Double.parseDouble(cinema.getVido()), Double.parseDouble(cinema.getKinhdo()));
            DecimalFormat decimalformat = new DecimalFormat("#.##");
            txtkhoancach.setText(String.valueOf(decimalformat.format(resultDistance)) + " km");
        }
    }

    public void openAppGoogleMap(Location location, Cinema cinema) {
        Uri uridir = Uri.parse("https://www.google.co.in/maps/dir/" + location.getLatitude() + "," + location.getLongitude() + "/" + detailCinema.getDiachi() + "/@" + cinema.getVido() + "," + cinema.getKinhdo());
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uridir);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1001) {
            switch (requestCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(DetailCinemaActivity.this, "GPS on", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(DetailCinemaActivity.this, "GPS require turn on", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    public static Cinema getDetailCinema() {
        if (detailCinema != null) {
            return detailCinema;
        }
        return null;
    }

    public static void setCurrentLocation(Location newLocation) {
        currentLocation = newLocation;
    }

    public static Location getCurrentLocation() {
        if (currentLocation != null) {
            return currentLocation;
        }
        return null;
    }

    public void startGPSService(){
        Intent i = new Intent(DetailCinemaActivity.this, GPSService.class);
        startService(i);
    }

    public void stopGPSService(){
        Intent i = new Intent(DetailCinemaActivity.this, GPSService.class);
        stopService(i);
    }

    public static void updateStatusBtnLocation(boolean isTurnOn){
        if(isTurnOn){
            ibtnvitri.setImageResource(R.drawable.ic_turnonlocation);
        }else{
            ibtnvitri.setImageResource(R.drawable.ic_turnofflocation);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGPSService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.checkLocationTurnOn(DetailCinemaActivity.this)){
            startGPSService();
        }
    }
}