package com.braoul.github30days;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braoul.github30days.Adapter.RepoAdapter;
import com.braoul.github30days.Interface.LoadMore;
import com.braoul.github30days.Model.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRepositories;
    private ArrayList<Repository> repositories = new ArrayList<>();
    private RepoAdapter adapter;

    private int totalCount = -1;

    private JsonObjectRequest request;
    private int page = 1;
    private String url;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());

        url = "https://api.github.com/search/repositories?q=created:>" + date + "&sort=stars&order=desc&page=";

        loadData(url + page++);

        rvRepositories = findViewById(R.id.repositories_recycler_view);
        rvRepositories.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RepoAdapter(rvRepositories, this, repositories);
        rvRepositories.setAdapter(adapter);

        adapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (page < totalCount) {
                    repositories.add(null);
                    adapter.notifyItemInserted(repositories.size() - 1);
                    rvRepositories.post(new Runnable() {
                        @Override
                        public void run() {
                            loadData(url + page++);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "no more data to load", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void loadData(String url) {
        Log.d("URL", url);
        request = new RepositoryRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!repositories.isEmpty()) {
                    repositories.remove(repositories.size() - 1);
                    adapter.notifyItemRemoved(repositories.size());
                }
                try {
                    if (totalCount == -1) {
                        if (response.getInt("total_count") > 1000)
                            totalCount = 1000;
                        else
                            totalCount = response.getInt("total_count");
                    }
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
                        repositories.add(repo);
                        Log.d("Repository_" + i, repo.getName());
                    }
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}