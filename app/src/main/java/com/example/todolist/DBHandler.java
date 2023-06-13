package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "taskdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "mytasks";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our task name column
    private static final String NAME_COL = "taskName";

    // below variable for our task description column.
    private static final String CONTENT_COL = "taskContent";
    private static final String DATE_COL = "dateOfCreation";
    private static final String FINISHDATE_COL = "dateOfCompletion";
    private static final String ISCOMPLETE_COL = "isTaskFinished";
    private static final String PRIORITY_COL = "priority";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT, "
                + CONTENT_COL + " TEXT, "
                + DATE_COL + " TEXT, "
                + FINISHDATE_COL + " TEXT, "
                + ISCOMPLETE_COL + " TEXT, "
                + PRIORITY_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new task to our sqlite database.
    public void addNewTask(String taskName, String taskContent, String finishDate, String priority) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(new Date());
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, taskName);
        values.put(CONTENT_COL, taskContent);
        values.put(DATE_COL, strDate);
        values.put(FINISHDATE_COL, finishDate);
        values.put(ISCOMPLETE_COL, "FALSE");
        values.put(PRIORITY_COL, priority);
        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public void deleteTask(String taskName){
        String[] taskNameArr = {taskName};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, NAME_COL + "=?", taskNameArr);
    }

    public void completeTask(String taskName){
        ContentValues cv = new ContentValues();
        cv.put(ISCOMPLETE_COL, "TRUE");
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, cv, NAME_COL + "= ?", new String[]{taskName});
    }

    public void updateTask(String oldTaskName, String newTaskName, String taskContent, String finishDate, String priority){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME_COL, newTaskName);
        cv.put(CONTENT_COL, taskContent);
        cv.put(FINISHDATE_COL, finishDate);
        cv.put(PRIORITY_COL, priority);
        db.update(TABLE_NAME, cv, NAME_COL + "= ?", new String[]{oldTaskName});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<TaskModal> readTasks()
    {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<TaskModal> taskModalArrayList = new ArrayList<>();
        boolean completionState;
        // moving our cursor to first position.
        if (cursorTasks.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                if(cursorTasks.getString(5).equals("TRUE")){
                    completionState = true;
                }else{
                    completionState = false;
                }
                taskModalArrayList.add(new TaskModal(cursorTasks.getString(1),
                                                    cursorTasks.getString(2),
                                                    cursorTasks.getString(3),
                                                    cursorTasks.getString(4),
                                                    completionState,
                                                    cursorTasks.getString(6)));
            } while (cursorTasks.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTasks.close();
        return taskModalArrayList;
    }

    public ArrayList<TaskModal> sortTasksByName()
    {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + NAME_COL + " ASC", null);

        // on below line we are creating a new array list.
        ArrayList<TaskModal> taskModalArrayList = new ArrayList<>();
        boolean completionState;
        // moving our cursor to first position.
        if (cursorTasks.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                if(cursorTasks.getString(5).equals("TRUE")){
                    completionState = true;
                }else{
                    completionState = false;
                }
                taskModalArrayList.add(new TaskModal(cursorTasks.getString(1),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getString(4),
                        completionState,
                        cursorTasks.getString(6)));
            } while (cursorTasks.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTasks.close();
        return taskModalArrayList;
    }

    public ArrayList<TaskModal> sortTasksByDate()
    {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + DATE_COL + " ASC", null);

        // on below line we are creating a new array list.
        ArrayList<TaskModal> taskModalArrayList = new ArrayList<>();
        boolean completionState;
        // moving our cursor to first position.
        if (cursorTasks.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                if(cursorTasks.getString(5).equals("TRUE")){
                    completionState = true;
                }else{
                    completionState = false;
                }
                taskModalArrayList.add(new TaskModal(cursorTasks.getString(1),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getString(4),
                        completionState,
                        cursorTasks.getString(6)));
            } while (cursorTasks.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTasks.close();
        return taskModalArrayList;
    }

    public ArrayList<TaskModal> sortTasksByDeadline()
    {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.

        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + FINISHDATE_COL + " ASC", null);

        // on below line we are creating a new array list.
        ArrayList<TaskModal> taskModalArrayList = new ArrayList<>();
        boolean completionState;
        // moving our cursor to first position.
        if (cursorTasks.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                if(cursorTasks.getString(5).equals("TRUE")){
                    completionState = true;
                }else{
                    completionState = false;
                }
                taskModalArrayList.add(new TaskModal(cursorTasks.getString(1),
                        cursorTasks.getString(2),
                        cursorTasks.getString(3),
                        cursorTasks.getString(4),
                        completionState,
                        cursorTasks.getString(6)));
            } while (cursorTasks.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTasks.close();
        return taskModalArrayList;
    }

    public ArrayList<TaskModal> fetchRow(String taskName){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] taskNameArr = {taskName};
        Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + NAME_COL + "= ?", taskNameArr);
        ArrayList<TaskModal> taskModalArrayList = new ArrayList<>();
        boolean completionState;

        if(cursorTasks.moveToFirst()){
            if(cursorTasks.getString(5).equals("TRUE")){
                completionState = true;
            }else {
                completionState = false;
            }
            taskModalArrayList.add(new TaskModal(cursorTasks.getString(1),
                    cursorTasks.getString(2),
                    cursorTasks.getString(3),
                    cursorTasks.getString(4),
                    completionState,
                    cursorTasks.getString(6)));
        }
        cursorTasks.close();
        return taskModalArrayList;
    }
}