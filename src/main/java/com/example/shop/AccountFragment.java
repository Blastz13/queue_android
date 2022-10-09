package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.Auth.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class AccountFragment extends Fragment {

    private Button signOutButton;
    private static String URL_PROFILE = "http://10.0.2.2:8000/auth/profile/";

    private TextView name;
    SharedPreferences PreferenceStorage;
    String JWT_TOKEN;
    Integer currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        NavHostFragment.findNavController(this).navigate(R.id.orderFragment);
        PreferenceStorage = getActivity().getSharedPreferences("com.example.shop", Context.MODE_PRIVATE);
        JWT_TOKEN = PreferenceStorage.getString("JWT_TOKEN", "");

        signOutButton = view.findViewById(R.id.sign_out_btn);
        name = view.findViewById(R.id.name_account);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceStorage.edit().clear().commit();
                Intent registerIntent = new Intent(getContext(), RegistrationActivity.class);
                getContext().startActivity(registerIntent);
            }
        });
        request_get_current_user_id();

        return view;
    }

    private void request_get_current_user_id() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL_PROFILE, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences.Editor editor = PreferenceStorage.edit();
                            currentUserId = response.getInt("id");
                            name.setText(response.getString("username"));
                            editor.putString("USER_ID", String.valueOf(response.get("id")));
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = null;
//                String statusCode = String.valueOf(error.networkResponse.statusCode);
                if (error.networkResponse.data != null) {
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //                    Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            /** Passing some request headers* */
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + JWT_TOKEN);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }
}