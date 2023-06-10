package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> { 

    public CustomAdapter(ArrayList<Task> tasks){
        mTasks = tasks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTextView;
        private final Button taskButton;

        public ViewHolder(View view) {
            super(view);

            taskTextView = (TextView) view.findViewById(R.id.task_name);
            taskButton = (Button) itemView.findViewById(R.id.show_button);
        }

        public TextView getTextView() {
            return taskTextView;
        }

        public Button getButton(){
            return taskButton;
        }


    }

    private List<Task> mTasks;


    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.text_row_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        Task task = mTasks.get(position);

        TextView textView = holder.taskTextView;
        textView.setText(task.getTaskTitle());
        Button button = holder.taskButton;
        button.setText("Wyświetl");
    }


    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}