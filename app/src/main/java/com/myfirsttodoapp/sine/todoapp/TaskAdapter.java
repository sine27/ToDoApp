package com.myfirsttodoapp.sine.todoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sine on 5/6/17.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    DatabaseHelper databaseHelper;

    Context context;
    ArrayList<String> taskList;

    boolean completed = false;

    public TaskAdapter(Context context) {
        this.context = context;
        int type = 0;
        if (completed) {
            type = 1;
        }
        taskList = databaseHelper.getTaskList(type);
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;
        ImageButton btnDone;
        public ViewHolder (View itemView) {
            super(itemView);
            TextView taskTextView = (TextView) itemView.findViewById(R.id.task_title);
            ImageButton btnDone = (ImageButton) itemView.findViewById(R.id.btnDone);
        }
    }
}
