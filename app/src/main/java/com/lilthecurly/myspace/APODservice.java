package com.lilthecurly.myspace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APODservice {
    @GET("planetary/apod?")
    Call<APOD> getAPOD(@Query("date") String datetoday,
                       @Query("api_key") String key);
}
