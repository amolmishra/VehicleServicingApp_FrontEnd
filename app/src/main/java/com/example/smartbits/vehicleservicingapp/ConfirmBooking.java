package com.example.smartbits.vehicleservicingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartbits.vehicleservicingapp.loginandregistration.AppController;
import com.example.smartbits.vehicleservicingapp.loginandregistration.Config;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SQLiteHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity {

    private Button btn_confirm;
    private TextView tv_name, tv_date, tv_time, tv_center, tv_pickupd;
    private String name, center, date, time;
    private boolean pickup_delivery;
    private ProgressDialog progressDialog;

    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        setTitle("Confirm Booking");

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_name = (TextView) findViewById(R.id.cf_veh_name);
        tv_center = (TextView) findViewById(R.id.cf_service_center);
        tv_center.setMovementMethod(new ScrollingMovementMethod());
        tv_date = (TextView) findViewById(R.id.cf_app_date);
        tv_time = (TextView) findViewById(R.id.cf_app_time);
        tv_pickupd = (TextView) findViewById(R.id.cf_pick_delivery);

        progressDialog = new ProgressDialog(this);
        db = new SQLiteHandler(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("veh_name");
        center = bundle.getString("ser_center");
        date = bundle.getString("date");
        time = bundle.getString("time");
        if (bundle.getString("pickup").equals("true")) {
            pickup_delivery = true;
            tv_pickupd.setText("Yes");
        } else {
            pickup_delivery = false;
            tv_pickupd.setText("No");
        }

        tv_name.setText(name);
        tv_center.setText(center);
        tv_date.setText(date.toString());
        tv_time.setText(time.toString());


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookServicing();
            }
        });
    }

    private void bookServicing() {
        String tag_string_req = "req_register";

        progressDialog.setMessage("Booking...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_BOOK_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Booking", "BookResponse: " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        // booked successfully
                        String message = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        String username = db.getUserDetails().get("username");
                        String car[] = name.split("\\s+");
                        String carId = db.getCarId(car[0], car[1]);
                        String lines[] = center.split("[\\r\\n]");
                        int centerId = Integer.parseInt(db.getServiceCenterId(lines[1]));
                        String pickup = "";
                        if (pickup_delivery) {
                            pickup = "Y";
                        } else {
                            pickup = "N";
                        }
                        int charges = 0;

                        db.addHistory(username, carId, date, time, centerId, pickup, charges);
                        sendEmail(username, carId, date, time, centerId, pickup);

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error-msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                String username = db.getUserDetails().get("username");
                String car[] = name.split("\\s+");
                String carId = db.getCarId(car[0], car[1]);
                String lines[] = center.split("[\\r\\n]");
                String centerId = db.getServiceCenterId(lines[1]);
                params.put("username", username);
                params.put("registered_car_id", carId);
                params.put("date", date);
                params.put("time", time);
                params.put("centerid", centerId);
                if (pickup_delivery) {
                    params.put("pickup", "Y" );
                } else {
                    params.put("pickup", "N" );
                }
                Log.d("paramsConfirmBooking:", params.toString());
                return params;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    private void sendEmail(final String username, final String carId, final String date, final String time, final int centerId, final String pickup) {
        final String serviceCenter = db.getCenterById(centerId);
        final String carDetails = db.getCarById(carId);

            String tag_string_req = "req_send_confirmation";

            progressDialog.setMessage("Sending Confirmation...");
            showDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_SEND_CONFIRMATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Booking", "Confirmation Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            // booked successfully
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error-msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    Map<String, String> userDetails = db.getUserDetails();
                    params.put("name", userDetails.get("name"));
                    params.put("email", userDetails.get("email"));
                    params.put("registered_car_id", carDetails);
                    params.put("date", date);
                    params.put("time", time);
                    params.put("centerid", serviceCenter);
                    params.put("pickup", pickup);
                    params.put("charges", "Will be decided later");
                    Log.d("paramsConfirmBooking:", params.toString());
                    return params;
                }
            };

            AppController.getmInstance().addToRequestQueue(stringRequest, tag_string_req);

    }


    public void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}
