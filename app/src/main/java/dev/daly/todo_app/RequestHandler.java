package dev.daly.todo_app;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dev.daly.todo_app.models.Status;
import dev.daly.todo_app.models.Task;

public class RequestHandler {

    private final String ADDRESS = "192.168.56.122";
    private final String URL = "http://" + ADDRESS + ":8082/api/v1/users";

    private Context context;

    private RequestQueue requestQueue;

    public RequestHandler(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public RequestHandler() {
    }

    public void createUser(String username, String password) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, json, response -> {
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }


    public void getUserTasks(String username, TaskCallback taskCallback) throws JSONException {
        List<Task> tasks = new ArrayList<>();
        String url = URL + "/" + username + "/tasks";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            Log.d("Response", response.toString());
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String status = jsonObject.getString("status");
                    tasks.add(new Task(title, status));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            taskCallback.onCallback(tasks);
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);
    }

    public void updateTask(String username, String oldTitle, String newTitle) throws JSONException {
        String url = URL + "/" + username + "/tasks/" + oldTitle.replaceAll(" ", "%20");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", newTitle);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response -> {
            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }

    public void updateTask(String username, String title, Status status) throws JSONException {
        String url = URL + "/" + username + "/tasks/" + title.replaceAll(" ", "%20");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("status", status.getStatus());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response -> {
            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }

    public void addTask(String username, String title) throws JSONException {
        String url = URL + "/" + username + "/tasks";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }

    public void deleteTask(String username, String title) throws JSONException {
        String url = URL + "/" + username + "/tasks/" + title.replaceAll(" ", "%20");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, response -> {
            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            System.out.println(response.toString());
        }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }

    public interface TaskCallback {
        void onCallback(List<Task> tasks);
    }

}
