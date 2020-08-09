package com.philipstudio.pizzaplan.view;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.utils.VitriUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    EditText edtTimKiem;
    Button btnChon;
    private String result;

    LocationManager locationManager;
    private boolean isLocationPermissionGranted = false;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    static final float DEFAULT_ROOM = 15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        edtTimKiem = findViewById(R.id.edittext_timkiemvitri);
        btnChon = findViewById(R.id.button_chon);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        getLocationPermission();
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    LatLng latLng = new LatLng(latitude, longitude);
//
//                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        String text = addressList.get(0).getLocality();
//                        text += addressList.get(0).getCountryName();
//                        showMarkerInGoogleMap(latLng, text);
//                        showCameraToPosition(latLng, 12.5f);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//
//                }
//            });
//        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    LatLng latLng = new LatLng(latitude, longitude);
//
//                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        String text = addressList.get(0).getLocality();
//                        text += addressList.get(0).getCountryName();
//                        showMarkerInGoogleMap(latLng, text);
//                        showCameraToPosition(latLng, 12.5f);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//
//                }
//            });
//        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (isLocationPermissionGranted){
            getCurrentLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtTimKiem.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(MapsActivity.this, "Bạn chưa chọn địa điểm", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("diachi", address);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (listAddress != null && listAddress.size() > 0) {
                Address address = listAddress.get(0);
                result = address.getAddressLine(0) + ", " + address.getLocality();
                edtTimKiem.setText(result);
                LatLng latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
                showMarkerInGoogleMap(latLng1, result);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isLocationPermissionGranted = false;
    }

    private void getCurrentLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    VitriUtils vitriUtils = new VitriUtils(MapsActivity.this);
                    vitriUtils.setVitriNguoiDung(lat, lng);
                    LatLng latLng = new LatLng(lat, lng);
                    showCameraToPosition(latLng, DEFAULT_ROOM);
                    showMarkerInGoogleMap(latLng, "My current location");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (SecurityException securityException) {
            Log.d("phuc", securityException.getMessage());
        }
    }

    private void showMarkerInGoogleMap(LatLng latLng, String text) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(text));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
    }

    private void showCameraToPosition(LatLng position, float zoomLevel) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(position)
                .zoom(zoomLevel)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
        }
    }
}