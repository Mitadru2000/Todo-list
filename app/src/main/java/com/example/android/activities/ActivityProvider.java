package com.example.android.activities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.android.activities.data.ActivityContract;
import com.example.android.activities.data.ActivityContract.TaskEntry;


public class ActivityProvider extends ContentProvider {
   static UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
   Activitydb pd;

   static {
       uriMatcher.addURI(ActivityContract.authority, ActivityContract.type_of_data,1);
       uriMatcher.addURI(ActivityContract.authority, ActivityContract.type_of_data_specific,2);
   }

    @Override
    public boolean onCreate() {
        pd=new Activitydb(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) {
        SQLiteDatabase db=pd.getReadableDatabase();
        int code=uriMatcher.match(uri);
        Cursor cursor;
        switch (code){
            case 1:
                cursor=db.query(TaskEntry.TableName,projection,null,null,null,null,null);
                break;
            case 2:
                selection=TaskEntry.Column_Id+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(TaskEntry.TableName,projection,selection,selectionArgs,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Cannot find uri "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int code=uriMatcher.match(uri);
        switch(code)
        {
            case 1:

                return insertTask(uri,contentValues);
            default:
                throw new IllegalArgumentException("Cannot be inserted "+uri);
        }
    }

    Uri insertTask(Uri uri,ContentValues contentValues){

        String dummyname=contentValues.getAsString(TaskEntry.ColumnTaskName);
        if(dummyname==null){
            throw new IllegalArgumentException("Empty field");
        }
        String dummydescription=contentValues.getAsString(TaskEntry.ColumnTaskDescription);

        String dummytime=contentValues.getAsString(TaskEntry.ColumnTaskTime);
        if(dummytime==null){
            throw new IllegalArgumentException("Empty field");
        }

        int dummypriority=contentValues.getAsInteger(TaskEntry.ColumnTaskPriority);
        if(dummypriority!=TaskEntry.Priority_Medium && dummypriority!=TaskEntry.Priority_Low && dummypriority!=TaskEntry.Priority_High){
            throw new IllegalArgumentException("Empty field");
        }

        SQLiteDatabase db=pd.getWritableDatabase();
        long id=db.insert(TaskEntry.TableName,null,contentValues);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }



    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int code=uriMatcher.match(uri);
        switch (code){
            case 1:
                return updatetask(uri,contentValues,selection,selectionArgs);
            case 2:
                selection=TaskEntry.Column_Id+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return  updatetask(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Wrong uri");
        }
    }
    public int updatetask(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs){
          if(contentValues.containsKey(TaskEntry.ColumnTaskName)){
              String updatename=contentValues.getAsString(TaskEntry.ColumnTaskName);
              if(updatename==null){
                  throw new IllegalArgumentException("Name cannot be empty");
              }
          }
          if(contentValues.containsKey(TaskEntry.ColumnTaskTime)){
              String updatetime=contentValues.getAsString(TaskEntry.ColumnTaskTime);
              if(updatetime==null){
                  throw new IllegalArgumentException("Date and Time needs to be given");
              }

          }
          if(contentValues.containsKey(TaskEntry.ColumnTaskPriority)){
              int updatepriority=contentValues.getAsInteger(TaskEntry.ColumnTaskPriority);
              if(updatepriority!=TaskEntry.Priority_Medium && updatepriority!=TaskEntry.Priority_Low && updatepriority!=TaskEntry.Priority_High){
                  throw new IllegalArgumentException("No priority recognized");
              }
          }
          SQLiteDatabase db=pd.getWritableDatabase();
          getContext().getContentResolver().notifyChange(uri,null);
          return db.update(TaskEntry.TableName,contentValues,selection,selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code=uriMatcher.match(uri);
        SQLiteDatabase db=pd.getWritableDatabase();
        switch (code){
            case 1:
                getContext().getContentResolver().notifyChange(uri,null);
                return db.delete(TaskEntry.TableName,selection,selectionArgs);
            case 2:
                selection=TaskEntry.Column_Id+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri,null);
                return db.delete(TaskEntry.TableName,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Wrong uri");
        }
    }


    @Override
    public String getType(Uri uri) {
        int code=uriMatcher.match(uri);
        switch (code) {
            case 1:
                return ActivityContract.dir;
            case 2:
                return ActivityContract.item;
            default:
                throw new IllegalArgumentException("wrong uri");
        }
    }
}