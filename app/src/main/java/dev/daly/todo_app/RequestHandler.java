package dev.daly.todo_app;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {

    JSONObject json;

    public JsonObjectRequest createUser(String username, String password) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/register";
        json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        return new JsonObjectRequest(Request.Method.POST, url, json, response -> {
            Log.d("Response", response.toString());
        }, Throwable::printStackTrace);
    }

    public JsonArrayRequest getUserTasks(Context context, String username) throws JSONException {
        String url = "http://192.168.1.17:8082/api/v1/users/" + username + "/tasks";
        return new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            Log.d("Response", response.toString());
            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
        }, Throwable::printStackTrace);
    }



}
