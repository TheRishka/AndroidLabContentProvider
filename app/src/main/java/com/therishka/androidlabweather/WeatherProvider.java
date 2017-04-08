package com.therishka.androidlabweather;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.therishka.androidlabweather.tables.CityContract;
import com.therishka.androidlabweather.tables.RequestContract;
import com.therishka.androidlabweather.tables.WeatherCityContract;

/**
 * @author Rishad Mustafaev
 */
public class WeatherProvider extends ContentProvider {

    private static final int CITY_URI = 100;
    private static final int REQUESTS_URI = 105;
    private static final int WEATHER_CITY_URI = 111;

    private static final String DATABASE_NAME = "weather.db";
    private static final String CONTENT_AUTHORITY = "com.therishka.androidlabweather";

    public static final String TAG = WeatherProvider.class.getSimpleName();

    private static Uri baseUri;

    private static String baseContentAuthority;


    private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private WeatherSQLiteOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            dbOpenHelper = new WeatherSQLiteOpenHelper(getContext());
            baseContentAuthority = CONTENT_AUTHORITY;
            baseUri = Uri.parse("content://" + baseContentAuthority);
            registerTables();
            return true;
        } else {
            return false;
        }
    }

    private void registerTables() {
        mUriMatcher.addURI(baseContentAuthority, CityContract.TABLE_NAME, CITY_URI);
        mUriMatcher.addURI(baseContentAuthority, RequestContract.TABLE_NAME, REQUESTS_URI);
        mUriMatcher.addURI(baseContentAuthority, WeatherCityContract.TABLE_NAME, WEATHER_CITY_URI);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = mUriMatcher.match(uri);
        switch (matchCode) {
            case CITY_URI:
                return CityContract.TABLE_NAME;
            case WEATHER_CITY_URI:
                return WeatherCityContract.TABLE_NAME;
            case REQUESTS_URI:
                return RequestContract.TABLE_NAME;
            default:
                throw new IllegalStateException("WRONG URI! request = " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String tableName = getType(uri);
        cursor = db.query(tableName,
                projection,
                selection,
                selectionArgs,
                null, null, sortOrder);
        if (getContext() != null && getContext().getContentResolver() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String tableName = getType(uri);
        long id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String tableName = getType(uri);
        return db.delete(tableName, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String tableName = getType(uri);
        return db.update(tableName, values, selection, selectionArgs);
    }

    public static Uri getBaseUri() {
        return baseUri;
    }

    public static String getBaseContentAuthority() {
        return baseContentAuthority;
    }
}
