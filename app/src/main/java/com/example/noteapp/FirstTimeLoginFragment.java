package com.example.noteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirstTimeLoginFragment extends Fragment {

    private AppCompatButton continue_btn;
    private TextView username;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first_time_login, container, false);
        continue_btn = v.findViewById(R.id.firsttime_btn_continue);
        username = v.findViewById(R.id.firsttime_input_username);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        continue_btn.setOnClickListener(v -> {
            if(username.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter username!",
                        Toast.LENGTH_SHORT).show();
            }else {
                //Update user's display name with one given as username input
                //TO DO: Check if username exists
                //If it exists tell user to choose another username
                Log.d("User email: ", user.getEmail());
                Log.d("User new display name: ", username.getText().toString());
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username.getText().toString()).build();

                user.updateProfile(profileUpdates);

                Map<String, Object> usrupdate = new HashMap<>();
                usrupdate.put("email", user.getEmail());
                usrupdate.put("username", username.getText().toString());
                db.collection("users").document(username.getText().toString()).set(usrupdate);

                Toast.makeText(getActivity(), "User name added succesfully!",
                        Toast.LENGTH_LONG).show();

                NavHostFragment.findNavController(FirstTimeLoginFragment.this)
                        .navigate(R.id.action_FirstTimeLoginFragment_to_NotesFragment);

            }
        });
    }
}