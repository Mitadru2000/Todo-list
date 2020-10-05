package com.example.android.activities.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ActivityContract {
    private ActivityContract(){};
    public final class TaskEntry implements BaseColumns{
        public final static String TableName="Tasks";
        public final static String Column_Id=BaseColumns._ID;
        public final static String ColumnTaskName="Name";
        public final static String ColumnTaskDescription="Description";
        public final static String ColumnTaskTime="Time";
        public final static String ColumnTaskPriority="Priority";

        public final static int Priority_High=1;
        public final static int Priority_Low=2;
        public final static int Priority_Medium=0;


    }
    public final static String scheme="content://";
    public final static String authority="com.example.android.activities";
    public final static Uri Base_content_uri=Uri.parse(scheme+authority);
    public final static String type_of_data="activities";
    public final static Uri General_content_uri=Uri.withAppendedPath(Base_content_uri,type_of_data);
    public final static String type_of_data_specific="activities/#";
    public final static Uri Specific_content_uri=Uri.withAppendedPath(Base_content_uri,type_of_data_specific);

    public final static String dir= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+authority+"/"+type_of_data;
    public final static String item=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+authority+"/"+type_of_data_specific;
}
