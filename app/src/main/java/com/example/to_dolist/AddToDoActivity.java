package com.example.to_dolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_dolist.database.ConnectDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddToDoActivity extends AppCompatActivity {

    String function;
    int todoId;
    Button btn_save_todo;
    TextInputEditText txt_todo_name, txt_date_of_todo, txt_todo_description;
    private ConnectDatabase connectDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        setTitle("Add new TODO");

        connectDatabase = new ConnectDatabase(this);
        //set interface variable
        btn_save_todo = findViewById(R.id.btn_save_todo);
        txt_todo_name = findViewById(R.id.txt_todo_name);
        txt_date_of_todo = findViewById(R.id.txt_date_of_todo);
        txt_todo_description = findViewById(R.id.txt_todo_description);

        //date picker
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                // Time picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddToDoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String myFormat = "MM/dd/yyyy HH:mm";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
                        txt_date_of_todo.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        };
        txt_date_of_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddToDoActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Determine function
        if (getIntent().hasExtra("function")) {
            function = getIntent().getStringExtra("function");
        }

        //if function is update, set data to input field
        if (function.equals("UPDATE") && getIntent().hasExtra("todoId") && getIntent().hasExtra("todoName") && getIntent().hasExtra("dateOfTodo")
                && getIntent().hasExtra("todoDescription")) {
            setTitle(getIntent().getStringExtra("todoName"));
            todoId = getIntent().getIntExtra("todoId", -1);
            txt_todo_name.setText(getIntent().getStringExtra("todoName"));
            txt_date_of_todo.setText(getIntent().getStringExtra("dateOfTodo"));
            txt_todo_description.setText(getIntent().getStringExtra("todoDescription"));
            btn_save_todo.setText("Update");
        }

        btn_save_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validation
                if (function.equals("UPDATE")) {

                }
                List<Boolean> checkResults = new ArrayList<Boolean>();
                checkResults.add(isTextInputEditTextEmpty(txt_todo_name));
                checkResults.add(isTextInputEditTextEmpty(txt_date_of_todo));

                if (!checkResults.contains(false)) {
                    //Get data from UI
                    String todoName = txt_todo_name.getText().toString().trim();
                    String dateOfTodo = txt_date_of_todo.getText().toString().trim();
                    String todoDescription = txt_todo_description.getText().toString().trim();

                    //add function
                    if (function.equals("ADD")) {
                        //Add data to database
                        long result = connectDatabase.addNewTodo(todoName, dateOfTodo, todoDescription);
                        if (result == -1) {
                            Toast.makeText(AddToDoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            //Refresh input if success
                            refreshInputForm();
                            Toast.makeText(AddToDoActivity.this, "Success", Toast.LENGTH_SHORT).show();

//                            finish();
                        }
                    } else {
                        //update function
                        long result = connectDatabase.updateTodo(todoId + "", todoName, dateOfTodo, todoDescription);
                        if (result == -1) {
                            Toast.makeText(AddToDoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddToDoActivity.this, "Success", Toast.LENGTH_SHORT).show();

//                            finish();
                        }
                    }
                }
            }
        });
    }

    //validation
    private boolean isTextInputEditTextEmpty(TextInputEditText txt) {

        if (txt.getText().toString().trim().length() == 0) {
            txt.setError("This field is required");
            return false;
        }
        return true;
    }

    //Refresh input form
    private void refreshInputForm() {
        txt_todo_name.setText("");
        txt_date_of_todo.setText("");
        txt_todo_description.setText("");
    }
}
