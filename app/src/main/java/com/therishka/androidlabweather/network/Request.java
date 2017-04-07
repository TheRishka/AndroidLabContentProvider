package com.therishka.androidlabweather.network;


import android.support.annotation.NonNull;

/**
 * @author Rishad Mustafaev
 */
public class Request {

    @NetworkRequests
    private final String mRequest;

    private RequestStatus mStatus;

    private String mError;

    public Request(@NonNull @NetworkRequests String request) {
        mRequest = request;
    }

    public Request(@NonNull @NetworkRequests String request,
                   @NonNull RequestStatus status, @NonNull String error) {
        mRequest = request;
        mStatus = status;
        mError = error;
    }

    @NetworkRequests
    @NonNull
    public String getRequest() {
        return mRequest;
    }

    @NonNull
    public RequestStatus getStatus() {
        return mStatus;
    }

    public void setStatus(@NonNull RequestStatus status) {
        mStatus = status;
    }

    @NonNull
    public String getError() {
        return mError;
    }

    public void setError(@NonNull String error) {
        mError = error;
    }
}
