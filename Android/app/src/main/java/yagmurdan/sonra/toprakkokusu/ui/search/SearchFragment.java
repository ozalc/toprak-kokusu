package yagmurdan.sonra.toprakkokusu.ui.search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.R;

public class SearchFragment extends Fragment {

    public EditText EditTextSearchFragment;
    protected Button ButtonSearchFragment;
    //Map
    FusedLocationProviderClient fusedLocationClient;
    GoogleMap gMap;
    double Latitude, Longitute;
    List<Double> latitudeList = new ArrayList<>();
    List<Double> longituteList = new ArrayList<>();
    List<String> campingNameList = new ArrayList<>();
    List<String> cityName = new ArrayList<>();


    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            getAllCampingLocation();
            gMap = googleMap;
            Latitude = 38.95;
            Longitute = 35.2;
            LatLng latLng = new LatLng(Latitude, Longitute);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 5);
            gMap.animateCamera(location);


            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gMap.setMyLocationEnabled(true);
                return;
            }



        }
    };



    public SearchFragment() {
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.searchFragmentMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditTextSearchFragment = view.findViewById(R.id.EditTextSearchFragment);
        ButtonSearchFragment = view.findViewById(R.id.ButtonSearchFragment);

        ButtonSearchFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle yapısı ile fragment'tan fragment'a veri gönderme
                //Kullanıcının girdiği şehir-kamp adı bilgisi diğer fragment'a gönderilir
                SearchFragmentResultFragment searchFragmentResultFragment = new SearchFragmentResultFragment();
                Bundle bundle = new Bundle();
                bundle.putString("EditTextValue", EditTextSearchFragment.getText().toString());

                searchFragmentResultFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragmentResultFragment).commit();


            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        return view;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();

        } else {
            Toast.makeText(getActivity(), "İzin Alınamadı!", Toast.LENGTH_SHORT).show();
        }
    }



    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        Latitude = location.getLatitude();
                        Longitute = location.getLongitude();
                    } else {
                        @SuppressLint("RestrictedApi") LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                Latitude = location1.getLatitude();
                                Longitute = location1.getLongitude();
                            }
                        };
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }



    private void getAllCampingLocation(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camping Areas");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot campingSnapshot : snapshot.getChildren()){
                    CampingArea campingArea = campingSnapshot.getValue(CampingArea.class);
                    latitudeList.add(campingArea.getLatitute());
                    longituteList.add(campingArea.getLongitute());
                    campingNameList.add(campingArea.getName());
                    cityName.add(campingArea.getLocation());
                }
                setAllMarkers();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAllMarkers(){
        for (int i=0; i<latitudeList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng campingLatlng = new LatLng(latitudeList.get(i),longituteList.get(i));
            markerOptions.position(campingLatlng);
            markerOptions.title(campingNameList.get(i)+" "+cityName.get(i));
            gMap.addMarker(markerOptions);
        }
    }
}