package com.example.shop.Cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shop.Addresses.Address;
import com.example.shop.DeliveryActivity;
import com.example.shop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

    private RecyclerView cartItemsRecyclerView;
    private Button continueButton;
    private TextView totalAmount;
    private LinearLayout removeButton;
    public static CartAdapter cartAdapter;
    private LinearLayout continueCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        Map<String, Object> addAddress = new HashMap<>();

        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        continueButton = view.findViewById(R.id.cart_continue_btn);
        totalAmount = view.findViewById(R.id.total_cart_amount);
        continueCart = view.findViewById(R.id.end_cart);
        removeButton = view.findViewById(R.id.remove_item_cart_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        if(Cart.cartItemModelList.size() == 0){
            Cart.cartList.clear();
            Cart.loadCart(true);
        }else {
            if(Cart.cartItemModelList.get(Cart.cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
        }

//        List<CartItemModel> cartItemModelList = new ArrayList<>();
//        cartItemModelList.add(new CartItemModel(1, "Price", "10000$", "Free", "10000$", "You saved 10$"));

        cartAdapter = new CartAdapter(Cart.cartItemModelList, totalAmount, true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cart.cartItemModelList.size() > 0) {
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    for (int i = 0; i < Cart.cartItemModelList.size(); i++) {
                        CartItemModel cartItemModel = Cart.cartItemModelList.get(i);
                        if (cartItemModel.isInStock()) {
                            DeliveryActivity.cartItemModelList.add(cartItemModel);
                        }
                    }
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                    if (Address.addressesModelList.size() == 0) {
                        Address.loadAddress(getContext());
                    } else {
                        Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });
        return view;
    }
}