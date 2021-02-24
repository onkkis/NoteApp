package com.example.noteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteFragment extends Fragment {

    private EditText note_text,note_title;
    private AppCompatButton create_btn;

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
        View v = inflater.inflate(R.layout.fragment_new_note, container, false);
        note_title = v.findViewById(R.id.newnote_title);
        note_text = v.findViewById(R.id.newnote_text);
        create_btn = v.findViewById(R.id.newnote_btn_create);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        create_btn.setOnClickListener(v -> {
            if(note_text.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please write note text.",
                        Toast.LENGTH_LONG).show();
            }else if(note_title.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please enter a title.",
                        Toast.LENGTH_LONG).show();
            }else {
                String title = note_title.getText().toString();
                String text = note_text.getText().toString();

                //Make note POJO object and set title and text as constructor's parameters
                Note newNote = new Note(title,text);
                //Add new note inside user's notes collection
                db.collection("users").document(user.getDisplayName())
                        .collection("notes").document(title).set(newNote);
                //Tell user that new note was added
                Toast.makeText(getActivity(), "New note added!",
                        Toast.LENGTH_LONG).show();
                //Navigate back to NotesFragment
                NavHostFragment.findNavController(NewNoteFragment.this)
                        .navigate(R.id.action_newNoteFragment_to_NotesFragment);
            }
        });

    }
}