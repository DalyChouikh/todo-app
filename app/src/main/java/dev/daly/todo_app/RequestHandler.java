package dev.daly.todo_app;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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



}
