package com.myfirsttodoapp.sine.todoapp;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> mAdapter;

    ListView lstTask;

    DatabaseHelper databaseHelper;

    boolean completed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lstTask);
//        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.setAdapter(new TaskAdapter(this));

        lstTask = (ListView)findViewById(R.id.lstTask);

        setTitle("Todo List");
        loadTaskList();
    }

    private void loadTaskList() {
        int type = 0;
        if (completed) {
            type = 1;
        }
        ArrayList<String> taskList = databaseHelper.getTaskList(type);

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.task_title, taskList);
            lstTask.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                databaseHelper.insertNewTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();

                dialog.show();
                return true;
            case R.id.done_task_list:
                if (completed) {
                    completed = false;
                } else {
                    completed = true;
                }
                loadTaskList();
                if (completed) {
                    item.setTitle("Todo List");
                    setTitle("Completed List");
                } else {
                    item.setTitle("Completed List");
                    setTitle("Todo List");
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void doneTask(View view) {
        int markCompleted = 0;
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        ImageButton btnDone = (ImageButton)findViewById(R.id.btnDone);
        String task = String.valueOf(taskTextView.getText());
        if (databaseHelper.getCompleted(task) == 0) {
            markCompleted = 1;
//            btnDone.setBackgroundResource(R.mipmap.ic_correct);
        } else if (databaseHelper.getCompleted(task) == 1) {
            markCompleted = 0;
//            btnDone.setBackgroundResource(R.mipmap.ic_incorrect);
        }
        databaseHelper.doneTask(task, markCompleted);
        loadTaskList();
    }

    public void editTask(View view) {
        View parent = (View)view.getParent();
        final EditText taskEditText = new EditText(this);
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        final String oldTask = String.valueOf(taskTextView.getText());
        taskEditText.setText(oldTask);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Task")
                .setView(taskEditText)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTask = String.valueOf(taskEditText.getText());
                        databaseHelper.updateTask(oldTask, newTask);
                        loadTaskList();
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteTask(oldTask);
                        loadTaskList();
                    }
                })
                .create();

        dialog.show();
    }
}
