package com.myfirsttodoapp.sine.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sine on 5/6/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FirstToDoApp";

    private static final int DB_VER = 1;

    public static final String DB_TABLE = "Task";

    public static final String DB_COLUMN = "TaskName";

    public static final String DB_COLUMN_STATUS = "Completed";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,%s INTEGER DEFAULT 0);", DB_TABLE, DB_COLUMN, DB_COLUMN_STATUS);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String task) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public int getCompleted(String task) {
        int result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT %s, %s FROM %s", DB_COLUMN, DB_COLUMN_STATUS, DB_TABLE);
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            if (cursor.getString(0).equals(task)) {
                result = cursor.getInt(1);
            }
            break;
        }
        cursor.close();
        db.close();
        return result;
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN + " = ?", new String[]{task});
        db.close();
    }

    public void ChangeStatus(String task, int completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DB_COLUMN_STATUS, completed);
        db.update(DB_TABLE, cv, DB_COLUMN  + " = '" + task + "'", null);
        db.close();
    }

    public void doneTask(String task, int completed) {
        ChangeStatus(task, completed);
    }

    public void updateTask(String orginalTask, String newTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DB_COLUMN, newTask);
        db.update(DB_TABLE, cv, DB_COLUMN  + " = '" + orginalTask + "'", null);
        db.close();
    }

    public ArrayList<String> getTaskList(int completed) {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN}, DB_COLUMN_STATUS + " = " + completed, null, null, null, null);

        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }

        cursor.close();
        db.close();
        return taskList;
    }
}
