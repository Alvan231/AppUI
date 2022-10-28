package com.example.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment{

    private View view;
    String base_url = "http://132.226.10.241:11222/";
    JSONObject jsonObject;
    GoogleMap map;

    private Button start_btn;
    LatLng location_now;

    List<LatLng> latLngList = new ArrayList<LatLng>();
    private ArrayList<LatLng> arrayPoints = new ArrayList<>();
    PolylineOptions polylineOptions=new PolylineOptions();




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        start_btn = view.findViewById(R.id.start_record);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getGPS("admin");

                //LatLng local_now;

                //getLastLocation();
                //local_now = location_now;

                getLocal();



                Log.d("Test", "Onclick");
                //Log.d("LatLng", "local_now : " + local_now);
            }
        });

        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                map = googleMap;

                /*
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options

                        MarkerOptions markerOptions=new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);

                    }
                });
                */

                Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                        .add(
                                new LatLng(-35.016, 143.321),
                                new LatLng(-34.747, 145.592),
                                new LatLng(-34.364, 147.891),
                                new LatLng(-33.501, 150.217),
                                new LatLng(-32.306, 149.248)
                                //new LatLng(-32.491, 147.309)
                        ));



                // Position the map's camera near Alice Springs in the center of Australia,
                // and set the zoom factor so most of Australia shows on the screen.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 5));
                Log.d("latLngList", "latLngList : " + latLngList);
            }
        });

        return view;
    }

    public void draw(LatLng latLng) {
        if(latLngList.size()>1) {
            map.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(latLngList.get(latLngList.size() - 1).latitude, latLngList.get(latLngList.size() - 1).longitude),
                            new LatLng(latLng.latitude, latLng.longitude)));
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 10));
    }

    public void getLocal() {
        /**沒有權限則返回*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String localProvider = "";
        /**知道位置後..*/
        Location location = manager.getLastKnownLocation(localProvider);
        if (location != null){
            startRecord(location);
        }else{
            Log.d("getLocal: ", "");
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, LocalListener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, LocalListener);
        }
    }

    /**監聽位置變化*/
    LocationListener LocalListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            startRecord(location);
        }
    };



    private void startRecord(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        draw(latLng);
        latLngList.add(latLng);


        Log.d("latLngList", "latLngList : " + latLngList);
    }


    //後面不用看

    public LatLng getLastLocation() {
        LatLng latLng = null;
        /**沒有權限則返回*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
        }
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String localProvider = "";

        Location location = manager.getLastKnownLocation(localProvider);

        if (location != null){
            updateLocation(location);
        }else{
            Log.d("getlocal", "getLocal: ");
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mListener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mListener);

        }
        latLng = location_now;
        return latLng;
    }

    /**監聽位置變化*/
    LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }
    };

    private void updateLocation(Location location){
        location_now = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("LatLng", "location_now : " + location_now);
    }

    public void getGPS(String user) {
        final String REGISTER_URL = base_url + "GPS/" + user;
        Utf8StringRequest stringRequest = new Utf8StringRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray arr = jsonObject.getJSONArray("Result");

                            for (int i = 0; i < arr.length(); i++) {
                                String longitude = arr.getJSONObject(i).getString("longitude");
                                String latitude = arr.getJSONObject(i).getString("latitude");
                                String Time = arr.getJSONObject(i).getString("time");

                                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                //latLngList.add(point);


                                Log.d("Test","longitude : " + longitude + " " + "latitude : " + latitude);
                                Log.d("Test","Time : " + Time);
                            }
                            Log.d("Test","latLngList : " + latLngList);
                            Log.d("Test","latLngList : " + latLngList.get(0).latitude);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("info","response : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error","onErrorResponse");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

}