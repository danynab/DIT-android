package com.miw.dit.presentation.task;

import android.os.AsyncTask;
import android.util.Log;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 11/2/15.
 */
public class LoadNearEventsTask extends AsyncTask<Void, Void, List<Event>> {

    private LoadEventsListener callback;
    private double lat;
    private double lng;
    private double radius;
    private Integer fromId;
    private Integer elements;
    private Integer categoryId;

    public LoadNearEventsTask(LoadEventsListener callback, double lat, double lng, double radius, Integer fromId, Integer elements) {
        this.callback = callback;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.fromId = fromId;
        this.elements = elements;
        this.categoryId = null;
    }

    public LoadNearEventsTask(LoadEventsListener callback, int categoryId, double lat, double lng, double radius, Integer fromId, Integer elements) {
        this(callback, lat, lng, radius, fromId, elements);
        this.categoryId = categoryId;
    }


    @Override
    protected List<Event> doInBackground(Void... params) {
        List<Event> events;
        if (categoryId == null)
            events = Factories.getBusinessFactory().getEventServices().getNearEvents(lat, lng, radius, fromId, elements);
        else
            events = Factories.getBusinessFactory().getEventServices().getNearEventsByCategory(categoryId, lat, lng, radius, fromId, elements);
        return events;
    }

    @Override
    protected void onPostExecute(List<Event> events) {
        super.onPostExecute(events);
        Log.d(this.getClass().getSimpleName(), "onPostExecute: events { " + events + " }");
        callback.onLoadEvents(events);
    }
}
