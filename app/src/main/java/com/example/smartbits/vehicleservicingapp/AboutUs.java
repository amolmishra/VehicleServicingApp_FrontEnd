package com.example.smartbits.vehicleservicingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    public int My_Permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setTitle("Contact Us");

        TextView tv_email = findViewById(R.id.cu_email);
        TextView tv_support = findViewById(R.id.cu_support);
        TextView tv_phone = findViewById(R.id.cu_phone);
        Button bt_goback = findViewById(R.id.cu_goback);

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"manisa.das@wipro.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Informational");
                startActivity(Intent.createChooser(intent, ""));

            }
        });

        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"manisa.das@wipro.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                startActivity(Intent.createChooser(intent, ""));

            }
        });

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+917008794214"));

                if (ActivityCompat.checkSelfPermission(AboutUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AboutUs.this, new String[] {Manifest.permission.CALL_PHONE}, My_Permission);
                }

                startActivity(intent);

            }
        });

        bt_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
