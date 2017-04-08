package com.therishka.androidlabweather.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.therishka.androidlabweather.data.WeatherProvider;
import com.therishka.androidlabweather.models.City;
import com.therishka.androidlabweather.models.Main;
import com.therishka.androidlabweather.models.Weather;
import com.therishka.androidlabweather.models.Wind;

import java.util.ArrayList;
import java.util.List;

import static com.therishka.androidlabweather.tables.CityContract.CityEntry.COLUMN_CITY_NAME;
import static com.therishka.androidlabweather.tables.CityContract.CityEntry.COLUMN_MAIN;
import static com.therishka.androidlabweather.tables.CityContract.CityEntry.COLUMN_WEATHER;
import static com.therishka.androidlabweather.tables.CityContract.CityEntry.COLUMN_WIND;

/**
 * @author Rishad Mustafaev
 */
public class CityContract {

    public static final String TABLE_NAME = "cities";

    public void createTable(@NonNull SQLiteDatabase database) {
        TableBuilder.create(TABLE_NAME)
                .textColumn(COLUMN_WIND)
                .textColumn(COLUMN_CITY_NAME)
                .textColumn(COLUMN_MAIN)
                .textColumn(COLUMN_WEATHER)
                .execute(database);
    }

    @NonNull
    public ContentValues toValues(@NonNull City city) {
        Gson mGson = new Gson();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY_NAME, city.getName());
        values.put(COLUMN_WEATHER, mGson.toJson(city.getWeather()));
        values.put(COLUMN_MAIN, mGson.toJson(city.getMain()));
        values.put(COLUMN_WIND, mGson.toJson(city.getWind()));
        return values;
    }


    @NonNull
    public City fromCursor(@NonNull Cursor cursor) {
        City city = new City();
        Gson mGson = new Gson();
        city.setName(cursor.getString(cursor.getColumnIndex(
                COLUMN_CITY_NAME)));

        Weather weather = mGson.fromJson(cursor.getString(cursor.getColumnIndex(
                COLUMN_WEATHER)), Weather.class);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(weather);
        city.setWeathers(weathers);

        city.setMain(mGson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_MAIN)), Main.class));
        city.setWind(mGson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_WIND)), Wind.class));

        return city;
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
