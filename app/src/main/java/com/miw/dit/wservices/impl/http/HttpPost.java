package com.miw.dit.wservices.impl.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Dani on 7/2/15.
 */
public class HttpPost extends HttpMethod {

    private static final String TAG = "HttpPost";

    private String url;
    private String content;

    public HttpPost(String url, String content) {
        this.url = url;
        this.content = content;
    }

    @Override
    protected HttpUriRequest doMethod() throws UnsupportedEncodingException {
        org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);
        httpPost.setHeader("content-type", "application/json; charset=utf-8");
        httpPost.setEntity(new StringEntity(content));
        return httpPost;
    }
}
