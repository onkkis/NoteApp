package com.example.noteapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText email,passwd;
    private AppCompatButton signup_btn;
    private TextView link_login;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        email = v.findViewById(R.id.signup_input_email);
        passwd = v.findViewById(R.id.signup_input_password);
        signup_btn = v.findViewById(R.id.btn_signup);
        link_login = v.findViewById(R.id.link_login);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup_btn.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter email!",
                        Toast.LENGTH_SHORT).show();
            }else if(passwd.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please enter password!",
                        Toast.LENGTH_SHORT).show();
            }else{
                signUp(email.getText().toString(),passwd.getText().toString());
            }
        });

        link_login.setOnClickListener(v -> {
            NavHostFragment.findNavController(SignupFragment.this)
                    .navigate(R.id.action_SignupFragment_to_LoginFragment);
        });
    }

    //Create user with email and password and navigate to FirstTimeLoginFragment
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SIGNUP,", "createUserWithEmail:success");

                        Toast.makeText(getActivity(), "Registration successful!.",
                                Toast.LENGTH_SHORT).show();

                        NavHostFragment.findNavController(SignupFragment.this)
                                .navigate(R.id.action_SignupFragment_to_FirstTimeLoginFragment);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SIGNUP", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Registration failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }
}