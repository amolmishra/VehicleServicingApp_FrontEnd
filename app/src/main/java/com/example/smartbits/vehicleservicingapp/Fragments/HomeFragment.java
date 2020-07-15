package com.example.smartbits.vehicleservicingapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartbits.vehicleservicingapp.MainActivity;
import com.example.smartbits.vehicleservicingapp.R;
import com.example.smartbits.vehicleservicingapp.loginandregistration.AppController;
import com.example.smartbits.vehicleservicingapp.loginandregistration.CarsListManager;
import com.example.smartbits.vehicleservicingapp.loginandregistration.Config;
import com.example.smartbits.vehicleservicingapp.loginandregistration.LoginActivity;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner sp_veh_name;
    private Spinner sp_veh_model;
    private EditText et_reg_no;
    private Button btn_reg_veh;

    private SQLiteHandler db;

    private ProgressDialog pDialog;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        sp_veh_name = (Spinner) v.findViewById(R.id.vehicle_name);
        sp_veh_name.setOnItemSelectedListener(this);
        sp_veh_model = (Spinner) v.findViewById(R.id.veh_model);
        et_reg_no = (EditText) v.findViewById(R.id.et_reg_no);
        btn_reg_veh = (Button) v.findViewById(R.id.btn_reg_veh);

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

        //creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, carNames);

        //Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_veh_name.setAdapter(dataAdapter);

        // Submit button action
        btn_reg_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sp_veh_name.getSelectedItem().toString().trim();
                String model = sp_veh_model.getSelectedItem().toString().trim();
                String reg_no = et_reg_no.getText().toString().trim().replaceAll("\\s+","");
                if (!name.isEmpty() && !model.isEmpty() && !reg_no.isEmpty()) {
                    registerCar(name, model, reg_no);
                    Toast.makeText(getContext(), name + model + reg_no, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Please Enter Registration Number and Check Other Details", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Progress Dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getContext());

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
        String sp1 = String.valueOf(sp_veh_name.getSelectedItem());

            CarsListManager carsListManager = new CarsListManager();

            if (sp1.contentEquals("Chevrolet")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Chrevrolet.length; i++) {
                    list.add(carsListManager.Chrevrolet[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Ford")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Ford.length; i++) {
                    list.add(carsListManager.Ford[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Hyundai")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Hyundai.length; i++) {
                    list.add(carsListManager.Hyundai[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Mahindra")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Mahindra.length; i++) {
                    list.add(carsListManager.Mahindra[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("MarutiSuzuki")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.MarutiSuzuki.length; i++) {
                    list.add(carsListManager.MarutiSuzuki[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Skoda")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Skoda.length; i++) {
                    list.add(carsListManager.Skoda[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("TataMotors")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.TataMotors.length; i++) {
                    list.add(carsListManager.TataMotors[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Mitsubishi")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Mitsubishi.length; i++) {
                    list.add(carsListManager.Mitsubishi[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Nissan")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Nissan.length; i++) {
                    list.add(carsListManager.Nissan[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
            if (sp1.contentEquals("Fiat")) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < carsListManager.Fiat.length; i++) {
                    list.add(carsListManager.Fiat[i]);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                sp_veh_model.setAdapter(dataAdapter);
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void registerCar(final String name, final String model, final String reg_no) {
        String tag_string_req = "req_register_car";
        pDialog.setMessage("Registering...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_REG_CAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("HOMEFRAGMENT", "Register Response: " + response.toString());
                pDialog.hide();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        // Car successfully registered
                        // show it to the user
                        JSONObject car = jsonObject.getJSONObject("car");
                        int id = car.getInt("id");
                        String n = car.getString("name");
                        String m = car.getString("model");
                        String r = car.getString("regno");
                        Toast.makeText(getContext(), "Successfully registered the vehicle with you.", Toast.LENGTH_LONG).show();
                        Log.d("Carregistered", "Car is: " + n + m + r);

                        // add the registered car to sqlite
                        db.addCar(id, n, m, r);

                        // Clear the form
                    } else {
                        // error in car registration
                        String error_message = jsonObject.getString("error-msg");
                        Toast.makeText(getContext(), error_message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HOMEFRAGMENT", "VOLLEYERROR: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("model", model);
                params.put("regno", reg_no);
                // getting username
                String username = getUsername();
                params.put("username", username);
                return params;
            }
        };

        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public String getUsername() {
        HashMap<String, String> user = new SQLiteHandler(getContext()).getUserDetails();
        String username = user.get("username");
        return username;
    }
}
