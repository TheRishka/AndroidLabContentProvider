package com.therishka.androidlabweather.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.therishka.androidlabweather.models.City;
import com.therishka.androidlabweather.tables.CityContract;
import com.therishka.androidlabweather.tables.RequestContract;
import com.therishka.androidlabweather.tables.Where;

import java.io.IOException;

/**
 * @author Rishad Mustafaev
 */
public class RequestsService extends IntentService {

    private static final String REQUEST_KEY = "request";
    private static final String CITY_NAME_KEY = "city_name";

    public static void start(@NonNull Context context, @NonNull Request request, @NonNull String cityName) {
        Intent intent = new Intent(context, RequestsService.class);
        intent.putExtra(REQUEST_KEY, new Gson().toJson(request));
        intent.putExtra(CITY_NAME_KEY, cityName);
        context.startService(intent);
    }

    public RequestsService() {
        super(RequestsService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Request request = new Gson().fromJson(intent.getStringExtra(REQUEST_KEY), Request.class);
        Where mWhere = Where.create().equalTo(
                RequestContract.RequestContractEntry.COLUMN_REQUEST,
                NetworkRequests.WEATHER_IN_CITY
        );
        Cursor cursor = getContentResolver().query(RequestContract.getUri(), null,
                mWhere.where(),
                mWhere.whereArgs(),
                null
        );
        Request savedRequest = null;
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                savedRequest = RequestContract.fromCursor(cursor);
            }
        } catch (Exception ignored) {

        } finally {
            cursor.close();
        }

        if (savedRequest != null && request.getStatus() == RequestStatus.IN_PROGRESS) {
            return;
        }
        request.setStatus(RequestStatus.IN_PROGRESS);

        if (TextUtils.equals(NetworkRequests.WEATHER_IN_CITY, request.getRequest())) {
            String cityName = intent.getStringExtra(CITY_NAME_KEY);
            executeCityRequest(request, cityName);
        }
    }

    private void executeCityRequest(@NonNull Request request, @NonNull String cityName) {
        try {
            City city = NetworkFactory.getWeatherService()
                    .getWeather(cityName)
                    .execute()
                    .body();
            Where mWhere = Where.create().equalTo(CityContract.CityEntry.COLUMN_CITY_NAME, cityName);
            getContentResolver().delete(CityContract.getUri(), mWhere.where(), mWhere.whereArgs());
            getContentResolver().insert(CityContract.getUri(), new CityContract().toValues(city));
            getContentResolver().notifyChange(CityContract.getUri(), null);
            request.setStatus(RequestStatus.SUCCESS);
        } catch (IOException e) {
            request.setStatus(RequestStatus.ERROR);
            request.setError(e.getMessage());
        } finally {
            getContentResolver().insert(RequestContract.getUri(), new RequestContract().toValues(request));
            getContentResolver().notifyChange(RequestContract.getUri(), null);
        }
    }
}
