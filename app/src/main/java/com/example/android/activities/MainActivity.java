package com.example.android.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.activities.data.ActivityContract;
import com.example.android.activities.data.ActivityContract.TaskEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    Activitydb pd;
    com.example.android.activities.ActivityAdapter activityAdapter;
    int id_breed,id_name;
    String name,breed;
    private final int Loader_id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }

        });
        getLoaderManager().initLoader(Loader_id,null,this);
        activityAdapter=new com.example.android.activities.ActivityAdapter(this,null);
        ListView l1=(ListView)findViewById(R.id.text_view_task);

        l1.setAdapter(activityAdapter);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MainActivity.this,EditorActivity.class);
                Uri uri= ContentUris.withAppendedId(ActivityContract.General_content_uri,id);
                i.setData(uri);
                startActivity(i);
            }
        });

        View emptyView=(View)findViewById(R.id.empty);
        l1.setEmptyView(emptyView);

    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_task:
                insert();

                return true;
            case R.id.action_delete_all_entries:
                delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insert(){
        ContentValues con=new ContentValues();
        con.put(TaskEntry.ColumnTaskName,"Relax");
        con.put(TaskEntry.ColumnTaskDescription,"Do nothing");
        con.put(TaskEntry.ColumnTaskTime,"Anytime today");
        con.put(TaskEntry.ColumnTaskPriority,TaskEntry.Priority_High);
        getContentResolver().insert(ActivityContract.General_content_uri,con);
    }

    private void delete(){
        getContentResolver().delete(ActivityContract.General_content_uri,null,null);
    }



    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection={TaskEntry.Column_Id,TaskEntry.ColumnTaskName,TaskEntry.ColumnTaskDescription,TaskEntry.ColumnTaskTime,TaskEntry.ColumnTaskPriority};
        return new CursorLoader(this, ActivityContract.General_content_uri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       activityAdapter.swapCursor(data);
    }



    @Override
    public void onLoaderReset(@NonNull Loader loader) {
      activityAdapter.swapCursor(null);
    }
}