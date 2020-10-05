package com.example.android.activities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.activities.data.ActivityContract;

public class ActivityAdapter extends CursorAdapter{



    public ActivityAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.taskdetails,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView t1=(TextView)view.findViewById(R.id.Name);
        TextView t2=(TextView)view.findViewById(R.id.Time);
        int id_time = cursor.getColumnIndex(ActivityContract.TaskEntry.ColumnTaskTime);
        String time= cursor.getString(id_time);
        int id_name=cursor.getColumnIndex(ActivityContract.TaskEntry.ColumnTaskName);
        String name=cursor.getString(id_name);
        t1.setText(name);
        t2.setText(time);
    }
}
