package com.lilthecurly.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apod_Activity extends AppCompatActivity {
    public static String BaseURL = "https://api.nasa.gov/";
    public static String key = "hb2Mdd736sqIYfMF2mHA7a6Dp4o9AhDccuMUkrBK";
    public TextView copyright, explanation, explanation2;
    public ImageView picture;
    public String datetoday;
    public String type, picurl, videoulr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        copyright = findViewById(R.id.copyright);
        explanation = findViewById(R.id.explanation);
        explanation2 = findViewById(R.id.explanation2);
        picture = findViewById(R.id.picture);
        Button openVideo = findViewById(R.id.openVideo);

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        datetoday = dateFormat.format(date);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APODservice service = retrofit.create(APODservice.class);
        Call<APOD> call = service.getAPOD(datetoday, key);
        call.enqueue(new Callback<APOD>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<APOD> call, Response<APOD> response) {
                APOD apod = response.body();
                if(apod.copyright != null){
                    copyright.setText("©" + apod.copyright);
                }
                type = apod.media_type;
                if(type.equals("image")){
                    picurl = apod.url;
                    Picasso.with(getApplicationContext()).load(picurl).into(picture);
                    explanation.setText(apod.explanation);
                } else if(type.equals("video")){
                    openVideo.setVisibility(View.VISIBLE);
                    videoulr = apod.url;
                    openVideo.setText("Tap to open video");
                    openVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(videoulr));
                            startActivity(i);
                        }
                    });
                    explanation2.setText(apod.explanation);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<APOD> call, Throwable t) {
                explanation.setText("Fail!Try to restart the app");
            }
        });
    }
}