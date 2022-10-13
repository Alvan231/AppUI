package com.example.app;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import java.io.UnsupportedEncodingException;

//解決中文亂碼用Utf8StringRequest
public class Utf8StringRequest extends StringRequest {

    public Utf8StringRequest(int method, String url,
                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, (Response.ErrorListener) errorListener);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
