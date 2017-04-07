package com.therishka.androidlabweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author Rishad Mustafaev
 */
public class WeatherReceiver extends BroadcastReceiver {

    @NonNull
    private final CallbacksReceiver callbacksReceiver;

    public WeatherReceiver(@NonNull CallbacksReceiver receiver) {
        callbacksReceiver = receiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callbacksReceiver.callbackReceived();
    }

    public interface CallbacksReceiver {
        void callbackReceived();
    }
}
