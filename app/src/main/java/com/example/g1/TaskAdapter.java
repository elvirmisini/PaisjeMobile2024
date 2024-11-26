package com.example.g1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<String> tasks;

    private onTaskClickListener listener;


    public TaskAdapter(ArrayList<String> tasks){
        this.tasks=tasks;
    }

    public void setOnTaskClickListener(onTaskClickListener listener){
        this.listener=listener;
    }


    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        holder.taskName.setText(tasks.get(position));
        holder.itemView.setOnClickListener(view ->{
            if(listener!=null){
                listener.onTaskClick(position,tasks.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView taskName;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName=itemView.findViewById(R.id.taskName);
        }
    }

    public interface onTaskClickListener{
        void onTaskClick(int position,String taskName);
    }
}
