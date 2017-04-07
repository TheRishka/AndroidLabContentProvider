package com.therishka.androidlabweather.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.therishka.androidlabweather.WeatherProvider;
import com.therishka.androidlabweather.network.NetworkRequests;
import com.therishka.androidlabweather.network.Request;
import com.therishka.androidlabweather.network.RequestStatus;

import static com.therishka.androidlabweather.tables.RequestContract.RequestContractEntry.COLUMN_ERROR;
import static com.therishka.androidlabweather.tables.RequestContract.RequestContractEntry.COLUMN_REQUEST;
import static com.therishka.androidlabweather.tables.RequestContract.RequestContractEntry.COLUMN_STATUS;

/**
 * @author Rishad Mustafaev
 */
public class RequestContract {

    public static final String TABLE_NAME = "requests";

    public void createTable(@NonNull SQLiteDatabase database) {
        TableBuilder.create(TABLE_NAME)
                .textColumn(COLUMN_REQUEST)
                .textColumn(COLUMN_STATUS)
                .textColumn(COLUMN_ERROR)
                .primaryKey(COLUMN_REQUEST)
                .execute(database);
    }

    @NonNull
    public ContentValues toValues(@NonNull Request request) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REQUEST, request.getRequest());
        values.put(COLUMN_STATUS, request.getStatus().name());
        values.put(COLUMN_ERROR, request.getError());
        return values;
    }

    @NonNull
    public static Request fromCursor(@NonNull Cursor cursor) {
        @NetworkRequests String request = cursor.getString(cursor.getColumnIndex(COLUMN_REQUEST));
        RequestStatus status = RequestStatus.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
        String error = cursor.getString(cursor.getColumnIndex(COLUMN_ERROR));
        return new Request(request, status, error);
    }

    public static Uri getUri() {
        return WeatherProvider.getBaseUri().buildUpon().appendPath(TABLE_NAME).build();
    }

    public static final class RequestContractEntry implements BaseColumns {
        public static final String COLUMN_REQUEST = "request";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_ERROR = "error";
    }
}
