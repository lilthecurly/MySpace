package com.lilthecurly.myspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GaleryActivity extends AppCompatActivity {
    public static String BaseURL = "https://api.nasa.gov/";
    public static String key = "hb2Mdd736sqIYfMF2mHA7a6Dp4o9AhDccuMUkrBK";
    public static String camera = "navcam";
    public String earth_date;
    public String url;
    public int iteration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);
        Button next = findViewById(R.id.next);
        Button download = findViewById(R.id.download);
        ImageView imageView = findViewById(R.id.imageView);
        Bundle arguments = getIntent().getExtras();
        earth_date = arguments.getString("keys");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RoverService service = retrofit.create(RoverService.class);
        Call<MarsRover> call = service.getRoverData(earth_date, camera, key);
        call.enqueue(new Callback<MarsRover>() {
            @Override
            public void onResponse(Call<MarsRover> call, Response<MarsRover> response) {
                if(response.isSuccessful()){
                    MarsRover marsRover = response.body();
                    int size = marsRover.photos.size();
                    url = marsRover.photos.get(0).img_src;
                    Picasso.with(getApplicationContext()).load(url).into(imageView);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(iteration < size) {
                                url = marsRover.photos.get(iteration).img_src;
                                Picasso.with(getApplicationContext()).load(url).into(imageView);
                                iteration = iteration + 1;
                            }
                        }
                    });
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            String title = URLUtil.guessFileName(url, null, null);
                            request.setTitle(title);
                            request.setDescription("Downloading...");
                            String cookie = CookieManager.getInstance().getCookie(url);
                            request.addRequestHeader("cookie", cookie);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                            DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                            downloadManager.enqueue(request);

                            Toast.makeText(GaleryActivity.this, "Downloading started", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<MarsRover> call, Throwable t) {
            }
        });


    }

}