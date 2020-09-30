package com.braoul.github30days;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RepositoryRequest extends JsonObjectRequest {

    public RepositoryRequest(String url) {
        super(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Repository repo = new Repository(
                                item.getString("name"),
                                item.getString("description"),
                                item.getInt("stargazers_count"),
                                item.getJSONObject("owner").getString("login"),
                                item.getJSONObject("owner").getString("avatar_url")
                        );
                        Log.d("Repository_" + i, repo.getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
