package com.example.to_dolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_dolist.util.Memory;

public class ToDoActivity extends AppCompatActivity {
    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        addButton = findViewById(R.id.button_add_todo);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoActivity.this, AddToDoActivity.class);
                if (getIntent().hasExtra("todoId")) {
                    Memory.setTodoId(getIntent().getIntExtra("todoId", -1));
                }
                intent.putExtra("function", "ADD");
                startActivity(intent);

            }
        });
    }
}
