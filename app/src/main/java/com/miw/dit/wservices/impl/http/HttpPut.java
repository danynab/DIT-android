package com.miw.dit.wservices.impl.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Dani on 7/2/15.
 */
public class HttpPut extends HttpMethod {

    private String url;
    private String content;

    public HttpPut(String url, String content) {
        this.url = url;
        this.content = content;
    }

    @Override
    protected HttpUriRequest doMethod() throws UnsupportedEncodingException {
        org.apache.http.client.methods.HttpPut httpPut = new org.apache.http.client.methods.HttpPut(url);
        httpPut.setHeader("Content-Type", "application/json");
        byte[] contentBytes = content.getBytes("UTF-8");
        httpPut.setEntity((new ByteArrayEntity(contentBytes)));
        return httpPut;
    }
}
