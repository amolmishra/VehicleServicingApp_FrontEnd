package com.example.smartbits.vehicleservicingapp.Fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
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
import com.example.smartbits.vehicleservicingapp.LocationTrack;
import com.example.smartbits.vehicleservicingapp.MainActivity;
import com.example.smartbits.vehicleservicingapp.R;
import com.example.smartbits.vehicleservicingapp.loginandregistration.AppController;
import com.example.smartbits.vehicleservicingapp.loginandregistration.Config;
import com.example.smartbits.vehicleservicingapp.loginandregistration.LoginActivity;
import com.example.smartbits.vehicleservicingapp.loginandregistration.RegisterActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ServiceCenterRegistration extends Fragment {
    private Spinner sp_veh_name;
    private ProgressDialog pDialog;
    private EditText et_latitude;
    private EditText et_longitude;
    private String latitude;
    private String longitude;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

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
        et_latitude = v.findViewById(R.id.et_latitude);
        et_longitude = v.findViewById(R.id.et_longitude);
        final Button bt_locateMe = v.findViewById(R.id.btn_locate);

        et_longitude.setEnabled(false);
        et_longitude.setFocusable(false);
        et_latitude.setEnabled(false);
        et_latitude.setFocusable(false);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        bt_locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationTrack = new LocationTrack(getContext());

                if (locationTrack.canGetLocation()) {
                    double lon = locationTrack.getLongitude();
                    double lat = locationTrack.getLatitude();

                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(lon);

                    et_latitude.setText(latitude);
                    et_longitude.setText(longitude);

                } else {
                    locationTrack.showSettingsAlert();
                }
            }
        });

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
                params.put("lat", latitude);
                params.put("lon", longitude);
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


    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}
