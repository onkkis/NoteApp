package com.example.noteapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;


public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        progressBar = v.findViewById(R.id.notes_progressBar);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        //Get all notes from user's notes collection
        db.collection("users").document(user.getDisplayName())
                .collection("notes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Convert the whole snapshot to a POJO list
                        List<Note> notes = task.getResult().toObjects(Note.class);
                        //Set notes list for NoteInfoFragment use
                        noteViewModel.setNotes(notes);
                        //Add recyclerview to fragment
                        recyclerView = v.findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        //Create NoteAdapter and add it to recyclerview
                        noteAdapter = new NoteAdapter(getContext(),notes);
                        recyclerView.setAdapter(noteAdapter);
                        //Set visibility of progressbar to gone
                        progressBar.setVisibility(v.GONE);
                        //Add RecyclerTouchListener to recyclerview
                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                noteViewModel.select(position);
                                NavHostFragment.findNavController(NotesFragment.this)
                                        .navigate(R.id.action_NotesFragment_to_noteInfoFragment);

                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Delete note")
                                        .setMessage("Are you sure you want to delete this note?")
                                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                            // Continue with delete operation
                                            db.collection("users").document(user.getDisplayName())
                                                    .collection("notes")
                                                    .whereEqualTo("title", noteAdapter.getNoteAt(position).getTitle())
                                                    .get().addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task1.getResult()) {
                                                        document.getReference().delete();
                                                        //reset notes for layout
                                                        resetNotes(v);
                                                    }
                                                } else {
                                                    Log.d("TAG", "Error getting documents: ", task1.getException());
                                                }
                                            });
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.cancel, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }));
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });



        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if add_icon is pressed navigate to newNoteFragment
        view.findViewById(R.id.add_icon).setOnClickListener(v -> {
            NavHostFragment.findNavController(NotesFragment.this)
                    .navigate(R.id.action_NotesFragment_to_newNoteFragment);
        });
    }

    private void resetNotes(View v) {
        progressBar.setVisibility(v.VISIBLE);
        db.collection("users").document(user.getDisplayName())
                .collection("notes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Convert the whole snapshot to a POJO list
                        List<Note> notes = task.getResult().toObjects(Note.class);
                        //Set notes list for NoteInfoFragment use
                        noteViewModel.setNotes(notes);
                        //Set visibility of progressbar to gone
                        progressBar.setVisibility(v.GONE);
                        noteAdapter.resetValues(notes);
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }
}