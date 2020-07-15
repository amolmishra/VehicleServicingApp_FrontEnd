package com.example.smartbits.vehicleservicingapp.loginandregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartbits.vehicleservicingapp.MainActivity;
import com.example.smartbits.vehicleservicingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private static SQLiteHandler db;
    private static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public static String getUsername() {
        if(!username.isEmpty()) {
            return username;
        }else {
            return db.getUserDetails().get("username");
        }
    }

    // function to verify registered_car details in mysql
    private void setCars() {
        // Tag used to cancel the request
        String tag_String_req = "req_cars";

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_FETCH_CARS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Cars Response: " + response);
                pDialog.hide();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Now store the cars in sQLite

                        JSONObject cars = jObj.getJSONObject("cars");
                        Iterator<String> keys = cars.keys();
                        while(keys.hasNext()) {
                            JSONObject car = cars.getJSONObject(keys.next());
                            int id = Integer.parseInt(car.getString("id"));
                            String name = car.getString("name");
                            String model = car.getString("model");
                            String regno = car.getString("regno");
                            // Inserting row in users table
                            db.addCar(id, name, model, regno);
                        }

                    } else {
                        // Error in login. Get error message
                        String errorMsg = jObj.getString("error-msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("username", getUsername());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(strReq, tag_String_req);
    }

    // Function to verify login details in mysql db
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_String_req = "req_login";

        pDialog.setMessage("Logging in...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in sQLite

                        JSONObject user = jObj.getJSONObject("user");
                        int id = Integer.parseInt(user.getString("id"));
                        String name = user.getString("name");
                        username = user.getString("username");
                        String email = user.getString("email");
                        String latitude = user.getString("lat");
                        String longitude = user.getString("lon");


                        // Inserting row in users table
                        db.addUser(id, name, username, email, latitude, longitude);
                        setCars();
                        setHistory();
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams(){
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("username", email);
                params.put("password", password);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(strReq, tag_String_req);
    }

    private void setHistory() {
        // TAG used to cancel the request
        String tag_string_req = "req_history";
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_FETCH_APPOINTMENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        // service center list is downloaded
                        // Store the list in sqlite
                        JSONObject centers = jsonObject.getJSONObject("appointments");
                        for (int i = 1; i<=centers.length(); i++) {
                            int charges, centerid;
                            String  carid, date, time, pickup;
                            JSONObject serviceCenter = centers.getJSONObject(""+i);
                            carid = serviceCenter.getString("registered_car_id");
                            date = serviceCenter.getString("date");
                            time = serviceCenter.getString("time");
                            centerid = serviceCenter.getInt("centerid");
                            pickup = serviceCenter.getString("pickup");
                            charges = serviceCenter.getInt("charges");

                            if (pickup.equalsIgnoreCase("y")) {
                                pickup = "Yes";
                            } else {
                                pickup = "No";
                            }

                            db.addHistory(username, carid, date, time, centerid, pickup, charges);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(pDialog.isShowing()) {
                        pDialog.hide();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error:", "Appointments Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                if(pDialog.isShowing()) {
                    pDialog.hide();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", LoginActivity.getUsername());
                return params;
            }
        };

        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
