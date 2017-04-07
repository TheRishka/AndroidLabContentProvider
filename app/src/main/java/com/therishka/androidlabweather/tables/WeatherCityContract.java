package com.therishka.androidlabweather.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.therishka.androidlabweather.WeatherProvider;
import com.therishka.androidlabweather.models.WeatherCity;

/**
 * @author Rishad Mustafaev
 */
public class WeatherCityContract {

    public static final String TABLE_NAME = "weather_cities";

    public void createTable(@NonNull SQLiteDatabase database) {
        TableBuilder.create(TABLE_NAME)
                .textColumn(WeatherCityEntry.COLUMN_CITY_ID)
                .textColumn(WeatherCityEntry.COLUMN_CITY_NAME)
                .execute(database);
    }

    @NonNull
    public ContentValues toValues(@NonNull WeatherCity weatherCity) {
        ContentValues values = new ContentValues();
        values.put(WeatherCityEntry.COLUMN_CITY_ID, weatherCity.getCityId());
        values.put(WeatherCityEntry.COLUMN_CITY_NAME, weatherCity.getCityName());
        return values;
    }

    public static Uri getUri() {
        return WeatherProvider.getBaseUri().buildUpon().appendPath(TABLE_NAME).build();
    }

    public static final class WeatherCityEntry implements BaseColumns {
        public static final String COLUMN_CITY_ID = "id";
        public static final String COLUMN_CITY_NAME = "city_name";
    }
}
