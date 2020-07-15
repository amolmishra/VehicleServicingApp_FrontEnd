package com.example.smartbits.vehicleservicingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartbits.vehicleservicingapp.Fragments.CostCalculation;
import com.example.smartbits.vehicleservicingapp.Fragments.HomeFragment;
import com.example.smartbits.vehicleservicingapp.Fragments.ServiceBooking;
import com.example.smartbits.vehicleservicingapp.Fragments.ServiceCenterRegistration;
import com.example.smartbits.vehicleservicingapp.Fragments.ServiceHistory;
import com.example.smartbits.vehicleservicingapp.loginandregistration.LoginActivity;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SQLiteHandler;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvname;
    private TextView tvemail;
    private TextView tvusername;


    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        tvemail = header.findViewById(R.id.tvemail);
        tvname = header.findViewById(R.id.tvname);
        tvusername = header.findViewById(R.id.tvusername);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // load nav menu headers data
        loadNavHeader();

        //Load the base fragment
        Fragment fragment;
        FragmentTransaction ft;
        getSupportActionBar().setTitle("Register Vehicle");
        fragment = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        FragmentTransaction ft;

        if (id == R.id.nav_reg_service_center) {
            getSupportActionBar().setTitle("Register your Service Center");
            fragment = new ServiceCenterRegistration();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }else if (id == R.id.nav_reg_vehicle) {
            getSupportActionBar().setTitle("Register Vehicle");
            fragment = new HomeFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        } else if (id == R.id.nav_service_booking) {
            getSupportActionBar().setTitle("Service Booking");
            fragment = new ServiceBooking();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();

        } else if (id == R.id.nav_service_history) {
            getSupportActionBar().setTitle("Service History");
            fragment = new ServiceHistory();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        } else if (id == R.id.nav_cost_calc) {
            getSupportActionBar().setTitle("Cost Calculation");
            fragment = new CostCalculation();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), AboutUs.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteCars();
        db.deleteServiceCenters();

        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void loadNavHeader() {
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String username = user.get("username");

        // Setting values to the textviews
        if (tvname != null && tvname != null && tvusername != null) {
            tvemail.setText(email);
            tvname.setText(name);
            tvusername.setText(username);
        } else {
            Toast.makeText(getApplicationContext(), "Text View Object is null:: " + name + email + username, Toast.LENGTH_LONG).show();
        }
    }

}
