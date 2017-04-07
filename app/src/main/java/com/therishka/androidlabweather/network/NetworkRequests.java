package com.therishka.androidlabweather.network;

import android.support.annotation.StringDef;

/**
 * @author Rishad Mustafaev
 */
@StringDef({NetworkRequests.WEATHER_IN_CITY})
public @interface NetworkRequests {
    String WEATHER_IN_CITY = "weather_in_city";
}
