package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private static String URL_WS = "ws://10.0.2.2:8000/chat/ws/";
    private static String URL_ROOM_WS = "ws://10.0.2.2:8000/chat/ws/";
    private static String URL_LOAD_MESSAGES = "http://10.0.2.2:8000/chat/";
    private static String URL_PROFILE = "http://10.0.2.2:8000/auth/profile/";
    private static String URL_LOAD_NEXT_QUEUE_TICKET = "http://10.0.2.2:8000/queue/";
    private static String URL = "http://10.0.2.2:8000/";
    private static String URL_ADD_TO_QUEUE_TICKET;
    private static String URL_LOAD_NEXT_QUEUE_TICKET_ROOM;
    private static String URL_CURRENT_TICKET;
    private int IMAGE_REQUEST_ID = 1;

    Integer idQueueRoom;
    Integer currentUserId;
    private String JWT_TOKEN;
    private WebSocket webSocket;
    private EditText messageEdit;
    private View sendBtn;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    SharedPreferences PreferenceStorage;
    private TextView currentTicketEmail;
    private TextView nextTicketEmail;
    private TextView moreQueueTicketBtn;
    private TextView hiddenQueueTicketBtn;
    private ConstraintLayout moreQueueTicket;
    Button addToQueueBtn;
    Button updateToQueueBtn;
    Button removeToQueueBtn;
    JSONObject currentQueueTicket;
    RecyclerView queueTicketRoomRecycler;
    QueueTicketRoomAdapter queueTicketRoomAdapter;
    List<QueueTicketRoomModel> queueTicketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        queueTicketRoomRecycler = findViewById(R.id.queueTicketRoomRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        queueTicketRoomRecycler.setLayoutManager(layoutManager);
        queueTicketList = new ArrayList<>();
//        queueTicketList.add(new QueueTicketRoomModel(1, "wait", "testttt"));
//        queueTicketList.add(new QueueTicketRoomModel(1, "wait", "test2"));
        queueTicketRoomAdapter = new QueueTicketRoomAdapter(this, queueTicketList);
        queueTicketRoomRecycler.setAdapter(queueTicketRoomAdapter);

        idQueueRoom = getIntent().getIntExtra("idQueueRoom", 0);
        PreferenceStorage = getSharedPreferences("com.example.shop", Context.MODE_PRIVATE);
        JWT_TOKEN = PreferenceStorage.getString("JWT_TOKEN", "");
        currentUserId = Integer.valueOf(PreferenceStorage.getString("USER_ID", String.valueOf(0)));
        URL_ROOM_WS = URL_WS + idQueueRoom + "/";
        URL_CURRENT_TICKET = "http://10.0.2.2:8000/queue/" + idQueueRoom + "/current";
        URL_ADD_TO_QUEUE_TICKET = "http://10.0.2.2:8000/" + idQueueRoom + "/";
        URL_LOAD_NEXT_QUEUE_TICKET_ROOM = URL_LOAD_NEXT_QUEUE_TICKET + idQueueRoom;
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);

        recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentTicketEmail = findViewById(R.id.currentTicketEmail);
        nextTicketEmail = findViewById(R.id.nextTicketEmail);
        moreQueueTicketBtn = findViewById(R.id.moreQueueTicketBtn);
        moreQueueTicket = findViewById(R.id.moreQueueTicket);
        hiddenQueueTicketBtn = findViewById(R.id.hiddenQueueTicketBtn);
        addToQueueBtn = findViewById(R.id.AddToQueueBtn);
        updateToQueueBtn = findViewById(R.id.updateToQueueBtn);
        removeToQueueBtn = findViewById(R.id.removeToQueueBtn);

        addToQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_add_to_queue();
//                try {
//                    refresh_queue_control();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        updateToQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_update_to_queue();
                initiateSocketConnection();

//                Log.d("ws", "REGRSHHH");
//                try {
//                    refresh_queue_control();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        removeToQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_remove_to_queue();
                initiateSocketConnection();

