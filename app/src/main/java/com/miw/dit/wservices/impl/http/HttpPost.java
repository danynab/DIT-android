package com.miw.dit.wservices.impl.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Dani on 7/2/15.
 */
public class HttpPost extends HttpMethod {

    private String url;
    private String content;

    public HttpPost(String url, String content) {
        this.url = url;
        this.content = content;
    }

    @Override
    protected HttpUriRequest doMethod() throws UnsupportedEncodingException {
        org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        byte[] contentBytes = content.getBytes("UTF-8");
        httpPost.setEntity((new ByteArrayEntity(contentBytes)));
        return httpPost;
    }
}
