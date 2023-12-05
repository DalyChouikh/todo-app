package dev.daly.todo_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddTask";

    private EditText newTaskTitle;
    private Button newTaskSaveButton;

    RequestHandler requestHandler;

    public static AddTask newInstance() {
        return new AddTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        requestHandler = new RequestHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.new_task, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskTitle =  view.findViewById(R.id.taskTitleEditText);
        newTaskSaveButton = view.findViewById(R.id.addTaskButton);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("Bundle", "Bundle is not null");
            isUpdate = true;
            String title = bundle.getString("title");
            newTaskTitle.setText(title);
            if (!newTaskTitle.getText().toString().isEmpty()) {
                newTaskSaveButton.setTextColor(getResources().getColor(R.color.purple));
            } else {
                newTaskSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
            }
        }
        newTaskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newTaskSaveButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    newTaskSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
                } else {
                    newTaskSaveButton.setTextColor(getResources().getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(v -> {
            String username = getActivity().getIntent().getStringExtra("username");
            if (finalIsUpdate) {
                String oldTitle = bundle.getString("title");
                String newTitle = newTaskTitle.getText().toString();
                try {
                    requestHandler.updateTask(username, oldTitle, newTitle);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    String title = newTaskTitle.getText().toString();
                    requestHandler.addTask(username, title);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            dismiss();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Activity activity = getActivity();
        if (activity instanceof dev.daly.todo_app.DialogInterface) {
            ((dev.daly.todo_app.DialogInterface) activity).handleDialogClose(dialogInterface);
        }
    }

}
