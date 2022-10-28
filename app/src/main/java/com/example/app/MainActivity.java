package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.app.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static android.app.FragmentManager manager;

    private PhoneCallListener mPhoneState;
    public static final String TAG = "getLocal: ";

    String base_url = "http://132.226.10.241:11222/";
    String user = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragement(new HomeFragment());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }


        //phone
        mPhoneState = new PhoneCallListener();
        ((TelephonyManager) Objects.requireNonNull(getSystemService(Context.TELEPHONY_SERVICE)))
                .listen(mPhoneState, PhoneCallListener.LISTEN_CALL_STATE);

        //bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.home:
                    replaceFragement(new HomeFragment());
                    break;
                case R.id.setstring:
                    replaceFragement(new SetStringFragment());
                    break;
                /*
                case R.id.location:
                    replaceFragement(new MapsFragment());
                    break;
                */
                case R.id.profile:
                    replaceFragement(new ProfileFragment());
                    break;

                case R.id.settings:
                    replaceFragement(new SettingsFragment());
                    break;

            }
            return  true;
        });
    }

    //bottom navigation replaceFragement
    private void replaceFragement(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public class PhoneCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int currentCallState, String incomingNumber) {

            if (currentCallState == TelephonyManager.CALL_STATE_IDLE) {// 空閒
//TODO
                //tab1_phonenumber_tv.setText("待機");
            } else if (currentCallState == TelephonyManager.CALL_STATE_RINGING) {// 響鈴
//TODO
                //tab1_phonenumber_tv.setText("來電號碼 : " + incomingNumber);
            } else if (currentCallState == TelephonyManager.CALL_STATE_OFFHOOK) {// 接聽
//TODO
            }
            super.onCallStateChanged(currentCallState, incomingNumber);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getLocal();
    }

    private void getLocal() {
        /**沒有權限則返回*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String localProvider = "";
        /**知道位置後..*/
        Location location = manager.getLastKnownLocation(localProvider);
        if (location != null){
            updateLocation(location);
        }else{
            Log.d(TAG, "getLocal: ");
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, mListener);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, mListener);
        }
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
        final String REGISTER_URL = base_url + "GPS/" + user + "/" + location.getLongitude() + "/" + location.getLatitude();

        Utf8StringRequest stringRequest = new Utf8StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("info","response : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error","onErrorResponse");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

}