package dev.daly.todo_app.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.checkbox.MaterialCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;

import dev.daly.todo_app.AddTask;
import dev.daly.todo_app.HomeActivity;
import dev.daly.todo_app.R;
import dev.daly.todo_app.models.Status;
import dev.daly.todo_app.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private final HomeActivity homeActivity;

    public TaskAdapter(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
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
                Volley.newRequestQueue(homeActivity).add(updateTask(username, task.getTitle(), isChecked ? Status.DONE : Status.IN_PROGRESS));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) throws JSONException {
        Task task = tasks.get(position);
        String username = homeActivity.getIntent().getStringExtra("username");
        String title = task.getTitle();
        Volley.newRequestQueue(homeActivity).add(deleteTask(username, title));
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
        addTask.show(homeActivity.getSupportFragmentManager(), AddTask.TAG);
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


    public JsonObjectRequest updateTask(String username, String title, Status status) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/" + username + "/tasks/" + title.replaceAll(" ", "%20");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("status", status.getStatus());
        return new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response -> {
            Toast.makeText(homeActivity, response.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
    }

    public JsonObjectRequest deleteTask(String username, String title) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/" + username + "/tasks/" + title.replaceAll(" ", "%20");
        return new JsonObjectRequest(Request.Method.DELETE, url, null, response -> {
            Toast.makeText(homeActivity, response.toString(), Toast.LENGTH_SHORT).show();
            System.out.println(response.toString());
        }, Throwable::printStackTrace);
    }
}
