package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<TaskModal> taskModalArrayList;
    private Context context;

    public CustomAdapter(ArrayList<TaskModal> taskModalArrayList, Context context){
        this.taskModalArrayList = taskModalArrayList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView taskTextView;
        private final Button taskButton;

        public ViewHolder(View view) {
            super(view);

            taskTextView = (TextView) view.findViewById(R.id.task_name);
            taskButton = (Button) itemView.findViewById(R.id.show_button);

            taskButton.setOnClickListener(this);
        }

        public TextView getTextView() {
            return taskTextView;
        }

        public Button getButton(){
            return taskButton;
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == taskButton.getId()){
                Toast.makeText(view.getContext(), taskTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

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
        TaskModal modal = taskModalArrayList.get(position);

        TextView textView = holder.taskTextView;
        textView.setText(modal.getTaskName());
        Button button = holder.taskButton;
        button.setText("Wyświetl");
    }


    @Override
    public int getItemCount() {
        return taskModalArrayList.size();
    }
}