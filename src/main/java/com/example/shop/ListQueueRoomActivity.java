package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListQueueRoomActivity extends AppCompatActivity {
    String URL = "http://10.0.2.2:8000/queue_room/entered";
    SharedPreferences PreferenceStorage;
    RecyclerView queueRoomRecycler;
    QueueRoomAdapter queueRoomAdapter;
    List<QueueRoomModel> queueRoomModelList = new ArrayList<>();
    private String JWT_TOKEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_queue_room);
        PreferenceStorage = this.getSharedPreferences("com.example.shop", Context.MODE_PRIVATE);
        JWT_TOKEN = PreferenceStorage.getString("JWT_TOKEN", "");
        setQueueRoomRecycler();
    }

    public void setQueueRoomRecycler() {
        request_get_list_queue_room();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        queueRoomRecycler = findViewById(R.id.queueRoomRecycler);
        queueRoomRecycler.setLayoutManager(layoutManager);
        queueRoomAdapter = new QueueRoomAdapter(this, queueRoomModelList);
        queueRoomRecycler.setAdapter(queueRoomAdapter);
    }

    private void request_get_list_queue_room() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                queueRoomModelList.add(new QueueRoomModel(jsonObject.getInt("id"),
                                                                          jsonObject.getString("title"),
                                                                          jsonObject.getString("date_create")));
                                queueRoomAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){

            /** Passing some request headers* */
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + JWT_TOKEN);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }
}