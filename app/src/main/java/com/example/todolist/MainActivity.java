package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button editItem, addItem, deleteItem;
    RecyclerView recyclerView;
    ArrayList<Task> tasks;
    private  DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tasks = Task.createTasksList(30);
        CustomAdapter adapter = new CustomAdapter(tasks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addItem = (Button) findViewById(R.id.addItem);
        addItem.setOnClickListener(this);
    }

    private void xmlSetup(){

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