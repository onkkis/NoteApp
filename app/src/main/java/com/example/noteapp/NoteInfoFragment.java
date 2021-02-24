package com.example.noteapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteInfoFragment extends Fragment {

    private NoteViewModel noteViewModel;
    private EditText note_text;

    private FirebaseFirestore db;
    private FirebaseUser user;

    boolean saved = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(!saved){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Note might be unsaved!")
                            .setMessage("Do you still wish to exit?")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                NavHostFragment.findNavController(NoteInfoFragment.this)
                                        .navigate(R.id.action_noteInfoFragment_to_NotesFragment);

                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.cancel, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else {
                    NavHostFragment.findNavController(NoteInfoFragment.this)
                            .navigate(R.id.action_noteInfoFragment_to_NotesFragment);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_note_info, container, false);
        note_text = v.findViewById(R.id.note_info_text);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        note_text.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                saved = false;
            }
        });

        //Load selected note to view from NoteViewModel
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteViewModel.getSelected().observe(getViewLifecycleOwner(), selected ->{
            noteViewModel.getNotes().observe(getViewLifecycleOwner(), notes1 ->{
                note_text.setText(notes1.get(selected).getText(), TextView.BufferType.EDITABLE);
            });
        });

        //On click listener for save button
        v.findViewById(R.id.btn_save).setOnClickListener(v1 -> {
            noteViewModel.getSelected().observe(getViewLifecycleOwner(), selected ->{
                noteViewModel.getNotes().observe(getViewLifecycleOwner(), notes1 ->{
                    String title = notes1.get(selected).getTitle();
                    String text = note_text.getText().toString();
                    //Make note POJO object and set title and text as constructor's parameters
                    Note newNote = new Note(title,text);
                    //Add new note inside user's notes collection
                    db.collection("users").document(user.getDisplayName())
                            .collection("notes").document(title).set(newNote);
                });
            });
        });

        return v;
    }
}