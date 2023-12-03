package dev.daly.todo_app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.taskCheckBox);
        }
    }
}
