package com.example.shop.Wish;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishFragment newInstance(String param1, String param2) {
        WishFragment fragment = new WishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView wishlistRecyclerView;
    private static FirebaseFirestore firebaseFirestore;
    public static List<String> wl = new ArrayList<>();
    public static List<WishlistModel> wlm = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();
    public static WishlistAdapter wishlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish, container, false);

        wishlistRecyclerView = view.findViewById(R.id.wishlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        wl.clear();
        wishlistModelList.clear();
        load();

        wishlistAdapter = new WishlistAdapter(wishlistModelList, true);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
        return view;
    }

//    TODO: Code review

    public static void load(){
        for(int i=0; i < wishlistModelList.size(); i++){
            wishlistModelList.remove(i);
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        String f = FirebaseAuth.getInstance().getUid();
        firebaseFirestore.collection("USERS").document(f).collection("USER_DATA").document("WISHLIST").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(int i=0; i < (long)task.getResult().get("size_list"); i++){
                                wl.add(task.getResult().get("product_id_"+i).toString());
                                firebaseFirestore.collection("PRODUCTS").document(task.getResult().get("product_id_"+i).toString()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    wishlistModelList.add(new WishlistModel(task.getResult().get("product_id").toString(),
                                                            task.getResult().get("product_image_1").toString(),
                                                            1,
                                                            Long.parseLong(task.getResult().get("total_rating").toString()),
                                                            task.getResult().get("product_title").toString(),
                                                            task.getResult().get("avg_rating").toString(),
                                                            task.getResult().get("product_price").toString(),
                                                            task.getResult().get("product_discount_price").toString()));
                                                    wishlistAdapter.notifyDataSetChanged();
                                                }

                                                else{
                                                    ;
                                                }
                                            }
                                        });
                            }
                        }
                        else{
                            ;
                        }
                    }
                });
    }

    public static void loadWishList(){
        Log.d("dbg", FirebaseAuth.getInstance().getUid());
        firebaseFirestore = FirebaseFirestore.getInstance();
        String f = FirebaseAuth.getInstance().getUid();
        Log.d("dbg", f);
        firebaseFirestore.collection("USERS").document(f).collection("USER_DATA").document("WISHLIST").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("dbg", task.getResult().toString());
                            for(int i=0; i < (long)task.getResult().get("size_list"); i++){
                                wl.add(task.getResult().get("product_id_"+i).toString());

                                firebaseFirestore.collection("PRODUCTS").document(task.getResult().get("product_id_"+i).toString()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Log.d("dbg", task.getResult().get("product_title").toString());

                                                    wishlistModelList.add(new WishlistModel(task.getResult().get("product_id").toString(),
                                                            task.getResult().get("product_image_1").toString(),
                                                            1,
                                                            Long.parseLong(task.getResult().get("total_rating").toString()),
                                                            task.getResult().get("product_title").toString(),
                                                            task.getResult().get("avg_rating").toString(),
                                                            task.getResult().get("product_price").toString(),
                                                            task.getResult().get("product_discount_price").toString()));
                                                }
                                                else{
                                                    ;
                                                }
                                            }
                                        });
                            }
                        }
                        else{
                            ;
                        }
                    }
                });
    }

    public static void removeWishList(int index){
        wl.remove(index);

        Map<String, Object> updateWishList = new HashMap<>();
        for (int i=0; i < wl.size(); i++){
            updateWishList.put("product_id_"+i, wl.get(i));
        }
        updateWishList.put("size_list", (long)wl.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("WISHLIST").set(updateWishList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(wishlistModelList.size() != 0){
                                wishlistModelList.remove(index);
                                WishFragment.wishlistAdapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            ;
                        }
                    }
                });
    }
}