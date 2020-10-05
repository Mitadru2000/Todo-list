package com.example.android.activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import android.app.LoaderManager;

import com.example.android.activities.R;
import com.example.android.activities.data.ActivityContract;
import com.example.android.activities.data.ActivityContract.TaskEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    com.example.android.activities.Activitydb pd;
    Uri uri;

    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private EditText mTimeEditText;
    private Spinner mPrioritySpinner;

    private int mPriority = 0;
    int Loader_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);

        mNameEditText = (EditText) findViewById(R.id.edit_task_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_task_description);
        mTimeEditText = (EditText) findViewById(R.id.edit_task_time);
        mPrioritySpinner = (Spinner) findViewById(R.id.spinner_priority);

        setupSpinner();
        pd = new com.example.android.activities.Activitydb(this);

        Intent i = getIntent();
        uri = i.getData();
        if (uri == null) {
            setTitle("Add a task");
            invalidateOptionsMenu();
        }
        else {
            setTitle("Edit a task");
            getLoaderManager().initLoader(Loader_id,null,this);
        }



    }


    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mPrioritySpinner.setAdapter(genderSpinnerAdapter);
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.priority_high))) {
                        mPriority = TaskEntry.Priority_High;
                    } else if (selection.equals(getString(R.string.priority_low))) {
                        mPriority = TaskEntry.Priority_Low;
                    } else {
                        mPriority = TaskEntry.Priority_Medium;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPriority = 0;
            }
        });
    }

    private void insert() {
        ContentValues con = new ContentValues();
        con.put(TaskEntry.ColumnTaskName, (mNameEditText.getText()).toString());
        if(TextUtils.isEmpty((mDescriptionEditText.getText()).toString())){mDescriptionEditText.setText("No description added");}
        con.put(TaskEntry.ColumnTaskDescription, (mDescriptionEditText.getText()).toString());
        if(TextUtils.isEmpty((mTimeEditText.getText()).toString())==true){mTimeEditText.setText("12:00 pm today");}
        con.put(TaskEntry.ColumnTaskTime, (mTimeEditText.getText()).toString());
        con.put(TaskEntry.ColumnTaskPriority, mPriority);
        if(TextUtils.isEmpty((mNameEditText.getText()).toString())==false && TextUtils.isEmpty((mDescriptionEditText.getText()).toString())==false && TextUtils.isEmpty((mTimeEditText.getText()).toString())==false){
            getContentResolver().insert(ActivityContract.General_content_uri, con);}
        else{finish();}
    }


      private void update(){
      ContentValues con=new ContentValues();
      con.put(TaskEntry.ColumnTaskName,(mNameEditText.getText()).toString());
      con.put(TaskEntry.ColumnTaskDescription,(mDescriptionEditText.getText()).toString());
      con.put(TaskEntry.ColumnTaskTime,(mTimeEditText.getText()).toString());
      con.put(TaskEntry.ColumnTaskPriority,mPriority);
      String selection=TaskEntry.Column_Id+"=?";
      String[] selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
      getContentResolver().update(uri,con,selection,selectionArgs);
      }

      private void delete(){
        String selection=TaskEntry.Column_Id+"=?";
        String[] selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
       getContentResolver().delete(uri,selection,selectionArgs);
      }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_save:
                    if(uri==null){
                    insert();}
                    else{
                        update();
                    }
                    finish();
                    return true;
                case R.id.action_delete:
                    if(uri!=null)
                        delete();
                    finish();
                    return true;
                case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                    return true;

            }

            return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={TaskEntry.Column_Id,TaskEntry.ColumnTaskName,TaskEntry.ColumnTaskDescription,TaskEntry.ColumnTaskTime,TaskEntry.ColumnTaskPriority};
        return new CursorLoader(this,uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int name_index=0,description_index,time_index,priority_index;
        if(data.moveToFirst()){
            name_index=data.getColumnIndex(TaskEntry.ColumnTaskName);
            description_index=data.getColumnIndex(TaskEntry.ColumnTaskDescription);
            time_index=data.getColumnIndex(TaskEntry.ColumnTaskTime);
            priority_index=data.getColumnIndex(TaskEntry.ColumnTaskPriority);
            String name=data.getString(name_index);
            String description=data.getString(description_index);
            String time=data.getString(time_index);
            switch(data.getInt(priority_index)){
                case 0:
                    mPrioritySpinner.setSelection(0);
                    break;
                case 1:
                    mPrioritySpinner.setSelection(1);
                    break;
                case 2:
                    mPrioritySpinner.setSelection(2);

            }
            mNameEditText.setText(name);
            mDescriptionEditText.setText(description);
            mTimeEditText.setText(time);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (uri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
}
