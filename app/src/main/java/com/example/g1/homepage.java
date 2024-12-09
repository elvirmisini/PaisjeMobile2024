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

public class homepage extends AppCompatActivity {

    private DB db;
    private RecyclerView taskList;
    private TaskAdapter adapter;
    private ArrayList<String> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        db=new DB(this);
        tasks=new ArrayList<>();
        taskList=findViewById(R.id.taskList);
        adapter=new TaskAdapter(tasks);

        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(adapter);

        adapter.setOnTaskClickListener((position,taskName)->{
            EditText editText=findViewById(R.id.editTextText3);
            editText.setText(taskName);
        });

        loadTasks();

        Button addButton=findViewById(R.id.button4);
        Button deleteButton=findViewById(R.id.deleteTask);
        EditText taskInput=findViewById(R.id.editTextText3);

        addButton.setOnClickListener(view->{
            String taskName=taskInput.getText().toString().trim();

            if(!taskName.isEmpty())
            {
                if(db.insertTask(taskName)){
                    tasks.add(taskName);
                    adapter.notifyItemInserted(tasks.size()-1);
                    taskInput.setText("");
                    Toast.makeText(this,"Task added",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"Failed to add task!",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Task name can not be empty!",Toast.LENGTH_SHORT).show();
            }

        });

        deleteButton.setOnClickListener(view -> {
            if (!tasks.isEmpty()) {
                boolean isDeleted = db.deleteTask(); // Use your updated deleteTask method
                if (isDeleted) {
                    tasks.remove(tasks.size() - 1); // Update the local list
                    adapter.notifyItemRemoved(tasks.size());
                    Toast.makeText(this, "Last task deleted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete task!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No tasks to delete!", Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteCheckedButton = findViewById(R.id.deleteCheckedButton); // Add this button in `homepage.xml`

        deleteCheckedButton.setOnClickListener(view -> {
            HashMap<Integer, Boolean> checkedStates = adapter.getCheckedStates();

            // Collect names of tasks to delete
            ArrayList<Integer> indexesToRemove = new ArrayList<>();
            ArrayList<String> tasksToRemoveByName = new ArrayList<>();

            for (int i = 0; i < tasks.size(); i++) {
                if (checkedStates.getOrDefault(i, false)) {
                    indexesToRemove.add(i);
                    tasksToRemoveByName.add(tasks.get(i));
                }
            }

            if (tasksToRemoveByName.isEmpty()) {
                // If no tasks are checked, show a message
                Toast.makeText(this, "No tasks selected for deletion!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build confirmation message
            StringBuilder message = new StringBuilder("Are you sure you want to delete these tasks?\n");
            for (String task : tasksToRemoveByName) {
                message.append("- ").append(task).append("\n");
            }

            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage(message.toString())
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // If user confirms, delete the tasks
                        for (int i = indexesToRemove.size() - 1; i >= 0; i--) {
                            int index = indexesToRemove.get(i);
                            String taskName = tasksToRemoveByName.get(indexesToRemove.size() - 1 - i);

                            boolean isDeleted = db.deleteTaskByName(taskName); // Delete task from DB
                            if (isDeleted) {
                                tasks.remove(index);
                                adapter.notifyItemRemoved(index);
                                checkedStates.remove(index); // Update the checked states
                            }
                        }

                        // Notify the user of success
                        Toast.makeText(this, "Selected tasks deleted!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null) // Dismiss the dialog if "No" is clicked
                    .show();
        });



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void loadTasks(){
        tasks.clear();

        Cursor cursor=db.getAllTasks();

        while (cursor.moveToNext()){
            tasks.add(cursor.getString(1));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}