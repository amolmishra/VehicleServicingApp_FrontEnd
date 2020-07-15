package com.example.smartbits.vehicleservicingapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Profile");

        ImageView background = (ImageView) findViewById(R.id.header_cover_image);
        ImageButton profile_pic = (ImageButton) findViewById(R.id.user_profile_photo);
        TextView name = (TextView) findViewById(R.id.user_profile_name);
        TextView email = (TextView) findViewById(R.id.user_profile_email);
        ImageView plus = (ImageView) findViewById(R.id.add_friend);
    }
}
