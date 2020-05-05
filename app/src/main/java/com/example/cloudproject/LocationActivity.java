package com.example.cloudproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {
    private ArrayList<JSONObject> locations_list = new ArrayList<>();
    private ArrayList<JSONObject> filtered_list = new ArrayList<>();
    private Menu main_menu;
    RecyclerView recyclerView;
    LocationViewAdapter adapter;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        search = findViewById(R.id.searchbar);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                filtered_list.clear();

                for (int i = 0; i < locations_list.size(); i++) {
                    JSONObject obj= locations_list.get(i);
                    try {
                        if(obj.get("name").toString().toLowerCase().contains(search.getText().toString().trim().toLowerCase()) || obj.get("location").toString().toLowerCase().contains(search.getText().toString().trim().toLowerCase()))
                        {
                            filtered_list.add(obj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                initRecyclerView(filtered_list);

            }

            @Override
            public void afterTextChanged(Editable s)
            {


            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://52.90.211.209:81/location/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONArray locations = ((JSONArray) response.get("data"));
                            for (int i=0; i<locations.length(); i++) {
                                locations_list.add((JSONObject) locations.get(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.toString());

                    }
                }
        );

        requestQueue.add(objectRequest);
        initRecyclerView(locations_list);
    }
    private void initRecyclerView(ArrayList<JSONObject> list) {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LocationViewAdapter(list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        main_menu = menu;
        return true;
    }
}
