package com.lilthecurly.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class roverActivity extends AppCompatActivity {
    public static String BaseURL = "https://api.nasa.gov/";
    public static String key = "hb2Mdd736sqIYfMF2mHA7a6Dp4o9AhDccuMUkrBK";
    public static String camera = "navcam";
    private DatePickerDialog datePickerDialog;
    private Button button;
    private Button select;
    private TextView fault;
    public String earth_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover);
        button = findViewById(R.id.button2);
        select = findViewById(R.id.select);
        button.setText(getTodaysDate());
        fault = findViewById(R.id.fault);
        initDatePicker();

        select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                galeryopen();
            }
        });
    }

    private String getTodaysDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month = month + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day,month,year);
                button.setText(date);
                earth_date = year + "-" + month + "-" + day;
            }
        };

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH, 01);
        cal.set(Calendar.DAY_OF_MONTH, 01);

        Date MinDate = cal.getTime();

        datePickerDialog.getDatePicker().setMinDate(MinDate.getTime());

        int addmonth = month + 1;

        earth_date = year + "-" + addmonth + "-" + day;

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "Jan";
    }

    public void OpenDatePicker(View view){
        datePickerDialog.show();
    }


    public void galeryopen(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RoverService service = retrofit.create(RoverService.class);
        Call<MarsRover> call = service.getRoverData(earth_date, camera, key);
        call.enqueue(new Callback<MarsRover>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MarsRover> call, Response<MarsRover> response) {
                if(response.isSuccessful()){
                    MarsRover marsRover = response.body();
                    int size = marsRover.photos.size();
                    if(size > 0){
                        Intent intent = new Intent(getApplicationContext(), GaleryActivity.class);
                        intent.putExtra("keys", earth_date);
                        startActivity(intent);
                    } else {
                        fault.setText("No photo from this day, try another date");
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<MarsRover> call, Throwable t) {
                fault.setText("Fault!!");
            }
        });
    }
}