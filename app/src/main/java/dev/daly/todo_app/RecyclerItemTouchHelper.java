package dev.daly.todo_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import dev.daly.todo_app.adapter.TaskAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final TaskAdapter taskAdapter;

    public RecyclerItemTouchHelper(TaskAdapter taskAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.taskAdapter = taskAdapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position  = viewHolder.getAbsoluteAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(taskAdapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
                try {
                    taskAdapter.deleteItem(position);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                taskAdapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            taskAdapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Drawable icon;
        ColorDrawable background;
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(taskAdapter.getContext() ,R.drawable.baseline_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(taskAdapter.getContext(), R.color.purple));
        } else {
            icon = ContextCompat.getDrawable(taskAdapter.getContext(), R.drawable.baseline_delete_24);
            background = new ColorDrawable(ContextCompat.getColor(taskAdapter.getContext(), R.color.red));
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0){
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0,0,0,0);
        }
        background.draw(canvas);
        icon.draw(canvas);

    }

    public JsonObjectRequest deleteTask(String username, String title) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/" + username + "/tasks/" + title.replaceAll(" ", "%20");
        return new JsonObjectRequest(Request.Method.DELETE, url, null, response -> {
            Toast.makeText(taskAdapter.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
            System.out.println(response.toString());
        }, Throwable::printStackTrace);
    }
}
