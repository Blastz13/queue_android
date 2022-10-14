package com.example.shop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment {

    private EditText searchRoomInput;
    String URL = "http://10.0.2.2:8000/queue_room/entered";
    SharedPreferences PreferenceStorage;
    RecyclerView queueRoomRecycler;
    QueueRoomAdapter queueRoomAdapter;
    List<QueueRoomModel> queueRoomModelList = new ArrayList<>();
    private String JWT_TOKEN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);
        searchRoomInput = view.findViewById(R.id.searchRoomInput);
        queueRoomRecycler = view.findViewById(R.id.queueRoomRecycler);
        PreferenceStorage = getActivity().getSharedPreferences("com.example.shop", Context.MODE_PRIVATE);
        JWT_TOKEN = PreferenceStorage.getString("JWT_TOKEN", "");
        setQueueRoomRecycler();


        searchRoomInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                List<QueueRoomModel> queueRoomModelListSearch = new ArrayList<>();
                for (int i=0; i < queueRoomModelList.size(); i++){
                    if (queueRoomModelList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        queueRoomModelListSearch.add(queueRoomModelList.get(i));
                    }
                }
                queueRoomAdapter = new QueueRoomAdapter(getActivity(), queueRoomModelListSearch);
                queueRoomRecycler.setAdapter(queueRoomAdapter);
            }
        });

        return view;
    }
    public void setQueueRoomRecycler() {
        request_get_list_queue_room();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        queueRoomRecycler = view.findViewById(R.id.queueRoomRecycler);
        queueRoomRecycler.setLayoutManager(layoutManager);
        queueRoomAdapter = new QueueRoomAdapter(getActivity(), queueRoomModelList);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjReq);
    }

}