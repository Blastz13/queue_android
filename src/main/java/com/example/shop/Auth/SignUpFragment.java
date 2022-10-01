package com.example.shop.Auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.MainActivity;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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

    private TextView signIn;
    private FrameLayout frameLayout;

    private EditText email;
    private EditText name;
    private EditText password;
    private EditText confirmPassword;
    private ImageButton closeBtn;
    private Button signUpBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        signIn = view.findViewById(R.id.or_sign_in_btn);
        frameLayout = getActivity().findViewById(R.id.registration_framelayout);

        email = view.findViewById(R.id.sign_up_email);
        name = view.findViewById(R.id.sign_up_name);
        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.sign_up_confirm_password);
        closeBtn = view.findViewById(R.id.sign_up_close);
        signUpBtn = view.findViewById(R.id.sign_up_btn);
        progressBar = view.findViewById(R.id.sign_up_progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignUpFragment", "Unregistered user");
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmailAndPassword();
            }
        });
    }

    private void validateInputs() {
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(name.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8){
                    if (!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(Color.rgb(255,255,255));
                    }
                    else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.rgb(55,255,255));
                    }
                }
                else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.rgb(55,255,255));
                }
            }
            else {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.rgb(55,255,255));
            }
        }
        else {
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.rgb(55,255,255));
        }
    }

    private void validateEmailAndPassword(){
        if (email.getText().toString().matches(emailRegex)){
            if (password.getText().toString().equals(confirmPassword.getText().toString())){
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                        password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Map<String, Object> userdata = new HashMap<>();
                            userdata.put("name", name.getText().toString());
                            userdata.put("email", email.getText().toString());
                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                .set(userdata)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA");

                                        Map<String, Object> wishListMap = new HashMap<>();
                                        wishListMap.put("size_list", 0);

                                        Map<String, Object> ratingMap = new HashMap<>();
                                        ratingMap.put("size_list", 0);

                                        Map<String, Object> cartMap = new HashMap<>();
                                        cartMap.put("size_list", 0);

                                        Map<String, Object> addressMap = new HashMap<>();
                                        addressMap.put("size_list", 0);

                                        List<String> documentNames = new ArrayList<>();
                                        documentNames.add("WISHLIST");
                                        documentNames.add("RATINGS");
                                        documentNames.add("CART");
                                        documentNames.add("ADDRESSES");

                                        List<Map<String, Object>> documentFields = new ArrayList<>();
                                        documentFields.add(wishListMap);
                                        documentFields.add(ratingMap);
                                        documentFields.add(cartMap);
                                        documentFields.add(addressMap);

                                        for(int i=0; i < documentNames.size(); i++){
                                            int finalI = i;
                                            userDataReference.document(documentNames.get(i)).set(documentFields.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        if(finalI == documentNames.size() - 1) {
                                                            Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                                            startActivity(mainIntent);
                                                            getActivity().finish();
                                                        }
                                                    }
                                                    else{
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                        signUpBtn.setEnabled(true);
                                                        signUpBtn.setTextColor(Color.rgb(255,255,255));

                                                        Toast.makeText(getActivity(), task.getException().getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        signUpBtn.setEnabled(true);
                                        signUpBtn.setTextColor(Color.rgb(255,255,255));

                                        Toast.makeText(getActivity(), task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);

                            signUpBtn.setEnabled(true);
                            signUpBtn.setTextColor(Color.rgb(255,255,255));

                            Toast.makeText(getActivity(), task.getException().getMessage(),
                                           Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                confirmPassword.setError("Password doesn't matched");
            }
        }
        else {
            email.setError("Invalid email");
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}