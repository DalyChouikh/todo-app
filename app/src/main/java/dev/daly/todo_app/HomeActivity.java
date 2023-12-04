package dev.daly.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dev.daly.todo_app.adapter.TaskAdapter;
import dev.daly.todo_app.databinding.ActivityHomeBinding;
import dev.daly.todo_app.models.Task;

public class HomeActivity extends AppCompatActivity implements DialogInterface {

    private ActivityHomeBinding binding;
    public RequestHandler requestHandler;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> tasks;
    private FloatingActionButton addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tasks = new ArrayList<>();
        addTaskButton = binding.addTaskButton;
        requestHandler = new RequestHandler(this);
        recyclerView = binding.tasksRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(binding.tasksRecyclerView);
        recyclerView.setAdapter(taskAdapter);
        try {
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            requestHandler.getUserTasks(username, tasks -> {
                System.out.println(tasks.toString());
                taskAdapter.setTasks(tasks);
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        addTaskButton.setOnClickListener(v -> {
            AddTask.newInstance().show(getSupportFragmentManager(), AddTask.TAG);
        });

    }

    @Override
    public void handleDialogClose(android.content.DialogInterface dialogInterface) {
        try {
            Thread.sleep(1000);
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            requestHandler.getUserTasks(username, tasks -> {
                System.out.println(tasks.toString());
                taskAdapter.setTasks(tasks);
                taskAdapter.notifyDataSetChanged();
            });
        } catch (JSONException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}