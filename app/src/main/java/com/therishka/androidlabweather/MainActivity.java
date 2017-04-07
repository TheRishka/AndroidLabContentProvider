package com.therishka.androidlabweather;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.therishka.androidlabweather.network.NetworkRequests;
import com.therishka.androidlabweather.network.Request;
import com.therishka.androidlabweather.network.RequestsService;
import com.therishka.androidlabweather.tables.RequestContract;
import com.therishka.androidlabweather.tables.Where;

public class MainActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());

    private final ContentObserver databasesObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    requestChanges();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Request request = new Request(NetworkRequests.WEATHER_IN_CITY);
        RequestsService.start(this, request, "New York");
        getContentResolver().registerContentObserver(RequestContract.getUri(), false, databasesObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void requestChanges() {
        Where mWhere = Where.create().equalTo(
                RequestContract.RequestContractEntry.COLUMN_REQUEST,
                NetworkRequests.WEATHER_IN_CITY
        );
        Cursor mCursor = getContentResolver().query(RequestContract.getUri(), null,
                mWhere.where(),
                mWhere.whereArgs(),
                null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
            Request request = RequestContract.fromCursor(mCursor);
            System.out.println("RESULT REQUEST = " + request.getRequest() +
                    " STATUS = " + request.getStatus());
        }
    }
}
