package dev.daly.todo_app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import org.json.JSONException;

import java.util.List;

import dev.daly.todo_app.AddTask;
import dev.daly.todo_app.activities.HomeActivity;
import dev.daly.todo_app.R;
import dev.daly.todo_app.RequestHandler;
import dev.daly.todo_app.models.Status;
import dev.daly.todo_app.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private final HomeActivity homeActivity;

    private RequestHandler requestHandler;

    public TaskAdapter(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        requestHandler = new RequestHandler(homeActivity);
    }


    public List<Task> getTasks() {
        return tasks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = homeActivity.getLayoutInflater().inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.checkBox.setText(task.getTitle());
        holder.checkBox.setChecked(task.getStatus() == Status.DONE);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String username = homeActivity.getIntent().getStringExtra("username");
            try {
                requestHandler.updateTask(username, task.getTitle(), isChecked ? Status.DONE : Status.IN_PROGRESS);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) throws JSONException {
        Task task = tasks.get(position);
        String username = homeActivity.getIntent().getStringExtra("username");
        String title = task.getTitle();
        requestHandler.deleteTask(username, title);
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        Task task = tasks.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("title", task.getTitle());
        bundle.putString("username", homeActivity.getIntent().getStringExtra("username"));
        AddTask addTask = new AddTask();
        addTask.setArguments(bundle);
        addTask.show(homeActivity.getSupportFragmentManager(), addTask.getTag());
    }

    public Context getContext() {
        return homeActivity;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.taskCheckBox);
        }
    }





}
