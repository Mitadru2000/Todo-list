package com.example.android.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.activities.data.ActivityContract.TaskEntry;

public class Activitydb extends SQLiteOpenHelper {
    public final static String Database_Name="Activity.db";
    public static final int Database_version=1;
    String sql="CREATE TABLE "+ TaskEntry.TableName+"("+TaskEntry.Column_Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"+TaskEntry.ColumnTaskName+" TEXT NOT NULL,"+TaskEntry.ColumnTaskDescription+" TEXT NOT NULL,"+TaskEntry.ColumnTaskPriority+" INTEGER NOT NULL DEFAULT 0,"+TaskEntry.ColumnTaskTime+" TEXT NOT NULL);";
    Activitydb(Context context)
    {
        super(context,Database_Name,null,Database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
