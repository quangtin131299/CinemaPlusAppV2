package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.ngolamquangtin.appdatvexemphim.Activity.CinemaActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.CinemaFilterAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.CinemaAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCinema extends Fragment {

    final double DISTANCE_NEAR_ME = 4.0;
    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    Location currentlocation;
    ImageButton ibtnvitri, imgBtnSearch;
    Spinner spinnerLocation;
    SwipeRefreshLayout refeshcinema;
    TextView txtthongbaokcoketnoi;
    ListView lvCinema;
    CinemaAdapter cinemaAdapter;
    CinemaFilterAdapter cinemaFilterAdapter;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    EditText edtKeyWork;
    Dialog dialogError;

    static ArrayList<Cinema> cinemas;

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
        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String optionFilter = (String) spinnerLocation.getSelectedItem();
                String keyWord = edtKeyWork.getText().toString();

                if(optionFilter.equals("Gần tôi")){
                    searchCinemaNearMe(currentlocation, keyWord);
                }else{
                    searchAllCinema(keyWord);
                }
            }
        });

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
                        updateStatusBtnLocation(true);
                        getCurrentLocation();
                    }
                } else {
                    updateStatusBtnLocation(false);
                }
            }
        });

        refeshcinema.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cinemas.clear();
                cinemaAdapter.notifyDataSetChanged();
                loadCinema();
            }
        });
    }

    public void searchCinemaNearMe(String keyWord, Integer[] idCinemas){
        Service service = RetrofitUtil.getService(getActivity());
        Call<ArrayList<Cinema>>  call = service.searchCinemaNearMe(keyWord,idCinemas);

        call.enqueue(new Callback<ArrayList<Cinema>>() {
            @Override
            public void onResponse(Call<ArrayList<Cinema>> call, Response<ArrayList<Cinema>> response) {
                cinemas.clear();

                if(response.body() != null  && response.body().size() != 0){
                    txtthongbaokcoketnoi.setVisibility(View.INVISIBLE);
                    lvCinema.setVisibility(View.VISIBLE);

                    cinemas.addAll(response.body());

                    updateLocationALLCinema(currentlocation);
                }else{
                    txtthongbaokcoketnoi.setText("Không tìm thấy rạp phim");
                    txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                    lvCinema.setVisibility(View.INVISIBLE);
                }

                cinemaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Cinema>> call, Throwable t) {
                txtthongbaokcoketnoi.setText("Không có kết nối");
                txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                lvCinema.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void updateLocationALLCinema(Location currentLocation) {
        if (cinemas.size() != 0 && currentLocation != null) {
            int slCinema = cinemas.size();
            for (int i = 0; i < slCinema; i++) {
                Cinema cinema = cinemas.get(i);
                updateDistance(i, cinema, currentLocation);
            }
        }
    }

    public void addControls(View view) {
        dialogError = new Dialog(getActivity());
        edtKeyWork = view.findViewById(R.id.edtkeywork);
        imgBtnSearch = view.findViewById(R.id.imgbtnsearch);
        cinemaFilterAdapter = new CinemaFilterAdapter(getActivity());
        spinnerLocation = view.findViewById(R.id.spinnertype);
        spinnerLocation.setAdapter(cinemaFilterAdapter);
        refeshcinema = view.findViewById(R.id.refeshcinema);
        ibtnvitri = view.findViewById(R.id.ibtnvitri);
        txtthongbaokcoketnoi = view.findViewById(R.id.txtthongbaokcoketnoi);
        lvCinema = view.findViewById(R.id.lvCinema);
        cinemas = new ArrayList<>();
        cinemaAdapter = new CinemaAdapter(getActivity().getApplicationContext(), cinemas, 1);

        if(getActivity() instanceof CinemaActivity){
            cinemaAdapter.setIsActivityCinema(true);
            cinemaAdapter.setIntent(getActivity().getIntent());
        }else{
            cinemaAdapter.setIsActivityCinema(false);
        }

        lvCinema.setAdapter(cinemaAdapter);

        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    public void loadCinema() {
        refeshcinema.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Cinema>> listCall = service.getListCinema();
        listCall.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, Response<List<Cinema>> response) {
                refeshcinema.setRefreshing(false);

                cinemas.clear();

                if (response.body() != null && response.body().size() != 0) {
                    txtthongbaokcoketnoi.setVisibility(View.INVISIBLE);
                    lvCinema.setVisibility(View.VISIBLE);
                    cinemas.addAll(response.body());

                    updateLocationALLCinema(currentlocation);

                } else {
                    txtthongbaokcoketnoi.setText("Không có rạp chiêu nào hoạt động");
                    txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                    lvCinema.setVisibility(View.INVISIBLE);
                }

                cinemaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {
                refeshcinema.setRefreshing(false);
                txtthongbaokcoketnoi.setText("Bạn bị ngắt kết nối !");
                txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                lvCinema.setVisibility(View.INVISIBLE);

                call.clone().enqueue(this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cinemas.clear();
        cinemaAdapter.notifyDataSetChanged();
        loadCinema();
    }

    public void updateDistance(int index, Cinema cinema, Location location) {
        if (lvCinema != null) {
            View v = lvCinema.getChildAt(index - lvCinema.getFirstVisiblePosition());

            if (v == null) {
                return;
            }

            TextView txtDistance = v.findViewById(R.id.tvdiachi);
            double distance = Util.distance(Double.parseDouble(cinema.getVido())
                                            , Double.parseDouble(cinema.getKinhdo())
                                            , location.getLatitude()
                                            , location.getLongitude());

            DecimalFormat decimalformat = new DecimalFormat("#.##");

            txtDistance.setVisibility(View.VISIBLE);
            
            txtDistance.setText(decimalformat.format(distance) + " km");
        }
    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            return;
        }

       fusedLocationClient.requestLocationUpdates(locationRequest,new LocationCallback(){
           @Override
           public void onLocationResult(LocationResult locationResult) {
               super.onLocationResult(locationResult);

               currentlocation = locationResult.getLastLocation();

               updateLocationALLCinema(currentlocation);

               fusedLocationClient.removeLocationUpdates(this);
           }
       }, Looper.getMainLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public  void updateStatusBtnLocation(boolean isTurnOn){
        if(isTurnOn){
            ibtnvitri.setImageResource(R.drawable.ic_turnonlocation);
        }else{
            ibtnvitri.setImageResource(R.drawable.ic_turnofflocation);
        }
    }

    public void searchAllCinema(String keyWord){
        Service service = RetrofitUtil.getService(getActivity());
        Call<ArrayList<Cinema>> call = service.searchAllMovie(keyWord);
        call.enqueue(new Callback<ArrayList<Cinema>>() {
            @Override
            public void onResponse(Call<ArrayList<Cinema>> call, Response<ArrayList<Cinema>> response) {
                cinemas.clear();

                if(response.body() != null && response.body().size() != 0){
                    txtthongbaokcoketnoi.setVisibility(View.INVISIBLE);
                    lvCinema.setVisibility(View.VISIBLE);
                    cinemas.addAll(response.body());
                }else{
                    txtthongbaokcoketnoi.setText("Không tìm thây rạp chiếu nào");
                    txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                    lvCinema.setVisibility(View.INVISIBLE);
                }

                cinemaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Cinema>> call, Throwable t) {
                txtthongbaokcoketnoi.setText("Bạn bị ngắt kết nối");
                txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                lvCinema.setVisibility(View.VISIBLE);
            }
        });
    }

    public void searchCinemaNearMe(Location location, String keyWord){
        if( location != null){
            ArrayList<Integer> cinemaSearchRequest = getCinemaNearMe( location);
            Integer[] idCinemasRequest = new Integer[cinemaSearchRequest.size()];

            cinemaSearchRequest.toArray(idCinemasRequest);

            searchCinemaNearMe(keyWord, idCinemasRequest);

        }else{
            showDialogError("Bạn chưa bật định vị");
        }
    }

    public ArrayList<Integer> getCinemaNearMe(Location currentLocation){
        ArrayList<Integer> cinemaSearchRequest = new ArrayList<>();

        if(cinemas != null && cinemas.size() != 0){
            int countCinema = cinemas.size();
            double currenLocationLat = currentLocation.getLatitude();
            double currentLocationLng = currentLocation.getLongitude();
            double distance = 0;

            for (int i = 0; i < countCinema; i++) {
                Cinema cinema = cinemas.get(i);
                distance = Util.distance(currenLocationLat, currentLocationLng, Double.parseDouble(cinema.getVido()), Double.parseDouble(cinema.getKinhdo()));

                if (distance < DISTANCE_NEAR_ME) {
                    cinemaSearchRequest.add(cinema.getId());
                }
            }
        }

        return  cinemaSearchRequest;
    }

    private void showDialogError(String mess){
        if(dialogError != null){
            dialogError.setContentView(R.layout.dialog_failed);
            dialogError.getWindow().setBackgroundDrawableResource(R.color.transparent);
            TextView txtMess = dialogError.findViewById(R.id.txtmess);
            Button btnOk = dialogError.findViewById(R.id.btnOK);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialogError();
                }
            });

            txtMess.setText(mess);
            dialogError.show();
        }

    }

    private void dismissDialogError(){
        if(dialogError != null && dialogError.isShowing()){
            dialogError.dismiss();
        }
    }

}
