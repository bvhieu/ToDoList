package com.example.to_dolist.objects;

import java.util.Date;

public class Todo {
    public static String TABLE_NAME = "todo";
    public static String COLUMN_ID = "todo_id";
    public static String COLUMN_NAME = "todo_name";
    public static String COLUMN_TODO_DATE = "date_of_todo";
    public static String COLUMN_TODO_DESCRIPTION = "todo_description";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_TODO_DATE + " TEXT NOT NULL, " +
            COLUMN_TODO_DESCRIPTION + " TEXT)";

    private int todo_id;
    private String todo_name;
    private Date date_of_todo;
    private String todo_description;

    public Todo(int todo_id, String todo_name, Date date_of_todo, String todo_description) {
        this.todo_id = todo_id;
        this.todo_name = todo_name;
        this.date_of_todo = date_of_todo;
        this.todo_description = todo_description;
    }

    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getTodo_name() {
        return todo_name;
    }

    public void setTodo_name(String todo_name) {
        this.todo_name = todo_name;
    }

    public Date getDate_of_todo() {
        return date_of_todo;
    }

    public void setDate_of_todo(Date date_of_todo) {
        this.date_of_todo = date_of_todo;
    }

    public String getTodo_description() {
        return todo_description;
    }

    public void setTodo_description(String todo_description) {
        this.todo_description = todo_description;
    }
}
