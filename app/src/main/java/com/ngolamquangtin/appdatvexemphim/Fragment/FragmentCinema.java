package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.RapAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.GPSService;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCinema extends Fragment {

    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    static ImageButton ibtnvitri;

    Location currentLocation;
    SwipeRefreshLayout refeshcinema;
    TextView txtthongbaokcoketnoi;
    ListView lvCinema;
    RapAdapter rapAdapter;

    ArrayList<Cinema> arrCinema;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinema, container, false);
        addControls(view);
        addEvents();
        loadCinema();
        return view;
    }

    private void addEvents() {

        ibtnvitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ibtnvitri.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_turnofflocation, null).getConstantState()) {
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                getActivity()
                                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_CODE_LOCALTION_PERMISSION);
                    } else {
                        Util.turnOnLocation(getActivity());
//                        startGPSService();
                        getCurrentLocation();
                        updateStatusBtnLocation(true);
                    }
                } else {
                    updateStatusBtnLocation(false);
                }
            }
        });

        lvCinema.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cinema rap = arrCinema.get(position);
                if(rap != null) {
                    Intent i = new Intent(getActivity(), DetailCinemaActivity.class);
                    i.putExtra("CINEMA", rap);
                    startActivity(i);
                }
            }
        });

        refeshcinema.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrCinema.clear();
                rapAdapter.notifyDataSetChanged();
                loadCinema();
            }
        });
    }

    private void updateLocationALLCinema() {
        if(arrCinema.size() != 0){
            int slCinema = arrCinema.size();
            for (int i = 0; i < slCinema ; i++) {
                Cinema cinema = arrCinema.get(i);
                updateDistance(i, cinema, currentLocation);
            }
        }
    }

    public void addControls(View view) {
        refeshcinema = view.findViewById(R.id.refeshcinema);
        ibtnvitri = view.findViewById(R.id.ibtnvitri);
        txtthongbaokcoketnoi = view.findViewById(R.id.txtthongbaokcoketnoi);
        lvCinema = view.findViewById(R.id.lvCinema);
        arrCinema = new ArrayList<>();
        rapAdapter = new RapAdapter(getActivity().getApplicationContext(), arrCinema);
        lvCinema.setAdapter(rapAdapter);
    }

    public void loadCinema() {
        refeshcinema.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Cinema>> listCall = service.getListCinema();
        listCall.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, Response<List<Cinema>> response) {
               if(response.body() != null){
                   refeshcinema.setRefreshing(false);
                   if(txtthongbaokcoketnoi.getVisibility() == View.VISIBLE){
                       txtthongbaokcoketnoi.setVisibility(View.INVISIBLE);
                       lvCinema.setVisibility(View.VISIBLE);
                   }
                   arrCinema.addAll(response.body());
                   rapAdapter.notifyDataSetChanged();
               }

            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {
                refeshcinema.setRefreshing(false);
                if(txtthongbaokcoketnoi.getVisibility() != View.VISIBLE){
                    txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                    lvCinema.setVisibility(View.INVISIBLE);
                }
                call.clone().enqueue(this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        arrCinema.clear();
        rapAdapter.notifyDataSetChanged();
        loadCinema();
    }

    public void updateDistance(int index, Cinema cinema ,Location location){
        if(lvCinema != null) {
            View v = lvCinema.getChildAt(index - lvCinema.getFirstVisiblePosition());

            if (v == null) {
                return;
            }

            TextView txtDistance = v.findViewById(R.id.tvdiachi);
            double distance = Util.distance(Double.parseDouble(cinema.getVido()), Double.parseDouble(cinema.getKinhdo()), location.getLatitude(), location.getLongitude());
            DecimalFormat decimalformat = new DecimalFormat("#.##");
            txtDistance.setVisibility(View.VISIBLE);
            txtDistance.setText(decimalformat.format(distance) + " km");
        }
    }

    public void startGPSService(){
        Intent i = new Intent(getActivity(), GPSService.class);
        getActivity().startService(i);
    }

    public void getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location!= null){
                    currentLocation = location;
                }
                updateLocationALLCinema();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    public static void updateStatusBtnLocation(boolean isTurnOn){
        if(isTurnOn){
            ibtnvitri.setImageResource(R.drawable.ic_turnonlocation);
        }else{
            ibtnvitri.setImageResource(R.drawable.ic_turnofflocation);
        }
    }
}
