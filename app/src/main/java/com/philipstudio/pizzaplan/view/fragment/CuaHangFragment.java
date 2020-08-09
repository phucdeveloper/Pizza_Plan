package com.philipstudio.pizzaplan.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philipstudio.pizzaplan.R;

public class CuaHangFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mGoogleMap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cua_hang, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMapClickListener(this);

        setUpViTriCacCuaHang();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showMarkerInGoogleMap(latLng, "Current Marker");
    }

    private void showMarkerInGoogleMap(LatLng latLng, String title) {
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f));
    }

    private void setUpViTriCacCuaHang() {
        LatLng latLng1 = new LatLng(10.760398, 106.670878);
        showMarkerInGoogleMap(latLng1, "91 Nguyễn Chí Thanh, phường 9, Quận 5, Hồ Chí Minh");
        LatLng latLng2 = new LatLng(10.773527, 106.678397);
        showMarkerInGoogleMap(latLng2, "162 Cao Thắng, Quận 10, Hồ Chí Minh");
        LatLng latLng3 = new LatLng(10.770437, 106.676542);
        showMarkerInGoogleMap(latLng3, "567 Điện Biên Phủ, phường 1, Quận 3, Hồ Chí Minh");
        LatLng latLng4 = new LatLng(10.796334, 106.666035);
        showMarkerInGoogleMap(latLng4, "266 Đường Lê Văn Sỹ, Phường 1, Tân Bình, Hồ Chí Minh");
        LatLng latLng5 = new LatLng(10.810307, 106.695229);
        showMarkerInGoogleMap(latLng5, "132 Nơ Trang Long, phường 14, Bình Thạnh, Hồ Chí Minh");
        LatLng latLng6 = new LatLng(10.757893, 106.700130);
        showMarkerInGoogleMap(latLng6, "56 Đường Khánh Hội, phường 4, Quận 4, Hồ Chí Minh");
    }
}
