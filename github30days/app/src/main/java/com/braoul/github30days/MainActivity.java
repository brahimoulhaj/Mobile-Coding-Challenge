package com.braoul.github30days;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private JsonObjectRequest request;
    private String url = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        request = new RepositoryRequest(url);
        Volley.newRequestQueue(this).add(request);

    }
}