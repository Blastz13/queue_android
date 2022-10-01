package com.example.shop.Addresses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.shop.DeliveryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Address {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static boolean isAddressSelected = false;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();
    public static int selectedAddress = -1;

    public static void loadAddress(final Context context){
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Intent deliveryIntent;
                    if((long)task.getResult().get("size_list") == 0){
                        deliveryIntent = new Intent(context, AddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    }
                    else {
                        for(long i=1; i < (long) task.getResult().get("size_list") + 1; i++){
                            addressesModelList.add(new AddressesModel(task.getResult().getBoolean("selected_"+i),
                                    task.getResult().getString("city_"+i),
                                    task.getResult().getString("street_"+i),
                                    task.getResult().getString("house_"+i),
                                    task.getResult().getString("index_"+i),
                                    task.getResult().getString("flat_"+i),
                                    task.getResult().getString("note_"+i),
                                    task.getResult().getString("name_"+i),
                                    task.getResult().getString("phone_"+i)));
                            if((boolean)task.getResult().get("selected_"+i)){
                                selectedAddress = (int) (i - 1);
                                Log.d("dbg", addressesModelList.toString());
                                Log.d("dbg", String.valueOf(selectedAddress));
                            }
                        }

                        deliveryIntent = new Intent(context, DeliveryActivity.class);


                    }
                    context.startActivity(deliveryIntent);
                }
                else {
                    ;
                }
            }
        });
    }

    public static void loadAddress(final Context context, AddressesAdapter addressesAdapter){
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Intent deliveryIntent;
                    if((long)task.getResult().get("size_list") == 0){
                        deliveryIntent = new Intent(context, AddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    }
                    else {
                        for(long i=1; i < (long) task.getResult().get("size_list") + 1; i++){
                            addressesModelList.add(new AddressesModel(task.getResult().getBoolean("selected_"+i),
                                    task.getResult().getString("city_"+i),
                                    task.getResult().getString("street_"+i),
                                    task.getResult().getString("house_"+i),
                                    task.getResult().getString("index_"+i),
                                    task.getResult().getString("flat_"+i),
                                    task.getResult().getString("note_"+i),
                                    task.getResult().getString("name_"+i),
                                    task.getResult().getString("phone_"+i)));
                            if((boolean)task.getResult().get("selected_"+i)){
                                selectedAddress = (int) (i - 1);
                                Log.d("dbg", addressesModelList.toString());
                                Log.d("dbg", String.valueOf(selectedAddress));
                            }
                            addressesAdapter.notifyDataSetChanged();
                        }

//                        deliveryIntent = new Intent(context, DeliveryActivity.class);


                    }
//                    context.startActivity(deliveryIntent);
                }
                else {
                    ;
                }
            }
        });
    }
}
