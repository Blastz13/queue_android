package com.example.shop.Auth;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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

    private EditText emailForgotPassword;
    private Button resetPasswordBtn;
    private TextView signInForgotPassword;
    private ViewGroup sendMessageContainer;
    private ImageView sendMessageIco;
    private TextView sendMessageText;
    private ProgressBar sendMessageProgressBar;

    private FrameLayout frameLayout;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        emailForgotPassword = view.findViewById(R.id.email_forgot_password);
        resetPasswordBtn = view.findViewById(R.id.reset_password_btn);
        signInForgotPassword = view.findViewById(R.id.sign_in_forgot_password);

        sendMessageContainer = view.findViewById(R.id.send_message_container);
        sendMessageIco = view.findViewById(R.id.send_message_ico);
        sendMessageText = view.findViewById(R.id.send_message_text);
        sendMessageProgressBar = view.findViewById(R.id.send_message_progressBar);

        frameLayout = getActivity().findViewById(R.id.registration_framelayout);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailForgotPassword.addTextChangedListener(new TextWatcher() {
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
        signInForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(sendMessageContainer);
                sendMessageIco.setVisibility(View.VISIBLE);
                sendMessageProgressBar.setVisibility(View.VISIBLE);

                resetPasswordBtn.setEnabled(true);
                resetPasswordBtn.setTextColor(Color.rgb(55,255,255));

                firebaseAuth.sendPasswordResetEmail(emailForgotPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    sendMessageText.setVisibility(View.VISIBLE);
                                }
                                else {

                                    sendMessageProgressBar.setVisibility(View.GONE);

                                    sendMessageText.setText(task.getException().getMessage());
                                    sendMessageText.setTextColor(getResources().getColor(R.color.white_red));
                                    TransitionManager.beginDelayedTransition(sendMessageContainer);
                                    sendMessageText.setVisibility(View.VISIBLE);

                                }
                                resetPasswordBtn.setEnabled(true);
                                resetPasswordBtn.setTextColor(Color.rgb(255,255,255));
                            }
                        });
            }
        });
    }

    public void validateInputs(){
        if (!TextUtils.isEmpty(emailForgotPassword.getText())){
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setTextColor(Color.rgb(55,255,255));
        }
        else {
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setTextColor(Color.rgb(255,255,255));
        }
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}