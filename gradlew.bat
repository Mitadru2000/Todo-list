package com.example.android.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.todo.database.Task;
import com.example.android.todo.database.TaskDatabase;
import com.example.android.todo.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    TaskDatabase td;
    Task task;
    String p;
    Bundle extras;
    int item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add);
        Intent i=getIntent();
        extras=i.getExtras();
        if(extras!=null){
            item=extras.getInt("id");
            setTitle("Update Todo");
            binding.addbutton.setVisibility(View.GONE);
            binding.updatebutton.setVisibility(View.VISIBLE);
            AppExecutor.getInstance().getdiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    td=TaskDatabase.getInstance(getApplicationContext());
                    task=td.taskdao().getbyid(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.add.setText(task.getTaskname());
                        }
                    });


                }
            });
        }
        else {
            binding.addbutton.setVisibility(View.VISIBLE);
            binding.updatebutton.setVisibility(View.GONE);
        }
        if(extras!=null) {
            binding.updatebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else{
        binding.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                td=TaskDatabase.getInstance(getApplicationContex