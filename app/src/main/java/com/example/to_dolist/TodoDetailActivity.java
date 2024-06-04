package com.example.to_dolist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TodoDetailActivity extends AppCompatActivity {
    String function;
    int todoId;
    TextView textviewTodoName;
    TextView textviewDateOfTodo;
    TextView textviewDetailOfTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);
        setTitle("TODO Detail");

        // Initialize views
        textviewTodoName = findViewById(R.id.textview_todo_name);
        textviewDateOfTodo = findViewById(R.id.textview_date_of_todo);
        textviewDetailOfTodo = findViewById(R.id.textview_detai_of_todo);

        // Determine function
        if (getIntent().hasExtra("function")) {
            function = getIntent().getStringExtra("function");
        }

        // If function is update, set data to input fields
        if (getIntent().hasExtra("todoId") && getIntent().hasExtra("todoName") && getIntent().hasExtra("dateOfTodo")
                && getIntent().hasExtra("todoDescription")) {
            setTitle(getIntent().getStringExtra("todoName"));
            todoId = getIntent().getIntExtra("todoId", -1);
            textviewTodoName.setText(getIntent().getStringExtra("todoName"));
            textviewDateOfTodo.setText(getIntent().getStringExtra("dateOfTodo"));
            textviewDetailOfTodo.setText(getIntent().getStringExtra("todoDescription"));
        }
    }
}
