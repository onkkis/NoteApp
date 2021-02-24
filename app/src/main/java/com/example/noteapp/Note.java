package com.example.noteapp;

public class Note {

    private String title;
    private String text;

    public Note(String title, String text){
        this.title = title;
        this.text = text;
    }

    public Note(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
