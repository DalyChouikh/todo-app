package dev.daly.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import dev.daly.todo_app.adapter.TaskAdapter;
import dev.daly.todo_app.databinding.ActivityHomeBinding;
import dev.daly.todo_app.models.Task;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private RequestHandler requestHandler;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tasks = new ArrayList<>();
        requestHandler = new RequestHandler();
        recyclerView = binding.tasksRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        Task task = new Task("Task 1", "DONE");
        tasks.add(task);
        tasks.add(new Task("Task 2", "In progress"));
        tasks.add(new Task("Task 3", "In progress"));
        tasks.add(new Task("Task 4", "In progress"));
        tasks.add(new Task("Task 5", "In progress"));

        taskAdapter.setTasks(tasks);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Volley.newRequestQueue(HomeActivity.this).add(requestHandler.getUserTasks(this,"daly"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}