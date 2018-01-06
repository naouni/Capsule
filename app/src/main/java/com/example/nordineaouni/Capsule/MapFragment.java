package com.example.nordineaouni.Capsule;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by nordineaouni on 27/06/17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    String TAG = getClass().getSimpleName();
    MapView mapView;
    GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private CameraPosition cameraPosition;
    private final LatLng defaultLocation = new LatLng(50.8483300, 5.6888900);//Sydney's coordinates
    private static final int DEFAULT_ZOOM = 15;


    /**
     * Factory method to create such fragments
     * @return MapFragment's instance
     */
    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);

        //API required to get the device's location
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        //retrieve a MapView object from the xml layout
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        /* This is an asynchronous method that will instantiate this fragment's GoogleMap instance
        field. It works with a callback (onMapReady(GoogleMap googleMap)) that sets up the GoogleMap
         using a MapView. The Mapview object is taken from the xml layout of this fragment.
        */
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //The following line displays the +/- zoom buttons
        //this.googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void getDeviceLocation() {
    /*
    * Request location permission, so that we can get the location of the
    * device. The result of the permission request is handled by a callback,
    * onRequestPermissionsResult.
    */
     if (ContextCompat.checkSelfPermission(this.getContext(),
             android.Manifest.permission.ACCESS_FINE_LOCATION)
             == PackageManager.PERMISSION_GRANTED) {
         locationPermissionGranted = true;
     } else {
         ActivityCompat.requestPermissions(this.getActivity(),
                 new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                 PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
     }
    /*
    * Get the best and most recent location of the device, which may be null in rare
    * cases when a location is not available.
    */
     if (locationPermissionGranted) {
         lastKnownLocation = LocationServices.FusedLocationApi
                 .getLastLocation(googleApiClient);
     }

     // Set the map's camera position to the current location of the device.
     if (cameraPosition != null) {
         googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
     } else if (lastKnownLocation != null) {
         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                 new LatLng(lastKnownLocation.getLatitude(),
                         lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
     } else {
         Log.d(TAG, "Current location is null. Using defaults.");
         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
         googleMap.getUiSettings().setMyLocationButtonEnabled(false);
     }
    }


    private void updateLocationUI() {
        if (googleMap == null) {
         return;
        }

        /*
        * Request location permission, so that we can get the location of the
        * device. The result of the permission request is handled by a callback,
        * onRequestPermissionsResult.
        */
        if (ContextCompat.checkSelfPermission(this.getContext(),
             android.Manifest.permission.ACCESS_FINE_LOCATION)
             == PackageManager.PERMISSION_GRANTED) {
         locationPermissionGranted = true;
        } else {
         ActivityCompat.requestPermissions(this.getActivity(),
                 new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                 PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (locationPermissionGranted) {
         googleMap.setMyLocationEnabled(true);
         googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
         googleMap.setMyLocationEnabled(false);
         googleMap.getUiSettings().setMyLocationButtonEnabled(false);
         lastKnownLocation = null;
        }
    }

    // Callback handling permission request's result. In this case, access to the device's location
    // is requested.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                        @NonNull String permissions[],
                                        @NonNull int[] grantResults) {
     Toast.makeText(getContext(), "onRequestPermission MAP", Toast.LENGTH_SHORT).show();
     locationPermissionGranted = false;
     switch (requestCode) {
         case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
             // If request is cancelled, the result arrays are empty.
             if (grantResults.length > 0
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 locationPermissionGranted = true;
             }
         }
     }
     updateLocationUI();
    }

    //Callback for the Google API Client
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    //Callback for the Google API Client
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "API Client: onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    //Callback for the Google API Client
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "API Client: onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    /*
    As described in the MapView class documentation the entire lifecycle methods are passed from
    fragment to the MapView object (in order to avoid any memory leaks I believe).
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}