package com.usmart.com.rummanah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.location.LocationListener;

import java.util.List;
import java.util.Locale;

import helpers.AccessHolder;
import helpers.OpenHolder;

public class MapArea extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Activity activity = MapArea.this;
    GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    Button btn_Set;
    String AreaName;
    double Lat, Lng;
    Location mLastLocation;
    Typeface fontMedim, fontLight, fontUltra;
    private static final int INITIAL_REQUEST = 1337;
    private static final int Fine_REQUEST = INITIAL_REQUEST + 5;
    private static final int Coarse_REQUEST = INITIAL_REQUEST + 10;
    private static final String[] Fine_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final String[] Coarse_PERMS = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter);

        AccessHolder.getInstance().setData(0);
        OpenHolder.getInstance().setData(0);
        fontMedim = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        fontLight = Typeface.createFromAsset(activity.getAssets(), "fonts/GE_SS_Two_Light.otf");
        fontUltra = Typeface.createFromAsset(activity.getAssets(), "fonts/ArbFONTS-GESSTextUltraLight-UltraLight.otf");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        GoogleMapOptions options = new GoogleMapOptions();
        options.liteMode(true).mapToolbarEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //*************************************************
        View mapView = mapFragment.getView();
        if (mapView != null &&
                mapView.findViewById(1) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 160);
        }
        //*************************************************

                mapFragment.getMapAsync(this);

        //   Lat = 30.1024287;
        //   Lng = 31.342715;
       /* Name = getIntent().getExtras().getString("Name");
        Lat = getIntent().getExtras().getDouble("Lat");
        Lng = getIntent().getExtras().getDouble("Lng");*/
        // Name = "Test Name";
        // Lat = 30.1024287;
        // Lng = 31.342715;


        btn_Set = findViewById(R.id.btn_Set);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");
        btn_Set.setTypeface(font);


        btn_Set.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddAddress.class);
          //  intent.putExtra("AreaName", AreaName + "");
            intent.putExtra("Lat", Lat);
            intent.putExtra("Lng", Lng);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein_fast, R.anim.fadeout_fast);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        mMap.setOnCameraIdleListener(() -> {
            Lat = mMap.getCameraPosition().target.latitude;
            Lng = mMap.getCameraPosition().target.longitude;
            try {
                Geocoder gcd = new Geocoder(activity, Locale.getDefault());
                List<Address> addresses = null;

                addresses = gcd.getFromLocation(Lat, Lng, 1);
                //   AreaName = addresses.get(0).getLocality();
                AreaName = addresses.get(0).getAdminArea();
                Log.i("TestApp_Location", addresses.get(0).getAdminArea() + " - " + addresses.get(0).getLocality() + " - " +
                        addresses.get(0).getLocale().getDisplayName());
            } catch (Exception e) {
            }


        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationButtonClickListener(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Request Location Permission
                    checkLocationPermission();
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            return true;
        });

        //  addLocation(Lat, Lng, Name);
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        btn_Set.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        btn_Set.setVisibility(View.GONE);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();

        }
        btn_Set.setVisibility(View.VISIBLE);
        //Place current location marker
        Lat = location.getLatitude();
        Lng = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        // mCurrLocationMarker = mMap.addMarker(markerOptions);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            //getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
      /*  if (!canAccessFine()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(Fine_PERMS, Fine_REQUEST);
            }
        }
        if (!canAccessCoarse()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(Coarse_PERMS, Coarse_REQUEST);
            }
        }*/

        CheckGpsStatus();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        setUpMapIfNeeded();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(activity)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        }
    }

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

    public void addLocation(LatLng latLng, String Name) {

        //  LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(Name);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        //   mCurrLocationMarker = mMap.addMarker(markerOptions);
        //  mCurrLocationMarker.showInfoWindow();

        mMap.setOnMarkerClickListener(marker -> false);
        mMap.setOnInfoWindowClickListener(marker -> marker.hideInfoWindow());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

        Lat = latLng.latitude;
        Lng = latLng.longitude;
        AreaName = Name;
    }

    private void CheckGpsStatus() {
        LocationManager locationManager;
        boolean GpsStatus;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == true) {

            if (AccessHolder.getInstance().getData() == 1) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }/* else {
                buildGoogleApiClient();
            }*/

        } else {
            if (OpenHolder.getInstance().getData() == 0) {
                loadGPS();
            }
        }
    }

    private void loadGPS() {
        OpenHolder.getInstance().setData(1);
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_noads3);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView Title = dialog.findViewById(R.id.MsgTitle);
        TextView Text = dialog.findViewById(R.id.MsgText);

        Button Yes = dialog.findViewById(R.id.btn_Yes);
        Button No = dialog.findViewById(R.id.btn_No);

        Yes.setText(R.string.GoOpenGPS);
        Text.setText(R.string.OpenGPS);
        //    No.setVisibility(View.GONE);


        Yes.setTypeface(fontMedim);
        No.setTypeface(fontMedim);
        Title.setTypeface(fontMedim);
        Text.setTypeface(fontUltra);

        Yes.setOnClickListener(v -> {
            AccessHolder.getInstance().setData(1);
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
            dialog.dismiss();
            OpenHolder.getInstance().setData(0);
        });
        No.setOnClickListener(v -> {
            dialog.dismiss();
            OpenHolder.getInstance().setData(0);
            CheckGpsStatus();
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private boolean canAccessFine() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCoarse() {
        return (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return false;
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
