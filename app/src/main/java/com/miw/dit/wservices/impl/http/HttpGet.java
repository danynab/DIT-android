package com.miw.dit.wservices.impl.http;

import android.util.Log;

import org.apache.http.client.methods.HttpUriRequest;

/**
 * Created by Dani on 7/2/15.
 */
public class HttpGet extends HttpMethod {

    private String url;

    public HttpGet(String url) {
        Log.d(getClass().getSimpleName(), "url { " + url + " }");
        this.url = url;
    }


    @Override
    protected HttpUriRequest doMethod() {
        return new org.apache.http.client.methods.HttpGet(url);
    }
}
