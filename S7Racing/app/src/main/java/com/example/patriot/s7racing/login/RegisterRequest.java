package com.example.patriot.s7racing.login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String register_request_url = "http://proj-309-gp-b-4.cs.iastate.edu/php/register.php"; // We need to put server URL here
    private Map<String, String> params;

    public RegisterRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, register_request_url, listener, null); // We have no error listener so the 4th arg will be null

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

