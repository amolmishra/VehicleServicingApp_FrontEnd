package com.example.smartbits.vehicleservicingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartbits.vehicleservicingapp.loginandregistration.AppController;
import com.example.smartbits.vehicleservicingapp.loginandregistration.Config;
import com.example.smartbits.vehicleservicingapp.loginandregistration.LoginActivity;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SQLiteHandler;

import org.json.JSONObject;

import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SQLite handler
        db = new SQLiteHandler(getApplicationContext());

        fetchServiceCenteres();

        startNextActivity();


    }

    private void fetchServiceCenteres() {
        // TAG used to cancel the request
        String tag_string_req = "req_centeres";

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_SERVICE_CENTERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        // service center list is downloaded
                        // Store the list in sqlite
                        JSONObject centers = jsonObject.getJSONObject("centers");
                        for (int i = 1; i<=centers.length(); i++) {
                            int id;
                            String name, address, phone, company, email;
                            JSONObject serviceCenter = centers.getJSONObject(""+i);
                            id = serviceCenter.getInt("id");
                            name = serviceCenter.getString("name");
                            address = serviceCenter.getString("address");
                            phone = serviceCenter.getString("phone");
                            company = serviceCenter.getString("company");
                            email = serviceCenter.getString("email");
                            String latitude = serviceCenter.getString("lat");
                            String longitude = serviceCenter.getString("lon");
                            db.addServiceCenters(id, name, address, phone, company, email, latitude, longitude);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }


    // Starting next activity
    private void startNextActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
