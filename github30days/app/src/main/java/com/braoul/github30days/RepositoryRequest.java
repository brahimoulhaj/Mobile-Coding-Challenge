package com.braoul.github30days;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.HashMap;
import java.util.Map;

public class RepositoryRequest extends JsonObjectRequest {

    public RepositoryRequest(String url, Response.Listener responseListener) {
        super(Request.Method.GET, url, null, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.getMessage());
            }
        });
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("Accept", "application/json");
        return params;
    }
}
