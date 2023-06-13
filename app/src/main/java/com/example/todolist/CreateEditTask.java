package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEditTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Button createNewTaskBtn;
    Button cancelBtn;
    Button editTaskBtn;
    Button deleteBtn;
    Button markAsComplete;
    EditText newTaskNameTxt;
    EditText newTaskDeadlineTxt;
    TextInputEditText newTaskContentTxt;
    TextView newTaskPriorityChoice;
    Spinner priorityChoice;
    String taskName;
    String[] priorities = {"LOW", "MEDIUM", "HIGH"};
    private ArrayList<TaskModal> taskModalArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    newTaskDeadlineTxt.setText(current);
                    newTaskDeadlineTxt.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        newTaskNameTxt = (EditText) findViewById(R.id.newTaskName);
        newTaskDeadlineTxt = (EditText) findViewById(R.id.newDeadline);
        newTaskContentTxt = (TextInputEditText) findViewById(R.id.newTaskContent);
        newTaskDeadlineTxt.addTextChangedListener(tw);

        createNewTaskBtn = (Button) findViewById(R.id.createNewTask);
        cancelBtn = (Button) findViewById(R.id.cancel);
        editTaskBtn = (Button) findViewById(R.id.editTask);
        deleteBtn = (Button) findViewById(R.id.deleteTask);
        markAsComplete = (Button) findViewById(R.id.markAsComplete);
        createNewTaskBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        editTaskBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        markAsComplete.setOnClickListener(this);


        priorityChoice = (Spinner) findViewById(R.id.choosePriority);
        priorityChoice.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityChoice.setAdapter(aa);

        newTaskPriorityChoice = (TextView) findViewById(R.id.priorityDisplay);

        DBHandler dbHandler = new DBHandler(CreateEditTask.this);
        Intent caller = getIntent();
        Bundle sendInfo = caller.getExtras();
        boolean isAdding = sendInfo.getBoolean("isAdding");
        if (isAdding){
            editTaskBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }else{
            createNewTaskBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            taskName = sendInfo.getString("TaskName");

            taskModalArrayList = dbHandler.fetchRow(taskName);
            TaskModal modal = taskModalArrayList.get(0);
            newTaskNameTxt.setText(modal.getTaskName());
            newTaskContentTxt.setText(modal.getTaskContent());
            newTaskDeadlineTxt.setText(modal.getTaskDeadline());
            if(modal.getTaskPriority().equals(priorities[0])) {
                priorityChoice.setSelection(0);
            }else if(modal.getTaskPriority().equals(priorities[1])){
                priorityChoice.setSelection(1);
            }else{
                priorityChoice.setSelection(2);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        newTaskPriorityChoice.setText(priorities[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        newTaskPriorityChoice.setText("LOW");
    }

    @Override
    public void onClick(View view) {
        DBHandler dbHandler = new DBHandler(CreateEditTask.this);
        if(view.getId() == createNewTaskBtn.getId()){
            String toCheck = String.valueOf(newTaskDeadlineTxt.getText());
            if(!toCheck.matches("^[\\d]{2}\\/[\\d]{2}\\/[\\d]{4}$")){
                Toast.makeText(view.getContext(), "Zły deadline podany", Toast.LENGTH_SHORT).show();
            }else if(String.valueOf(newTaskNameTxt.getText()).isEmpty()) {
                Toast.makeText(view.getContext(), "Tytuł nie może być pusty", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(view.getContext(), "Correct", Toast.LENGTH_SHORT).show();
                dbHandler.addNewTask(String.valueOf(newTaskNameTxt.getText()),
                        String.valueOf(newTaskContentTxt.getText()),
                        toCheck,
                        String.valueOf(newTaskPriorityChoice.getText()));
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
        if (view.getId() == cancelBtn.getId()){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if(view.getId() == deleteBtn.getId()){
            dbHandler.deleteTask(String.valueOf(newTaskNameTxt.getText()));
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if(view.getId() == markAsComplete.getId()){
            dbHandler.completeTask(String.valueOf(newTaskNameTxt.getText()));
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if(view.getId() == editTaskBtn.getId()){
            String toCheck = String.valueOf(newTaskDeadlineTxt.getText());
            if(!toCheck.matches("^[\\d]{2}\\/[\\d]{2}\\/[\\d]{4}$")){
                Toast.makeText(view.getContext(), "Zły deadline podany", Toast.LENGTH_SHORT).show();
            }else if(String.valueOf(newTaskNameTxt.getText()).isEmpty()) {
                Toast.makeText(view.getContext(), "Tytuł nie może być pusty", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(view.getContext(), "Correct", Toast.LENGTH_SHORT).show();
                dbHandler.updateTask(taskName,
                        String.valueOf(newTaskNameTxt.getText()),
                        String.valueOf(newTaskContentTxt.getText()),
                        toCheck,
                        String.valueOf(newTaskPriorityChoice.getText()));
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
    }
}