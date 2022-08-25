package com.lilthecurly.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button apod;
    private Button MarsRover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apod = findViewById(R.id.apod);
        MarsRover = findViewById(R.id.MarsRover);

        apod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apodopen();
            }
        });


        MarsRover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               roveropen();
            }
        });

    }

    public void roveropen() {
        Intent intent = new Intent(this, roverActivity.class);
        startActivity(intent);
    }

    public void apodopen() {
        Intent intent = new Intent(this, apod_Activity.class);
        startActivity(intent);
    }
}