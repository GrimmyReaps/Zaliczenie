package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button addItem;
    RecyclerView recyclerView;
    ArrayList<TaskModal> tasks;
    private  DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(MainActivity.this);

        dbHandler.addNewTask("FirstTask", "No Content", "22-02-2024", "LOW");
        tasks = dbHandler.readTasks();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        CustomAdapter adapter = new CustomAdapter(tasks, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addItem = (Button) findViewById(R.id.addItem);
        addItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addItem.getId()){
            Bundle sendInfo = new Bundle();
            sendInfo.putBoolean("isAdding", true);

            Intent intent = new Intent(getApplicationContext(), CreateEditTask.class);
            intent.putExtras(sendInfo);
            startActivity(intent);
        }
    }
}