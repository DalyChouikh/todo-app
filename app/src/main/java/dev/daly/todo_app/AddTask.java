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

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskTitle;
    private Button newTaskSaveButton;

    public static AddTask newInstance() {
        return new AddTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.new_task, viewGroup, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskTitle = getView().findViewById(R.id.taskTitleEditText);
        newTaskSaveButton = getView().findViewById(R.id.addTaskButton);

        boolean isUpdate;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("Bundle", "Bundle is not null");
            isUpdate = true;
            if (!newTaskTitle.getText().toString().isEmpty()) {
                newTaskSaveButton.setTextColor(getResources().getColor(R.color.purple));
            } else {
                newTaskSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
            }

        } else {
            isUpdate = false;
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
        newTaskSaveButton.setOnClickListener(v -> {
            String title = bundle.getString("title");
            String username = getActivity().getIntent().getStringExtra("username");
            if(isUpdate){
                try {
                    Volley.newRequestQueue(getContext()).add(updateTask(username, title));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                dismiss();
                return;
            }
            try {
                Volley.newRequestQueue(getContext()).add(addTask(username, title));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            dismiss();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Activity activity = getActivity();
        if (activity instanceof dev.daly.todo_app.DialogInterface) {
            ((dev.daly.todo_app.DialogInterface) activity).handleDialogClose(dialogInterface);
        }
    }

    public JsonObjectRequest addTask(String username, String title) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/" + username + "/tasks";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        return new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
    }
    public JsonObjectRequest updateTask(String username, String title) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/" + username + "/tasks/" + title.replaceAll(" ", "%20");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        return new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response -> {
            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
    }


}
