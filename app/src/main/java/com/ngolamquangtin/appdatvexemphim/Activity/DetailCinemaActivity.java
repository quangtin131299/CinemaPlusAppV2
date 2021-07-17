package com.ngolamquangtin.appdatvexemphim.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.astritveliu.boom.Boom;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.GPSService;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailCinemaActivity extends AppCompatActivity implements OnMapReadyCallback {

    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    LinearLayout linearShowTime;

    Location currentLocation;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    ImageButton ibtnvitri;
    Cinema detailCinema;
    TextView txtTenRap;
    TextView txtDiaChi;
    TextView txtchiduong;
    TextView txtkhoancach;
    ImageView imgrap;
    GoogleMap mMap;
    Button btnHuy, btnMovieNowShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cinema);

        addControls();

        loadDataCinema();

        getCurrentLocation();

        setCurrentLocation(DetailCinemaActivity.this);

        initMap();

        addEvents();

    }


    private void addEvents() {
        btnMovieNowShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailCinema != null) {
                    Intent intentToScreenMovieNow = new Intent(DetailCinemaActivity.this, MovieNowShowActivity.class);
                    intentToScreenMovieNow.putExtra("ID_CINEMA", detailCinema.getId());
                    startActivity(intentToScreenMovieNow);
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linearShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToScreenSchedule = new Intent(DetailCinemaActivity.this, ScheduleActivity.class);
                intentToScreenSchedule.putExtra("CINEMA", detailCinema);
                startActivity(intentToScreenSchedule);
            }
        });

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
                        setCurrentLocation(DetailCinemaActivity.this);
                        updateUIDistance(currentLocation, detailCinema);
                    }
                } else {
                    updateStatusBtnLocation(false);
                }
            }
        });

        new Boom(ibtnvitri);
        new Boom(txtchiduong);
        new Boom(btnHuy);
        new Boom(btnMovieNowShow);
        new Boom(linearShowTime);
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
        btnMovieNowShow = findViewById(R.id.btnmovienowshow);
        linearShowTime = findViewById(R.id.linearshowtime);
        txtkhoancach = findViewById(R.id.txtkhoancach);
        txtchiduong = findViewById(R.id.txtchiduong);
        ibtnvitri = findViewById(R.id.ibtnvitri);
        txtTenRap = findViewById(R.id.txtenrap);
        txtDiaChi = findViewById(R.id.txtdiachi);
        imgrap = findViewById(R.id.imgrap);
        btnHuy = findViewById(R.id.btnhuy);

        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(DetailCinemaActivity.this);
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
        mMap.addMarker(new MarkerOptions().position(lnglatCinema).title(detailCinema.getTenrap()).icon(bitmapDescriptorFromVector(R.drawable.ic_marker_cinema)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lnglatCinema, 16));
    }

    public BitmapDescriptor bitmapDescriptorFromVector(int vectorDrawableResourceId) {
        VectorDrawable vectorDrawable = (VectorDrawable) ContextCompat.getDrawable(DetailCinemaActivity.this, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCALTION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLocation(DetailCinemaActivity.this);
                updateStatusBtnLocation(true);
            } else {
                updateStatusBtnLocation(false);
                Toast.makeText(this, "Tu choi Quyen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setCurrentLocation(Context context) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                                    != PackageManager.PERMISSION_GRANTED
                                                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                                    != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                currentLocation = locationResult.getLastLocation();

                updateUIDistance( currentLocation, detailCinema);



                fusedLocationClient.removeLocationUpdates(this);
            }
        }, Looper.getMainLooper());
    }

    public void updateUIDistance(Location location, Cinema cinema) {
        if (location != null && cinema != null) {
            txtkhoancach.setVisibility(View.VISIBLE);
            double resultDistance = Util.distance(location.getLatitude()
                                                  , location.getLongitude()
                                                  , Double.parseDouble(cinema.getVido())
                                                  , Double.parseDouble(cinema.getKinhdo()));
            DecimalFormat decimalformat = new DecimalFormat("#.##");
            txtkhoancach.setText(String.valueOf(decimalformat.format(resultDistance)) + " km");
        }
    }

    public void openAppGoogleMap(Location location, Cinema cinema) {
        Uri uridir = Uri.parse("https://www.google.co.in/maps/dir/" + location.getLatitude()
                                                                    + "," + location.getLongitude()
                                                                    + "/" + detailCinema.getDiachi()
                                                                    + "/@" + cinema.getVido()
                                                                    + "," + cinema.getKinhdo());

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

    public  Cinema getDetailCinema() {
        if (detailCinema != null) {
            return detailCinema;
        }
        return null;
    }

    public  void setCurrentLocation(Location newLocation) {
        currentLocation = newLocation;
    }

    public  Location getCurrentLocation() {
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

    public void updateStatusBtnLocation(boolean isTurnOn){
        if(isTurnOn){
            ibtnvitri.setImageResource(R.drawable.ic_turnonlocation);
        }else{
            ibtnvitri.setImageResource(R.drawable.ic_turnofflocation);
        }
    }


}