package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    View view;
    String base_url = "http://132.226.10.241:11222/";
    String weather_url = base_url + "weather/";

    TextClock textclock;
    TextView Showweek;

    JSONObject jsonObject;
    TextView area;
    TextView weather;
    TextView MaxT;

    TextView tv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

            //顯示時間
            textclock.setFormat24Hour("hh:mm");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //宣告findviewbyid
        Showweek = view.findViewById(R.id.week);
        area = view.findViewById(R.id.area);
        weather = view.findViewById(R.id.weather);
        MaxT = view.findViewById(R.id.temperature);

        tv=view.findViewById(R.id.tv);

        ShowWeek();
        ShowWeather("120.52","23.68");



        return view;
    }


    public void ShowWeek() {
        String week = "";

        Time mTime = new Time("GMT+8");
        mTime.setToNow();

        switch(mTime.weekDay){
            case 1:
                week="Mon ";
                break;
            case 2:
                week="Tue ";
                break;
            case 3:
                week="Wed ";
                break;
            case 4:
                week="Thu ";
                break;
            case 5:
                week="Fri ";
                break;
            case 6:
                week="Sat ";
                break;
            case 0:
                week="Sun ";
                break;
        }
        Showweek.setText(week);
    }


    public void ShowWeather(String Longitude, String Latitude) {
        final String REGISTER_URL = weather_url + Longitude + "/" + Latitude;
        Utf8StringRequest stringRequest = new Utf8StringRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //天氣顯示
                        try {
                            jsonObject = new JSONObject(response);
                            area.setText(jsonObject.get("location").toString());
                            weather.setText(jsonObject.get("weather").toString());
                            MaxT.setText(jsonObject.get("MaxT").toString() + "°C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tv.setText("");
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