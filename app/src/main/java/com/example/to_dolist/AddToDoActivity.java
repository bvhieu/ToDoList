package com.example.to_dolist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddToDoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        setTitle("Add new TODO");
    }
}