//                try {
//                    refresh_queue_control();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
        moreQueueTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreQueueTicket.setVisibility(View.VISIBLE);
                moreQueueTicketBtn.setVisibility(View.GONE);
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });

        hiddenQueueTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreQueueTicket.setVisibility(View.GONE);
                moreQueueTicketBtn.setVisibility(View.VISIBLE);
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

            }
        });

        messageEdit.addTextChangedListener(this);
        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", "client_android");
                jsonObject.put("message", messageEdit.getText().toString());

                webSocket.send(messageEdit.getText().toString());

                jsonObject.put("is_own_message", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        request_get_current_ticket();
        request_get_current_user_id();
        request_get_next_queue_ticket();
        initiateSocketConnection();
        request_get_list_messages_room();
//        try {
//            refresh_queue_control();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL_ROOM_WS + JWT_TOKEN).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {
            sendBtn.setVisibility(View.VISIBLE);
//            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {
        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d("ws", "OPEN");

            runOnUiThread(() -> {
//                Toast.makeText(ChatActivity.this,
//                        "Socket Connection Successful!",
//                        Toast.LENGTH_SHORT).show();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getString("action").equals("updateCurrentQueue")) {
                        if (!jsonObject.isNull("data")) {
                            currentQueueTicket = jsonObject.getJSONObject("data").getJSONObject("user");
                            currentTicketEmail.setText(jsonObject.getJSONObject("data").getJSONObject("user").getString("username"));
                        } else {
                            currentQueueTicket = null;
                            currentTicketEmail.setText("Is empty");
                        }
                    } else if (jsonObject.getString("action").equals("updateListQueue")) {
                        if (jsonObject.getJSONArray("data").length() != 0) {
                            nextTicketEmail.setText(jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("user").getString("username"));
                        } else {
                            nextTicketEmail.setText("Is empty");
                        }

                        JSONArray data = jsonObject.getJSONArray("data");
                        queueTicketList = new ArrayList<>();

                        for (int i = 0; i < data.length(); i++) {
                            queueTicketList.add(new QueueTicketRoomModel(data.getJSONObject(i).getInt("id"),
                                    data.getJSONObject(i).getJSONObject("user").getInt("id"),
                                    data.getJSONObject(i).getString("status"),
                                    data.getJSONObject(i).getJSONObject("user").getString("username")));
                            Log.d("ws", "onMessage: " + queueTicketList.toString());
                        }
                        queueTicketRoomAdapter = new QueueTicketRoomAdapter(ChatActivity.this, queueTicketList);
                        queueTicketRoomRecycler.setAdapter(queueTicketRoomAdapter);
                        queueTicketRoomAdapter.notifyDataSetChanged();
                    } else {
                        jsonObject.put("is_own_message", false);

                        if (jsonObject.getInt("user_id") != currentUserId) {
                            messageAdapter.addItem(jsonObject);
                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.d("ws", "onMessage: REFRESH WS CONTROLS");
                    Log.d("ws", "onMessage: REFRESH WS CONTROLS size " + queueTicketList.size());
                    refresh_queue_control();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d("ws", "onClosing: ");
            initiateSocketConnection();
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            initiateSocketConnection();
            Log.d("ws", "onClosed: ");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", JWT_TOKEN);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("is_own_message", true);

            messageAdapter.addItem(jsonObject);
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void refresh_queue_control() throws JSONException {
//            if (currentQueueTicket != null){
        Log.d("ws", "refresh_queue_control: NOT NULL");
        addToQueueBtn.setVisibility(View.VISIBLE);
        updateToQueueBtn.setVisibility(View.GONE);
        removeToQueueBtn.setVisibility(View.GONE);

        Log.d("ws", "refresh_queue_control: size" + queueTicketList.size());
        for (int i = 0; i < queueTicketList.size(); i++) {
            if (currentUserId == queueTicketList.get(i).getUserId()) {
                Log.d("ws", "onResponse: REMOVE");
                addToQueueBtn.setVisibility(View.GONE);
                updateToQueueBtn.setVisibility(View.GONE);
                removeToQueueBtn.setVisibility(View.VISIBLE);
                break;
            }
        }

        if (currentQueueTicket != null && currentUserId == currentQueueTicket.getInt("id")) {
            Log.d("ws", "onResponse: UPDATE");

            addToQueueBtn.setVisibility(View.GONE);
            updateToQueueBtn.setVisibility(View.VISIBLE);
            removeToQueueBtn.setVisibility(View.GONE);
        }
//                }
//            }else {
//                Log.d("ws", "refresh_queue_control: NULL");
//                addToQueueBtn.setVisibility(View.VISIBLE);
//                updateToQueueBtn.setVisibility(View.GONE);
//                removeToQueueBtn.setVisibility(View.GONE);
//
//                RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//                JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL_CURRENT_TICKET, null,
//                        new com.android.volley.Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    Log.d("ws", "onResponse: REQUEST");
//
//                                    currentTicketEmail.setText(response.getJSONObject("user").getString("username"));
//                                    currentQueueTicket = response.getJSONObject("user");
//                                    Log.d("ws", "onResponse: ADD");
//
//                                    addToQueueBtn.setVisibility(View.VISIBLE);
//                                    updateToQueueBtn.setVisibility(View.GONE);
//                                    removeToQueueBtn.setVisibility(View.GONE);
//                                    Log.d("ws", "onResponse:QUEUE TICKET LIST" + queueTicketList.size());
//
//                                    for (int i=0; i < queueTicketList.size(); i++){
//                                        if (currentUserId == queueTicketList.get(i).getUserId()) {
//                                            Log.d("ws", "onResponse: REMOVE");
//                                            addToQueueBtn.setVisibility(View.GONE);
//                                            updateToQueueBtn.setVisibility(View.GONE);
//                                            removeToQueueBtn.setVisibility(View.VISIBLE);
//                                            break;
//                                        }
//                                    }
//
//                                    if(currentUserId == currentQueueTicket.getInt("id")) {
//                                        Log.d("ws", "onResponse: UPDATE");
//
//                                        addToQueueBtn.setVisibility(View.GONE);
//                                        updateToQueueBtn.setVisibility(View.VISIBLE);
//                                        removeToQueueBtn.setVisibility(View.GONE);
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    addToQueueBtn.setVisibility(View.VISIBLE);
//                                    updateToQueueBtn.setVisibility(View.GONE);
//                                    removeToQueueBtn.setVisibility(View.GONE);
//                                }
//                            }
//                        }, new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        try {
//                            String body = null;
//                            String statusCode = String.valueOf(error.networkResponse.statusCode);
//                            if (error.networkResponse.data != null) {
//                                try {
//                                    body = new String(error.networkResponse.data, "UTF-8");
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            try {
//                                Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } catch (Exception e) {
//                            currentTicketEmail.setText("Is empty");
//                        }
//                    }
//                }) {
//
//                    /** Passing some request headers* */
//                    @Override
//                    public Map getHeaders() {
//                        HashMap headers = new HashMap();
//                        headers.put("Authorization", "Bearer " + JWT_TOKEN);
//                        return headers;
//                    }
//                };
//                requestQueue.add(stringRequest);
//            }
    }

    private void request_get_current_user_id() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL_PROFILE, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences.Editor editor = PreferenceStorage.edit();
                            currentUserId = response.getInt("id");
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
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                if (error.networkResponse.data != null) {
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void request_add_to_queue() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL_ADD_TO_QUEUE_TICKET, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ;
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = null;
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                if (error.networkResponse.data != null) {
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void request_remove_to_queue() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.DELETE, "http://10.0.2.2:8000/queue/10/", null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ;
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = null;
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                if (error.networkResponse.data != null) {
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    private void request_update_to_queue() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.PUT, URL_ADD_TO_QUEUE_TICKET, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ;
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ;
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

    public void request_get_list_messages_room() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(com.android.volley.Request.Method.GET,
                URL_LOAD_MESSAGES + idQueueRoom, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = new JSONObject();
                        for (int i = response.length() - 1; i >= 0; i--) {
                            try {
                                jsonObject = new JSONObject();
                                jsonObject.put("username", response.getJSONObject(i).getString("username"));
                                jsonObject.put("message", response.getJSONObject(i).getString("message"));
                                if (response.getJSONObject(i).getInt("user_id") == currentUserId) {
                                    jsonObject.put("is_own_message", true);
                                } else {
                                    jsonObject.put("is_own_message", false);

                                }
                                if (jsonObject != null) {
                                    messageAdapter.addItem(jsonObject);
                                }
                                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(ChatActivity.this, new JSONObject(error.toString()).get("detail").toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void request_get_current_ticket() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest stringRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL_CURRENT_TICKET, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentTicketEmail.setText(response.getJSONObject("user").getString("username"));
                            currentQueueTicket = response.getJSONObject("user");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String body = null;
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    currentTicketEmail.setText("Is empty");
                }
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


    private void request_get_next_queue_ticket() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, URL_LOAD_NEXT_QUEUE_TICKET_ROOM, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            nextTicketEmail.setText(response.getJSONObject(0).getJSONObject("user").getString("username"));
                        } catch (Exception e) {
                            nextTicketEmail.setText("Is empty");
                        }

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                queueTicketList.add(new QueueTicketRoomModel(obj.getInt("id"),
                                        obj.getJSONObject("user").getInt("id"),
                                        obj.getString("status"),
                                        obj.getJSONObject("user").getString("username")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        queueTicketRoomAdapter.notifyDataSetChanged();
                        try {
                            Log.d("ws", "next load: refresh");
                            Log.d("ws", "next load: dix" + queueTicketList.size());
                            refresh_queue_control();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = null;
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                if (error.networkResponse.data != null) {
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Toast.makeText(ChatActivity.this, new JSONObject(body).get("detail").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void request_get_list_tickets_room() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(com.android.volley.Request.Method.GET,
                URL_LOAD_MESSAGES + idQueueRoom, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ws", "messages" + response.toString());
                        JSONObject jsonObject = new JSONObject();
                        for (int i = response.length() - 1; i >= 0; i--) {
                            try {
                                jsonObject = new JSONObject();
                                jsonObject.put("username", response.getJSONObject(i).getString("username"));
                                jsonObject.put("message", response.getJSONObject(i).getString("message"));
                                if (response.getJSONObject(i).getInt("user_id") == currentUserId) {
                                    jsonObject.put("is_own_message", true);
                                } else {
                                    jsonObject.put("is_own_message", false);

                                }
                                Log.d("ws", "ADDDDD" + jsonObject.toString());
                                if (jsonObject != null) {
                                    messageAdapter.addItem(jsonObject);
                                }
                                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(ChatActivity.this, new JSONObject(error.toString()).get("detail").toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

}
