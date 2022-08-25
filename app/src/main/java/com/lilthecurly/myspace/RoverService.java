package com.lilthecurly.myspace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RoverService {
    @GET("mars-photos/api/v1/rovers/curiosity/photos?")
    Call<MarsRover> getRoverData(@Query("earth_date") String earth_date,
                                 @Query("camera") String camera,
                                 @Query("api_key") String key);
}
