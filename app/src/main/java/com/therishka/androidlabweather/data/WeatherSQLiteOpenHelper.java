package com.therishka.androidlabweather.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.therishka.androidlabweather.tables.CityContract;
import com.therishka.androidlabweather.tables.RequestContract;
import com.therishka.androidlabweather.tables.WeatherCityContract;

/**
 * @author Rishad Mustafaev
 */
class WeatherSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int CURRENT_VERSION = 1;

    WeatherSQLiteOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, CURRENT_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        new CityContract().createTable(db);
        new RequestContract().createTable(db);
        new WeatherCityContract().createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing yet... prepare migration here.
    }
}
