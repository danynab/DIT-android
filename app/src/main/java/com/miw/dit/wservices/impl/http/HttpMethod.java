package com.miw.dit.wservices.impl.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Dani on 7/2/15.
 */
public abstract class HttpMethod {

    protected abstract HttpUriRequest doMethod() throws Exception;

    public String execute() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(doMethod());
            return IOUtils.toString(httpResponse.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
