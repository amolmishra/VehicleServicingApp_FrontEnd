package com.example.smartbits.vehicleservicingapp.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.smartbits.vehicleservicingapp.ConfirmBooking;
import com.example.smartbits.vehicleservicingapp.R;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SQLiteHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceBooking extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner sb_veh_name;
    private TextView sb_app_date;
    private TextView sb_app_time;
    private Spinner sb_service_center;
    private CheckBox sb_pickup_delivery;
    private String name, center, date, time, pickup;

    private SQLiteHandler db;


    public ServiceBooking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_booking, container, false);

        sb_veh_name = v.findViewById(R.id.vehicle_names);
        sb_veh_name.setOnItemSelectedListener(this);
        sb_service_center = v.findViewById(R.id.service_centers);
        sb_app_date = v.findViewById(R.id.app_date);
        sb_app_time = v.findViewById(R.id.app_time);
        Button sb_submit = v.findViewById(R.id.btn_serv_booking);
        sb_pickup_delivery = v.findViewById(R.id.pickup_delivery);

        // db object
        db = new SQLiteHandler(getContext());

        // get cars from sqlite
        List<String> carNames = getCarsNames();
        Log.d("ServiceBooking", "CARS: " + carNames.toString());

        if (carNames!= null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, carNames);
            //Drop down layout style
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            //setting data adapter to spinner
            sb_veh_name.setAdapter(dataAdapter);
        }

        sb_app_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        sb_app_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        sb_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = sb_veh_name.getSelectedItem().toString().trim();
                date = sb_app_date.getText().toString();
                time = sb_app_time.getText().toString();
                center = sb_service_center.getSelectedItem().toString().trim();
                if (sb_pickup_delivery.isChecked()) {
                    pickup = "true";
                } else {
                    pickup = "false";
                }
                if (name.trim().isEmpty() || center.trim().isEmpty() || date.trim().equalsIgnoreCase("Enter Date Here") || time.trim().equalsIgnoreCase("Enter Time Here") ) {
                    Toast.makeText(getContext(), "Please fill all the details.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Please Contact Us if Pickup address and registered address are different", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), ConfirmBooking.class);
                    intent.putExtra("veh_name", name);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("ser_center", center);
                    intent.putExtra("pickup", pickup);
                    startActivity(intent);
                }
            }
        });

        return v;

    }

    // shows datepicker
    public void showDatePickerDialog(View v) {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // open the dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    sb_app_date.setText(i2 + "-" + (i1 + 1) + "-" + i);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
    }

    // shows timepicker
    public void showTimePickerDialog(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // open the dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                sb_app_time.setText(i+":"+i1);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sp1 = String.valueOf(sb_veh_name.getSelectedItem());
        Log.d("Spinner1", sp1);
        List<String> serviceCenters = getServiceCenters(sp1);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, serviceCenters);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        dataAdapter.notifyDataSetChanged();
        sb_service_center.setAdapter(dataAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private ArrayList<String> getCarsNames() {
        ArrayList<String> cars = db.getCarDetails();
        return cars;
    }

    private ArrayList<String> getServiceCenters(String carName) {
        String[] split = carName.split("\\s+");
        String name = split[0];
        ArrayList<String> serviceCenters = db.getServiceCenterDetails(name);
        Log.d("SERVICECENTERS:", serviceCenters.toString());
        return serviceCenters;
    }
}
