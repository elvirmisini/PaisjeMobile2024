package com.example.g1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.widget.CheckBox;

import java.util.HashMap;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<String> tasks;
    private HashMap<Integer, Boolean> checkedStates; // To store checked state of each task
    private onTaskClickListener listener;

    public TaskAdapter(ArrayList<String> tasks) {
        this.tasks = tasks;
        this.checkedStates = new HashMap<>();
    }

    public void setOnTaskClickListener(onTaskClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        String taskName = tasks.get(position);
        holder.taskName.setText(taskName);

        // Handle checkbox state
        boolean isChecked = checkedStates.getOrDefault(position, false);
        holder.taskCheckBox.setChecked(isChecked);

        // Listen for checkbox changes
        holder.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            checkedStates.put(position, isChecked1);
        });

        // Listen for item clicks
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTaskClick(position, taskName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        CheckBox taskCheckBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
        }
    }

    public interface onTaskClickListener {
        void onTaskClick(int position, String taskName);
    }

    public HashMap<Integer, Boolean> getCheckedStates() {
        return checkedStates;
    }
}
