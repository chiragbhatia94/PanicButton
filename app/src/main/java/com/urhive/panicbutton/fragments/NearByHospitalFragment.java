package com.urhive.panicbutton.fragments;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.Place_JSON;
import com.urhive.panicbutton.helpers.UIHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NearByHospitalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearByHospitalFragment extends FragmentBase implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com
                .google.android.gms.location.LocationListener {

    private static final String TAG = NearByHospitalFragment.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 12;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static double distanceInMeters;
    private static View view;
    private final LatLng mDefaultLocation = new LatLng(22.70444453, 75.85189242);
    TextView latlngTV, addressTV;
    LinearLayout mainLL;
    private boolean mLocationPermissionGranted;
    private Polyline gpsTrack;
    private CameraPosition mCameraPosition;
    private float current_zoom_level = DEFAULT_ZOOM;
    private boolean firstTimeCenter = true;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private LatLng lastKnownLatLng;

    public NearByHospitalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HiveFragment.
     */
    public static NearByHospitalFragment newInstance() {
        NearByHospitalFragment fragment = new NearByHospitalFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            *//*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*//*
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_nearby_hospital, container, false);
            latlngTV = (TextView) view.findViewById(R.id.latlngTV);
            addressTV = (TextView) view.findViewById(R.id.addressTV);
            mainLL = (LinearLayout) view.findViewById(R.id.mainLL);

            mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R
                    .id.mapsFragment);
            mapFragment.getMapAsync(this);

            if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(getContext()).addApi(Places
                        .GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).addConnectionCallbacks
                        (this).addOnConnectionFailedListener(this).addApi(LocationServices.API)
                        .build();
            }
        } catch (InflateException e) {
            // map is already there, just return view as it is
        }
        return view;
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest
                .permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest
                    .permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setAllGesturesEnabled(true);
        } else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void updateAddress(Location location) {
        try {
            double lng = location.getLongitude();
            double lat = location.getLatitude();
            latlngTV.setText(getString(R.string.lat) + " " + lat + " " + "&" + " " + getString(R
                    .string.lng) + " " + lng);
            if (!UIHelper.isOffline(getContext())) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(lat, lng, 1);
                if (list != null) {
                    try {
                        if (list.get(0) != null) {
                            String address = list.get(0).getAddressLine(0) + " " + list.get(0)
                                    .getAddressLine(1) + "\n" + list.get(0).getAddressLine(2) +
                                    "\n" + list.get(0).getAddressLine(3);
                            addressTV.setText(address);
                        }
                        return;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // do not do anything
                    }
                }
            } else {
                String prevAddr = addressTV.getText().toString();
                if (prevAddr.isEmpty())
                    addressTV.setText(getString(R.string.u_r_currently_offline));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String prevAddr = addressTV.getText().toString();
        if (prevAddr.isEmpty()) addressTV.setText(R.string.address_not_found);
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateAddress(location);

        lastKnownLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Location mLastKnownLocation = new Location("");
        if (firstTimeCenter) {
            mLastKnownLocation.setLatitude(lastKnownLatLng.latitude);
            mLastKnownLocation.setLongitude(lastKnownLatLng.longitude);

            // Set the map's camera position to the current location of the device.
            if (mCameraPosition != null) {
                map.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation
                        .getLatitude(), mLastKnownLocation.getLongitude()), current_zoom_level));
            } else {
                Log.d(TAG, "Current location is null. Using defaults.");
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            }

            StringBuilder sbValue = new StringBuilder(sbMethod());
            PlacesTask placesTask = new PlacesTask();
            placesTask.execute(sbValue.toString());
            firstTimeCenter = false;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        updateLocationUI();
        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest
                .permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest
                    .permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest, this);
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        } catch (Exception ignored) {

        }
    }

    private void updateTrack() {
        List<LatLng> points = gpsTrack.getPoints();
        points.add(lastKnownLatLng);
        if (points.size() > 2) {
            Location source = new Location("");
            source.setLatitude(points.get(points.size() - 2).latitude);
            source.setLongitude(points.get(points.size() - 2).longitude);

            Location destination = new Location("");
            destination.setLongitude(points.get(points.size() - 1).longitude);
            destination.setLatitude(points.get(points.size() - 1).latitude);

            distanceInMeters += source.distanceTo(destination);
        }
        Snackbar.make(mainLL, "distance is: " + distanceInMeters, Snackbar.LENGTH_SHORT).show();
        gpsTrack.setPoints(points);
    }

    public StringBuilder sbMethod() {
        //use your current location here
        double mLatitude = lastKnownLatLng.latitude;
        double mLongitude = lastKnownLatLng.longitude;

        StringBuilder sb = new StringBuilder("https://maps.googleapis" + "" + "" + "" + "" + "" +
                ".com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=5000");
        sb.append("&types=" + "hospital");
        sb.append("&sensor=true");
        sb.append("&key=" + getString(R.string.pb_google_api_key));

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d(TAG, "Exception while downloading url" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            try {
                Log.d("Map", "list size: " + list.size());
                // Clears all the existing markers;
                map.clear();

                for (int i = 0; i < list.size(); i++) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);


                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    String name = hmPlace.get("place_name");

                    Log.d("Map", "place: " + name);

                    // Getting vicinity
                    String vicinity = hmPlace.get("vicinity");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    markerOptions.title(name + " : " + vicinity);

                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker
                            (BitmapDescriptorFactory.HUE_MAGENTA));

                    // Placing a marker on the touched position
                    Marker m = map.addMarker(markerOptions);

                }
            } catch (NullPointerException e) {
                // ignore
            }
        }
    }
}
