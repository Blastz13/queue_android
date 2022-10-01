package com.example.shop.Addresses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shop.Cart.Cart;
import com.example.shop.DeliveryActivity;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private Button saveAddressButton;
    private EditText city;
    private EditText street;
    private EditText house;
    private EditText index;
    private EditText flat;
    private EditText note;
    private EditText name;
    private EditText phone;
    private boolean updateAddress = false;
    private AddressesModel addressesModel;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        saveAddressButton = findViewById(R.id.save_address_button);
        city = findViewById(R.id.city_address);
        street = findViewById(R.id.street_address);
        house = findViewById(R.id.house_address);
        index = findViewById(R.id.index_address);
        flat = findViewById(R.id.flat_address);
        note = findViewById(R.id.note_address);
        name = findViewById(R.id.name_address);
        phone = findViewById(R.id.phone_address);

        String type = getIntent().getStringExtra("INTENT");
        if(type != null && type.equals("update")){
            updateAddress = true;
            position = getIntent().getIntExtra("index", -1);
            addressesModel = Address.addressesModelList.get(position);

            city.setText(addressesModel.getCity());
            street.setText(addressesModel.getStreet());
            house.setText(addressesModel.getHouse());
            flat.setText(addressesModel.getFlat());
            index.setText(addressesModel.getIndex());
            note.setText(addressesModel.getNote());
            name.setText(addressesModel.getName());
            phone.setText(addressesModel.getPhone());
            saveAddressButton.setText("Обновить адрес");
        }else {
            position = (Address.addressesModelList.size()+1);
        }

        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dbg", "Click");
                if(!TextUtils.isEmpty(city.getText())){
                    if(!TextUtils.isEmpty(street.getText())){
                        if(!TextUtils.isEmpty(house.getText())){
                            if(!TextUtils.isEmpty(index.getText())){
                                if(!TextUtils.isEmpty(name.getText())){
                                    if(!TextUtils.isEmpty(phone.getText()) && phone.getText().length() == 12){
                                        String address = city.getText().toString() + " " + street.getText().toString()+ " " + house.getText().toString()+ " " + flat.getText().toString();
                                        Map<String, Object> addAddress = new HashMap<>();

                                        addAddress.put("name_" + (long)(Address.addressesModelList.size()+1), name.getText().toString());
                                        addAddress.put("address_" + (long)(Address.addressesModelList.size()+1), address);
                                        addAddress.put("index_" + (long)(Address.addressesModelList.size()+1), index.getText().toString());
                                        addAddress.put("city_" + (long)(Address.addressesModelList.size()+1), city.getText().toString());
                                        addAddress.put("street_" + (long)(Address.addressesModelList.size()+1), street.getText().toString());
                                        addAddress.put("house_" + (long)(Address.addressesModelList.size()+1), house.getText().toString());
                                        addAddress.put("index_" + (long)(Address.addressesModelList.size()+1), index.getText().toString());
                                        addAddress.put("flat_" + (long)(Address.addressesModelList.size()+1), flat.getText().toString());
                                        addAddress.put("note_" + (long)(Address.addressesModelList.size()+1), note.getText().toString());
                                        addAddress.put("phone_" + (long)(Address.addressesModelList.size()+1), phone.getText().toString());

                                        if(!updateAddress) {
                                            addAddress.put("size_list", (long) (Address.addressesModelList.size() + 1));
                                            addAddress.put("selected_" + (long)(Address.addressesModelList.size()+1), true);
                                            if(Address.addressesModelList.size() > 0){
                                                addAddress.put("selected_" + (long)(Address.selectedAddress+1), false);
                                            }
                                        }

                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA")
                                                .document("ADDRESSES").update(addAddress)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    if(!updateAddress) {
                                                        if (Address.addressesModelList.size() > 0) {
                                                            Address.addressesModelList.get(Address.selectedAddress).setIs_selected_address(false);
                                                        }
                                                        Address.addressesModelList.add(new AddressesModel(true, city.getText().toString(), street.getText().toString(), house.getText().toString(),
                                                                index.getText().toString(), flat.getText().toString(), note.getText().toString(), name.getText().toString(), phone.getText().toString()));
                                                        Address.selectedAddress = Cart.cartItemModelList.size() - 1;

                                                    }else{
                                                        Address.addressesModelList.set(position, new AddressesModel(true, city.getText().toString(), street.getText().toString(), house.getText().toString(),
                                                                index.getText().toString(), flat.getText().toString(), note.getText().toString(), name.getText().toString(), phone.getText().toString()));
                                                    }

                                                    if(getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    }
                                                    else {
                                                        MyAddressesActivity.refreshItem(Address.selectedAddress, Address.addressesModelList.size() - 1);
                                                    }
                                                    finish();
                                                }
                                                else{
                                                    ;
                                                }
                                            }
                                        });

                                        if(getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                            Intent deliveryIntent = new Intent(AddressActivity.this, DeliveryActivity.class);
                                            startActivity(deliveryIntent);
                                        }
                                        else {
                                            Intent deliveryIntent = new Intent(AddressActivity.this, MyAddressesActivity.class);
                                            deliveryIntent.putExtra("MODE", 1);
                                            AddressActivity.this.startActivity(deliveryIntent);
                                        }
                                    }else{
                                        phone.setError("Required");
                                    }
                                }else {
                                    name.setError("Required");
                                }
                            }else {
                                index.setError("Required");
                            }
                        }else {
                            house.setError("Required");
                        }
                    }else{
                        street.setError("Required");
                    }
                } else{
                    city.setError("Required");
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Новый адрес");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}