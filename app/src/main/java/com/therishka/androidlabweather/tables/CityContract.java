package com.therishka.androidlabweather.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.therishka.androidlabweather.WeatherProvider;
import com.therishka.androidlabweather.models.City;

/**
 * @author Rishad Mustafaev
 */
public class CityContract {

    public static final String TABLE_NAME = "cities";

    public void createTable(@NonNull SQLiteDatabase database) {
        TableBuilder.create(TABLE_NAME)
                .textColumn(CityEntry.COLUMN_WIND)
                .textColumn(CityEntry.COLUMN_CITY_NAME)
                .textColumn(CityEntry.COLUMN_MAIN)
                .textColumn(CityEntry.COLUMN_WEATHER)
                .execute(database);
    }

    @NonNull
    public ContentValues toValues(@NonNull City city) {
        Gson mGson = new Gson();
        ContentValues values = new ContentValues();
        values.put(CityEntry.COLUMN_CITY_NAME, city.getName());
        values.put(CityEntry.COLUMN_WEATHER, mGson.toJson(city.getWeather()));
        values.put(CityEntry.COLUMN_MAIN, mGson.toJson(city.getMain()));
        values.put(CityEntry.COLUMN_WIND, mGson.toJson(city.getWind()));
        return values;
    }

    public static Uri getUri() {
        return WeatherProvider.getBaseUri().buildUpon().appendPath(TABLE_NAME).build();
    }

    public static final class CityEntry implements BaseColumns {

        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_WEATHER = "weather";
        public static final String COLUMN_MAIN = "main";
        public static final String COLUMN_WIND = "wind";
    }
}
