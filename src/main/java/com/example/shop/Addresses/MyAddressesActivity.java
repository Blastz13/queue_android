package com.example.shop.Addresses;

import static com.example.shop.DeliveryActivity.SELECT_ADDRESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView addressesRecyclerView;
    private Button deliveryButton;
    private static AddressesAdapter addressesAdapter;
    private LinearLayout addAddressButton;
    private int previousAddress;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Адреса");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        previousAddress = Address.selectedAddress;
        addressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        deliveryButton = findViewById(R.id.dev_btn);
        addAddressButton = findViewById(R.id.add_new_address_layout);

        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this, AddressActivity.class);
                addAddressIntent.putExtra("INTENT", "none");
                startActivity(addAddressIntent);
            }
        });

        deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Address.selectedAddress != previousAddress){
                    final int index = previousAddress;
                    Map<String, Object> updateSelect = new HashMap<>();
                    updateSelect.put("selected_"+ (previousAddress+1), false);
                    updateSelect.put("selected_"+ (Address.selectedAddress+1), true);
                    previousAddress = Address.selectedAddress;
                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getUid())
                            .collection("USER_DATA").document("ADDRESSES")
                            .update(updateSelect).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                            }
                            else{
                                previousAddress = index;
                            }
                        }
                    });
                }else {
                    finish();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressesRecyclerView.setLayoutManager(layoutManager);

        mode = getIntent().getIntExtra("MODE", -1);
        if(mode == SELECT_ADDRESS){
            deliveryButton.setVisibility(View.VISIBLE);
        }
        else{
            deliveryButton.setVisibility(View.GONE);
        }

        addressesAdapter = new AddressesAdapter(Address.addressesModelList, getIntent().getIntExtra("MODE", -1));
        Address.loadAddress(this, addressesAdapter);
        addressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)addressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
    }

    public static void refreshItem(int preSelect, int select){
        addressesAdapter.notifyItemChanged(preSelect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(mode == SELECT_ADDRESS) {
                if (Address.selectedAddress != previousAddress) {
                    Address.addressesModelList.get(Address.selectedAddress).setIs_selected_address(false);
                    Address.addressesModelList.get(previousAddress).setIs_selected_address(true);
                    Address.selectedAddress = previousAddress;
                }
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if(mode == SELECT_ADDRESS) {
            if (Address.selectedAddress != previousAddress) {
                Address.addressesModelList.get(Address.selectedAddress).setIs_selected_address(false);
                Address.addressesModelList.get(previousAddress).setIs_selected_address(true);
                Address.selectedAddress = previousAddress;
            }
        }
        super.onBackPressed();
    }
}