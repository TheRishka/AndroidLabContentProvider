package com.therishka.androidlabweather;

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
public class WeatherSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    public static final int CURRENT_VERSION = 1;

    public WeatherSQLiteOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, CURRENT_VERSION);
    }

    public WeatherSQLiteOpenHelper(Context context,
                                   String name,
                                   SQLiteDatabase.CursorFactory factory,
                                   int version) {
        super(context, name, factory, version);
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
