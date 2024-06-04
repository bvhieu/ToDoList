package com.example.to_dolist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.AddToDoActivity;
import com.example.to_dolist.R;
import com.example.to_dolist.ToDoActivity;
import com.example.to_dolist.database.ConnectDatabase;
import com.example.to_dolist.objects.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

        Context context;
        private ArrayList<Todo> adapterData;

        public TodoAdapter(Context context, ArrayList<Todo> adapterData) {
            this.context = context;
            this.adapterData = adapterData;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.todo_recycle_view_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Todo currentTodo = adapterData.get(position);

            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
            holder.txt_todo_id.setText(String.valueOf(currentTodo.getTodo_id()));
            holder.txt_todo_name.setText(currentTodo.getTodo_name());
            holder.txt_date_of_todo.setText(dateFormat.format(currentTodo.getDate_of_todo()));

            setTodoDetailOnClickListener(holder.image_todo_update, currentTodo);

            setTodoItemLongClickListener(holder.todoItemLayout, currentTodo.getTodo_name(), currentTodo.getTodo_id());
        }


        private void setTodoDetailOnClickListener(ImageView imageView, Todo todo) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddToDoActivity.class);
                    intent.putExtra("function", "UPDATE");
                    intent.putExtra("todoId", todo.getTodo_id());
                    intent.putExtra("todoName", todo.getTodo_name());
                    String myFormat = "MM/dd/yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
                    intent.putExtra("dateOfTodo", dateFormat.format(todo.getDate_of_todo()));
                    intent.putExtra("todoDescription", todo.getTodo_description());
                    context.startActivity(intent);
                }
            });
        }

        private void setTodoItemLongClickListener(View view, String todoName, int todoId) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Remove " + todoName + "?");
                    builder.setMessage("Are you sure to remove " + todoName + "?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConnectDatabase connectDatabase = new ConnectDatabase(context);
                            long result = connectDatabase.deleteOneTodo(String.valueOf(todoId));
                            if (result == -1) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Remove successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, ToDoActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return adapterData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_todo_id, txt_todo_name, txt_date_of_todo;
            ImageView image_todo_update;
            LinearLayout todoItemLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_todo_id = itemView.findViewById(R.id.textview_todo_id);
                txt_todo_name = itemView.findViewById(R.id.textview_todo_name);
                txt_date_of_todo = itemView.findViewById(R.id.textview_date_of_todo);
                image_todo_update = itemView.findViewById(R.id.image_todo_update);
                todoItemLayout = itemView.findViewById(R.id.todoItemLayout);
            }
        }
    }
