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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText email,passwd;
    private AppCompatButton login_btn;
    private TextView link_signup,forgot_passwd;


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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        email = v.findViewById(R.id.login_input_email);
        passwd = v.findViewById(R.id.login_input_password);
        login_btn = v.findViewById(R.id.btn_login);
        link_signup = v.findViewById(R.id.link_signup);
        forgot_passwd = v.findViewById(R.id.forgot_passwd);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_btn.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter email!",
                        Toast.LENGTH_SHORT).show();
            }else if(passwd.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please enter password!",
                        Toast.LENGTH_SHORT).show();
            }else{
                signIn(email.getText().toString(),passwd.getText().toString());
            }
        });

        link_signup.setOnClickListener(v -> {
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_LoginFragment_to_SignupFragment);
        });

    }

    //Sign in with email and password if successful navigate to NotesFragment
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success navigate to NotesFragment
                            Log.d("TAG:", "signInWithEmail:success");
                            Toast.makeText(getActivity(), "Authentication Success!.",
                                    Toast.LENGTH_SHORT).show();

                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_LoginFragment_to_NotesFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG: ", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}