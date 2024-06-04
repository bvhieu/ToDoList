package com.example.to_dolist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.database.ConnectDatabase;
import com.example.to_dolist.objects.Todo;
import com.example.to_dolist.util.Memory;
import com.example.to_dolist.adapter.TodoAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ToDoActivity extends AppCompatActivity {
    Button addButton;
    RecyclerView todo_recycle_view;
    TodoAdapter todo_recycle_view_adapter;
    ConnectDatabase connectDatabase;
    ArrayList<Todo> adapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        connectDatabase = new ConnectDatabase(this);
        adapterData = new ArrayList<>();
        todo_recycle_view = findViewById(R.id.recycle_view_todo);
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

        getAllTodo();
        todo_recycle_view_adapter = new TodoAdapter(ToDoActivity.this, adapterData);
        todo_recycle_view.setAdapter(todo_recycle_view_adapter);
        todo_recycle_view.setLayoutManager(new LinearLayoutManager(ToDoActivity.this));

    }
    private void getAllTodo() {
        Cursor cursor = connectDatabase.getAllTodo();
        if (cursor.getCount() != 0) {
            setAdapterData(cursor);
        }
    }

    private void sortTodo(int order) {
        Cursor cursor = connectDatabase.sortTodo(order);
        if (cursor.getCount() != 0) {
            setAdapterData(cursor);
        }
    }

    private void searchTodo(String keyWord) {
        Cursor cursor = connectDatabase.searchTodo(keyWord);
        if (cursor.getCount() != 0) {
            setAdapterData(cursor);
        }
    }

    @SuppressLint("Range")
    private void setAdapterData(Cursor cursor) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        Date dateOfTodo = null;
        while (cursor.moveToNext()) {
            try {
                dateOfTodo = dateFormat.parse(cursor.getString(cursor.getColumnIndex(Todo.COLUMN_TODO_DATE)));
            } catch (Exception ex) {
                Toast.makeText(this, "Get data error", Toast.LENGTH_SHORT).show();
            }
            Todo newTodo = new Todo(
                    cursor.getInt(cursor.getColumnIndex(Todo.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Todo.COLUMN_NAME)),
                    dateOfTodo,
                    cursor.getString(cursor.getColumnIndex(Todo.COLUMN_TODO_DESCRIPTION))
            );
            adapterData.add(newTodo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterData.clear();
                searchTodo(s);
                todo_recycle_view_adapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.removeAll) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ToDoActivity.this);
            builder.setTitle("Remove all todo?");
            builder.setMessage("Are you sure to remove all todo?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    connectDatabase.deleteAllTodo();
                    adapterData.clear();
                    todo_recycle_view_adapter.notifyDataSetChanged();
                    Intent intent = new Intent(ToDoActivity.this, ToDoActivity.class);
                    Toast.makeText(ToDoActivity.this, "Remove successfully", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
        if (item.getItemId() == R.id.sortAToZ) {
            adapterData.clear();
            sortTodo(0);
            todo_recycle_view_adapter.notifyDataSetChanged();
        }
        if (item.getItemId() == R.id.sortZToA) {
            adapterData.clear();
            sortTodo(1);
            todo_recycle_view_adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
