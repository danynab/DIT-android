package com.miw.dit.wservices.impl.http;

import android.util.Log;

import org.apache.http.client.methods.HttpUriRequest;

/**
 * Created by Dani on 7/2/15.
 */
public class HttpDelete extends HttpMethod {

    private String url;

    public HttpDelete(String url) {
        Log.d(getClass().getSimpleName(), "url { " + url + " }");
        this.url = url;
    }


    @Override
    protected HttpUriRequest doMethod() {
        return new org.apache.http.client.methods.HttpDelete(url);
    }
}
