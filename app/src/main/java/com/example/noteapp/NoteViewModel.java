package com.example.noteapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private MutableLiveData<List<Note>> notes = new MutableLiveData<>();
    private MutableLiveData<Integer> selected = new MutableLiveData<>();

    //Getter and setter for note selection.
    public void select(int position) {
        selected.setValue(position);
    }
    public LiveData<Integer> getSelected(){
        return selected;
    }

    //Getter and setter for notes list
    public void setNotes(List<Note> notes) { this.notes.setValue(notes); }
    LiveData<List<Note>> getNotes() {return notes;}

    public NoteViewModel(@NonNull Application application) { super(application); }
}
