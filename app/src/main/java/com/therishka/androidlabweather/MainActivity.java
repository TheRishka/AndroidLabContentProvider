package com.therishka.androidlabweather;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.therishka.androidlabweather.loading.LoadingDialog;
import com.therishka.androidlabweather.loading.LoadingView;
import com.therishka.androidlabweather.models.City;
import com.therishka.androidlabweather.network.NetworkRequests;
import com.therishka.androidlabweather.network.Request;
import com.therishka.androidlabweather.network.RequestStatus;
import com.therishka.androidlabweather.network.RequestsService;
import com.therishka.androidlabweather.tables.CityContract;
import com.therishka.androidlabweather.tables.RequestContract;
import com.therishka.androidlabweather.tables.Where;

public class MainActivity extends AppCompatActivity {


    private static final String WEATHER_KEY = "weather";
    private static final String CITY_NAME_KEY = "city_name";

    Toolbar mToolbar;
    TextView mToolbarTitle;
    View mWeatherLayout;
    TextView mWeatherMain;
    TextView mTemperature;
    TextView mPressure;
    TextView mHumidity;
    TextView mWindSpeed;
    TextView mErrorLayout;

    private LoadingView mLoadingView;
    private String mCityName;

    @Nullable
    private City mCity;

    Handler mHandler = new Handler(Looper.getMainLooper());

    private final ContentObserver newDbObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange, final Uri uri) {
            super.onChange(selfChange, uri);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    requestChanges(uri);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (savedInstanceState == null || !savedInstanceState.containsKey(WEATHER_KEY)) {
            loadWeather();
        } else {
            mCity = (City) savedInstanceState.getSerializable(WEATHER_KEY);
            showWeather();
        }
    }

    void initViews() {
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mWeatherLayout = findViewById(R.id.weather_layout);
        mWeatherMain = (TextView) findViewById(R.id.weather_main);
        mTemperature = (TextView) findViewById(R.id.temperature);
        mPressure = (TextView) findViewById(R.id.pressure);
        mHumidity = (TextView) findViewById(R.id.humidity);
        mWindSpeed = (TextView) findViewById(R.id.wind_speed);
        mErrorLayout = (TextView) findViewById(R.id.error_layout);

        mCityName = getIntent().getStringExtra(CITY_NAME_KEY);
        mToolbarTitle.setText(mCityName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCity != null) {
            outState.putSerializable(WEATHER_KEY, mCity);
        }
    }

    void loadWeather() {
        mWeatherLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.GONE);
        mLoadingView.showLoadingIndicator();

        Request request = new Request(NetworkRequests.WEATHER_IN_CITY);
        RequestsService.start(this, request, "New York");
        getContentResolver().registerContentObserver(RequestContract.getUri(), false, newDbObserver);
    }

    void showWeather() {
        if (mCity == null || mCity.getMain() == null || mCity.getWeather() == null
                || mCity.getWind() == null) {
            showError();
            return;
        }

        mLoadingView.hideLoadingIndicator();
        mWeatherLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);

        mToolbarTitle.setText(mCity.getName());
        mWeatherMain.setText(mCity.getWeather().getMain());
        mTemperature.setText(getString(R.string.f_temperature, mCity.getMain().getTemp()));
        mPressure.setText(getString(R.string.f_pressure, mCity.getMain().getPressure()));
        mHumidity.setText(getString(R.string.f_humidity, mCity.getMain().getHumidity()));
        mWindSpeed.setText(getString(R.string.f_wind_speed, mCity.getWind().getSpeed()));
    }

    private void showError() {
        mWeatherLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void requestChanges(@NonNull Uri uri) {
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
            mCursor.close();
            if (RequestStatus.SUCCESS.equals(request.getStatus())) {
                Where cityWhere = Where.create();
                Cursor cityCursor = getContentResolver().query(CityContract.getUri(), null,
                        cityWhere.where(), cityWhere.whereArgs(), null);
                if (cityCursor != null) {
                    cityCursor.moveToFirst();
                    mCity = new CityContract().fromCursor(cityCursor);
                    cityCursor.close();
                    showWeather();
                }
            }
        }
    }
}
