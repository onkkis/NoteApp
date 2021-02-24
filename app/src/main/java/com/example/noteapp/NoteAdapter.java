package com.example.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private List<Note> notes;
    private Context context;

    public NoteAdapter(Context context,List<Note> notes){
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        if(notes != null){
            Note current = notes.get(position);
            holder.text.setText(current.getTitle());
        }else {
            holder.text.setText("No notes available.");
        }
    }

    @Override
    public int getItemCount() {
        if (notes != null)
            return notes.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.recycler_textview);
        }
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    public void resetValues(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
}
