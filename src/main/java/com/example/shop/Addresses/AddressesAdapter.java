package com.example.shop.Addresses;

import static com.example.shop.DeliveryActivity.SELECT_ADDRESS;
import static com.example.shop.AccountFragment.MANAGE_ADDRESS;
import static com.example.shop.Addresses.MyAddressesActivity.refreshItem;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean isRefreshItem = false;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int mode) {
        this.addressesModelList = addressesModelList;
        this.MODE = mode;
        preSelectedPosition = Address.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder holder, int position) {
        Boolean is_selected_address = addressesModelList.get(position).getIs_selected_address();
        String city = addressesModelList.get(position).getCity();
        String street = addressesModelList.get(position).getStreet();
        String house = addressesModelList.get(position).getHouse();
        String index = addressesModelList.get(position).getIndex();
        String flat = addressesModelList.get(position).getFlat();
        String note = addressesModelList.get(position).getNote();
        String name = addressesModelList.get(position).getName();
        String phone = addressesModelList.get(position).getPhone();
        String address = addressesModelList.get(position).getAddress();
//        Log.d("dbg", address.toString());
//
//        String name = addressesModelList.get(position).getName();
//        String address = addressesModelList.get(position).getAddress();
//        String index = addressesModelList.get(position).getIndex();
//        Boolean is_selected_address = addressesModelList.get(position).getIs_selected_address();
        holder.setData(name, address, index, is_selected_address, position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView address;
        private TextView index;
        private ImageView icon;
        private LinearLayout optionLinearLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_address_item);
            address = itemView.findViewById(R.id.address_item);
            index = itemView.findViewById(R.id.index_address_item);
            icon = itemView.findViewById(R.id.icon_check);
            optionLinearLayout = itemView.findViewById(R.id.option);
        }

        private void setData(String nameUser, String addressUser, String indexUser, Boolean selected, int position){
            name.setText(nameUser);
            address.setText(addressUser);
            index.setText(indexUser);
            if(MODE == SELECT_ADDRESS){
                icon.setImageResource(R.drawable.ic_check);
                if (selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                }
                else{
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setIs_selected_address(true);
                            addressesModelList.get(preSelectedPosition).setIs_selected_address(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            Address.selectedAddress = position;
                        }
                    }
                });

            }
            else if(MODE == MANAGE_ADDRESS){
                optionLinearLayout.setVisibility(View.GONE);
                optionLinearLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent addressIntent = new Intent(itemView.getContext(), AddressActivity.class);
                        addressIntent.putExtra("INTENT", "update");
                        addressIntent.putExtra("index", position);
                        itemView.getContext().startActivity(addressIntent);
                        isRefreshItem = false;
                    }
                });
                optionLinearLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> addresses = new HashMap<>();
                        int j=0;
                        int selected = -1;
                        for(int i=0; i < addressesModelList.size(); i++){
                            if(i!=position){
                                j++;
                                addresses.put("city_"+j, addressesModelList.get(i).getCity());
                                addresses.put("street_"+j, addressesModelList.get(i).getStreet());
                                addresses.put("house_"+j, addressesModelList.get(i).getHouse());
                                addresses.put("index_"+j, addressesModelList.get(i).getIndex());
                                addresses.put("flat_"+j, addressesModelList.get(i).getFlat());
                                addresses.put("note_"+j, addressesModelList.get(i).getNote());
                                addresses.put("name_"+j, addressesModelList.get(i).getName());
                                addresses.put("phone_"+j, addressesModelList.get(i).getPhone());

                                if(addressesModelList.get(position).getIs_selected_address()){
                                    if(position -1 >= 0){
                                        if(j==position-1){
                                            addresses.put("selected_"+j,true);
                                            selected = j;
                                        }
                                    }else{
                                        if(j==1){
                                            addresses.put("selected_"+j,true);
                                            selected = j;

                                        }
                                    }
                                }else {
                                    addresses.put("selected_"+j, addressesModelList.get(i).getIs_selected_address());
                                }

                            }
                        }
                        addresses.put("size_list", j);
                        int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                .document("ADDRESSES")
                                .update(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Address.addressesModelList.remove(position);
                                    if(finalSelected != -1) {
                                        Address.selectedAddress = finalSelected - 1;
                                        Address.addressesModelList.get(finalSelected - 1).setIs_selected_address(true);
                                    }
                                    notifyDataSetChanged();
                                }else {

                                }
                            }
                        });
                        isRefreshItem = false;
                    }
                });
                icon.setImageResource(R.drawable.ic_more);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionLinearLayout.setVisibility(View.VISIBLE);
                        if (isRefreshItem) {
                            refreshItem(preSelectedPosition, preSelectedPosition);
                        }else{
                            isRefreshItem = true;
                        }
                        preSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItem(preSelectedPosition, preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
