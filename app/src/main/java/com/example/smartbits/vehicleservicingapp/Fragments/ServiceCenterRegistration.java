package com.example.smartbits.vehicleservicingapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartbits.vehicleservicingapp.MainActivity;
import com.example.smartbits.vehicleservicingapp.R;
import com.example.smartbits.vehicleservicingapp.loginandregistration.AppController;
import com.example.smartbits.vehicleservicingapp.loginandregistration.Config;
import com.example.smartbits.vehicleservicingapp.loginandregistration.LoginActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiceCenterRegistration extends Fragment {
    private Spinner sp_veh_name;
    private ProgressDialog pDialog;

    public ServiceCenterRegistration() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_center_registration, container, false);

        Button submitButton = v.findViewById(R.id.submit_button);
        final EditText editName = v.findViewById(R.id.editTextName);
        final EditText editEmail = v.findViewById(R.id.editTextEmail);
        final EditText editPhone = v.findViewById(R.id.editTextPhone);
        final EditText editAddress = v.findViewById(R.id.editTextAddress);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String phone = editPhone.getText().toString();
                String address = editAddress.getText().toString();
                String company = sp_veh_name.getSelectedItem().toString();

                addServiceCenter(name, email, phone, address, company);
            }
        });

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        List<String> carNames = new ArrayList<>();
        carNames.add("Chevrolet");
        carNames.add("Fiat");
        carNames.add("Ford");
        carNames.add("Hyundai");
        carNames.add("Mahindra");
        carNames.add("MarutiSuzuki");
        carNames.add("Skoda");
        carNames.add("TataMotors");
        carNames.add("Mitsubishi");
        carNames.add("Nissan");

        sp_veh_name = v.findViewById(R.id.spinner_company);
        //creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, carNames);

        //Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_veh_name.setAdapter(dataAdapter);
        return v;
    }

    private void addServiceCenter(final String name, final String email, final String phone, final String address, final String company) {
        String tag_string_req = "req_register_service_center";

        pDialog.setMessage("Booking...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_REGISTER_SERVICE_CENTERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Registering", "Registering response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        // booked successfully
                        String message = jsonObject.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("error-msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("address", address);
                params.put("company", company);
                Log.d("paramsConfirmRegister:", params.toString());
                return params;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    public void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
    }
}
