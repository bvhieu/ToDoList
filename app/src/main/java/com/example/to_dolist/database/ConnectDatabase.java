package com.example.to_dolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.to_dolist.objects.Todo;

public class ConnectDatabase extends SQLiteOpenHelper {
    private Context context;

    public ConnectDatabase(Context context) {
        super(context, "To-doListManagementDatabase", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        sqLiteDatabase.execSQL(Todo.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Todo.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //Methods for To-do object

    public long addNewTodo(String todoName, String dateOfTodo,String todoDescription) {
        long result = -1;
        ContentValues values = createTodoContentValues( todoName, dateOfTodo, todoDescription);
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            result = sqLiteDatabase.insert(Todo.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Cursor getAllTodo() {
        String query = "SELECT * FROM " + Todo.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public long updateTodo(String todoId, String todoName, String dateOfTodo,String todoDescription) {
        long result = -1;
        ContentValues values = createTodoContentValues(todoName, dateOfTodo, todoDescription);
        try (SQLiteDatabase sqLiteDatabase = this.getWritableDatabase()) {
            result = sqLiteDatabase.update(Todo.TABLE_NAME, values, Todo.COLUMN_ID + "=?", new String[]{todoId});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ContentValues createTodoContentValues(String todoName, String dateOfTodo,String todoDescription) {
        ContentValues values = new ContentValues();
        values.put(Todo.COLUMN_NAME, todoName);
        values.put(Todo.COLUMN_TODO_DATE, dateOfTodo);
        values.put(Todo.COLUMN_TODO_DESCRIPTION, todoDescription);
        return values;
    }

    public long deleteOneTodo(String todId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(Todo.TABLE_NAME, Todo.COLUMN_ID + "=?", new String[]{todId});
        return result;
    }

    public void deleteAllTodo() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + Todo.TABLE_NAME);
    }

    public Cursor searchTodo(String keyWord) {
        String query = "SELECT * FROM " + Todo.TABLE_NAME+ " WHERE "
                + Todo.COLUMN_NAME + " LIKE '%" + keyWord + "%'"
                + " OR " + Todo.COLUMN_TODO_DATE + " LIKE '%" + keyWord + "%'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor sortTodo(int order) {
        String orderByClause = (order == 0) ? "ASC" : "DESC";
        String query = "SELECT * FROM " + Todo.TABLE_NAME +
                " ORDER BY " + Todo.COLUMN_NAME + " " + orderByClause;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }
}
