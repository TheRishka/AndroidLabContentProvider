package com.therishka.androidlabweather.network;

import android.support.annotation.NonNull;

import com.therishka.androidlabweather.models.City;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Rishad Mustafaev
 */
public interface WeatherService {

    @GET("data/2.5/weather?units=metric")
    Call<City> getWeather(@NonNull @Query("q") String query);

}