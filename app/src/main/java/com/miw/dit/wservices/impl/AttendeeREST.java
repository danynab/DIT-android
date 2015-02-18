package com.miw.dit.wservices.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miw.dit.model.Attendee;
import com.miw.dit.wservices.AttendeeWS;
import com.miw.dit.wservices.impl.http.HttpDelete;
import com.miw.dit.wservices.impl.http.HttpGet;
import com.miw.dit.wservices.impl.http.HttpPost;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public class AttendeeREST implements AttendeeWS {

    @Override
    public List<Attendee> findByEvent(int eventId) {
        return findListAttendees(WServicesUtil.getAttendeesByEventUrl(eventId));
    }

    @Override
    public void save(Attendee attendee) {
        Gson gson = new Gson();
        String json = gson.toJson(attendee);
        HttpPost httpPost = new HttpPost(WServicesUtil.getSaveAttendeetUrl(attendee.getEventId()), json);
        httpPost.execute();
    }

    @Override
    public void delete(int eventId, String userId) {
        HttpDelete httpDelete = new HttpDelete(WServicesUtil.getAttendeeUrl(eventId, userId));
        httpDelete.execute();
    }

    private List<Attendee> findListAttendees(String url) {
        HttpGet httpGet = new HttpGet(url);
        String json = httpGet.execute();
        Gson gson = new Gson();
        Type linesList = new TypeToken<List<Attendee>>() {
        }.getType();
        List<Attendee> result = gson.fromJson(json, linesList);
        return result == null ? new ArrayList<Attendee>() : result;
    }
}
