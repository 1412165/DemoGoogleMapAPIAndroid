package com.nthieuitus.demogooglemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapWrapperLayout mapWrapperLayout;
    View contentView;
    Button btnClick;

    OnInterInfoWindowTouchListener isClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_wrapper);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapWrapperLayout.init(mMap,this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng hcmus = new LatLng(10.762978,106.682561);

        mMap.addMarker(new MarkerOptions().position(hcmus).title("Location: HoChiMinh City University of Science")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(18),2000,null);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(hcmus)
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        contentView = LayoutInflater.from(this).inflate(R.layout.content,null);
        btnClick = (Button) contentView.findViewById(R.id.click);

        isClick = new OnInterInfoWindowTouchListener(btnClick) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Log.d("INFO WINDOW","Clicked");
            }
        };

        btnClick.setOnTouchListener(isClick);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                isClick.setMarker(marker);
                TextView tv_name = (TextView)contentView.findViewById(R.id.textView_name);
                TextView tv_location = (TextView)contentView.findViewById(R.id.textView_location);

                tv_name.setText(marker.getTitle());
                tv_location.setText(marker.getPosition().toString());

                mapWrapperLayout.setMarkerWithInfoWindow(marker,contentView);
                return contentView;
            }
        });
    }
}
