package com.miw.dit.wservices.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miw.dit.model.Event;
import com.miw.dit.wservices.EventWS;
import com.miw.dit.wservices.impl.http.HttpDelete;
import com.miw.dit.wservices.impl.http.HttpGet;
import com.miw.dit.wservices.impl.http.HttpPost;
import com.miw.dit.wservices.impl.http.HttpPut;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 10/2/15.
 */
public class EventREST implements EventWS {

    @Override
    public List<Event> findNearEvents(double lat, double lng, double radius, Integer fromId, Integer elements) {
        return findListEvents(WServicesUtil.getNearEventsUrl(lat, lng, radius, fromId, elements));
    }

    @Override
    public List<Event> findNearEventsByCategoryId(int categoryId, double lat, double lng, double radius, Integer fromId, Integer elements) {
        return findListEvents(WServicesUtil.getNearEventsByCategoryUrl(categoryId, lat, lng, radius, fromId, elements));
    }

    @Override
    public List<Event> findEventsByUserId(String userId, Integer fromId, Integer elements) {
        return findListEvents(WServicesUtil.getEventsByUserUrl(userId, fromId, elements));
    }

    @Override
    public List<Event> findEventsAttendedByUserId(String userId, Integer fromId, Integer elements) {
        return findListEvents(WServicesUtil.getEventsAttendedByUsertUrl(userId, fromId, elements));
    }

    @Override
    public void save(Event event) {
        Gson gson = new Gson();
        String json = gson.toJson(event);
        Log.d(getClass().getSimpleName(), "save: json { " + json + " }");
        HttpPost httpPost = new HttpPost(WServicesUtil.getSaveEventUrl(), json);
        httpPost.execute();
    }

    @Override
    public void update(Event event) {
        Gson gson = new Gson();
        String json = gson.toJson(event);
        Log.d(getClass().getSimpleName(), "update: json { " + json + " }");
        HttpPut httpPut = new HttpPut(WServicesUtil.getEventUrl(event.getId()), json);
        httpPut.execute();
    }

    @Override
    public void delete(Event event) {
        HttpDelete httpDelete = new HttpDelete(WServicesUtil.getEventUrl(event.getId()));
        httpDelete.execute();
    }

    private List<Event> findListEvents(String url) {
        HttpGet httpGet = new HttpGet(url);
        String json = httpGet.execute();
        Log.d(getClass().getSimpleName(), "findListEvents: json { " + json + " }");
        Gson gson = new Gson();
        Type linesList = new TypeToken<List<Event>>() {
        }.getType();
        List<Event> result = gson.fromJson(json, linesList);
        return result == null ? new ArrayList<Event>() : result;
    }
}
