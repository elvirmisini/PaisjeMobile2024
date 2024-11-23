package com.example.g1;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

        loadTasks();

        Button addButton=findViewById(R.id.button4);
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