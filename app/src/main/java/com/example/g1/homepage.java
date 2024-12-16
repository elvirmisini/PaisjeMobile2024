package com.example.g1;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import android.widget.TextView;

public class homepage extends AppCompatActivity {

    private DB db;
    private RecyclerView taskList;
    private TaskAdapter adapter;
    private ArrayList<String> tasks;
    private TextView taskCountText; // Added TextView to display task count

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        db = new DB(this);
        tasks = new ArrayList<>();
        taskList = findViewById(R.id.taskList);
        taskCountText = findViewById(R.id.taskCountText); // Initialize task count TextView
        adapter = new TaskAdapter(tasks);

        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(adapter);

        adapter.setOnTaskClickListener((position, taskName) -> {
            EditText editText = findViewById(R.id.editTextText3);
            editText.setText(taskName);
        });

        loadTasks();

        Button addButton = findViewById(R.id.button4);
        Button deleteButton = findViewById(R.id.deleteTask);
        EditText taskInput = findViewById(R.id.editTextText3);

        addButton.setOnClickListener(view -> {
            String taskName = taskInput.getText().toString().trim();

            if (!taskName.isEmpty()) {
                if (db.insertTask(taskName)) {
                    tasks.add(taskName);
                    adapter.notifyItemInserted(tasks.size() - 1);
                    taskInput.setText("");
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
                    updateTaskCount(); // Update the task count after adding
                } else {
                    Toast.makeText(this, "Failed to add task!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(view -> {
            if (!tasks.isEmpty()) {
                boolean isDeleted = db.deleteTask();
                if (isDeleted) {
                    tasks.remove(tasks.size() - 1);
                    adapter.notifyItemRemoved(tasks.size());
                    Toast.makeText(this, "Last task deleted!", Toast.LENGTH_SHORT).show();
                    updateTaskCount(); // Update the task count after deleting
                } else {
                    Toast.makeText(this, "Failed to delete task!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No tasks to delete!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTasks() {
        tasks.clear();

        Cursor cursor = db.getAllTasks();

        while (cursor.moveToNext()) {
            tasks.add(cursor.getString(1));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
        updateTaskCount(); // Update the task count after loading tasks
    }

    // Method to update the task count display
    private void updateTaskCount() {
        taskCountText.setText("Total Tasks: " + tasks.size());
    }
}
