package dev.daly.todo_app.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import dev.daly.todo_app.AddTask;
import dev.daly.todo_app.DialogInterface;
import dev.daly.todo_app.R;
import dev.daly.todo_app.RecyclerItemTouchHelper;
import dev.daly.todo_app.RequestHandler;
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
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*----------------Setting content view----------------*/
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*----------------Initializing variables----------------*/
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.toolbar.getRoot();
        tasks = new ArrayList<>();
        addTaskButton = binding.addTaskButton;
        requestHandler = new RequestHandler(this);
        recyclerView = binding.tasksRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(binding.tasksRecyclerView);
        recyclerView.setAdapter(taskAdapter);

        /*----------------Fetching tasks from server----------------*/
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

        /*----------------Setting up navigation drawer----------------*/
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        int navLogout = R.id.navLogout;
        int navSettings = R.id.navSettings;
        int navProfile = R.id.navProfile;
        int navContactUs = R.id.navContactUs;
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == navLogout){
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            if(menuItem.getItemId() == navSettings){
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
            if(menuItem.getItemId() == navProfile){
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
                finish();
            }
            if(menuItem.getItemId() == navContactUs){
                Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        });


    }

    @Override
    public void handleDialogClose(android.content.DialogInterface dialogInterface) {
        try {
            Thread.sleep(500);
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(navigationView);
            return;
        }else
            super.onBackPressed();
    }
}