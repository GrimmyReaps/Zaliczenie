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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CreateEditTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button createNewTaskBtn;
    Button cancelBtn;
    Button editTaskBtn;
    Button editCancelBtn;
    TextView newTaskNameTxt;
    EditText newTaskDeadlineTxt;
    TextInputEditText newTaskContentTxt;
    TextView newTaskPriorityChoice;
    Spinner priorityChoice;
    String[] priorities = {"HIGH", "MEDIUM", "LOW"};
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

        newTaskNameTxt = (TextView) findViewById(R.id.newTaskName);
        newTaskDeadlineTxt = (EditText) findViewById(R.id.newDeadline);
        newTaskContentTxt = (TextInputEditText) findViewById(R.id.newTaskContent);
        newTaskDeadlineTxt.addTextChangedListener(tw);

        createNewTaskBtn = (Button) findViewById(R.id.createNewTask);
        cancelBtn = (Button) findViewById(R.id.cancel);
        editTaskBtn = (Button) findViewById(R.id.editTask);
        editCancelBtn = (Button) findViewById(R.id.cancelEdit);

        priorityChoice = (Spinner) findViewById(R.id.choosePriority);
        priorityChoice.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityChoice.setAdapter(aa);

        newTaskPriorityChoice = (TextView) findViewById(R.id.priorityDisplay);

        Intent caller = getIntent();
        Bundle sendInfo = caller.getExtras();
        boolean isAdding = sendInfo.getBoolean("isAdding");
        if (isAdding){
            editTaskBtn.setVisibility(View.GONE);
            editCancelBtn.setVisibility(View.GONE);
        }else{
            createNewTaskBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
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
}