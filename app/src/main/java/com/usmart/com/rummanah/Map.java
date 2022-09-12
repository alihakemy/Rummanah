package com.usmart.com.rummanah;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Activity activity = Map.this;
    GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    Button btn_Set, btn_Skipe;
    String Name;
    TextView MainTitle;
    double Lat, Lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        GoogleMapOptions options = new GoogleMapOptions();
        options.liteMode(true).mapToolbarEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MainTitle = findViewById(R.id.MainTitle);
        Typeface fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");
        MainTitle.setTypeface(fontUltra);
        Name = getIntent().getExtras().getString("Name");
        Lat = getIntent().getExtras().getDouble("Lat");
        Lng = getIntent().getExtras().getDouble("Lng");

        MainTitle.setText(Name + "");
        // Name = "Test Name";
        // Lat = 30.1024287;
        // Lng = 31.342715;

        btn_Set = findViewById(R.id.btn_Set);
        btn_Skipe = findViewById(R.id.btn_Skipe);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");
        btn_Set.setTypeface(font);
        btn_Skipe.setTypeface(font);

        btn_Skipe.setVisibility(View.INVISIBLE);

        btn_Set.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "http://maps.google.com/maps?daddr=" + Lat + ", " + Lng + ""));
                startActivity(intent);
            } catch (Exception e) {

            }
        });

        btn_Skipe.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        addLocation(Lat, Lng, Name);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        btn_Set.setVisibility(View.GONE);
        btn_Skipe.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        btn_Set.setVisibility(View.GONE);
        btn_Skipe.setVisibility(View.GONE);
    }


    private void setUpMapIfNeeded() {

        if (mMap == null) {
            //getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void addLocation(double lat, double lng, String Name) {

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(Name);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mCurrLocationMarker.showInfoWindow();

        mMap.setOnMarkerClickListener(marker -> false);
        mMap.setOnInfoWindowClickListener(marker -> marker.hideInfoWindow());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    public void gotoUpload(View v) {
        Intent i = new Intent(activity, Menu.class);
        startActivity(i);
        overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
    }

    public void gotoBack(View v) {
        super.onBackPressed();
    }


}
