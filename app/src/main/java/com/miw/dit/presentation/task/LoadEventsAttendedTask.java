package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public class LoadEventsAttendedTask extends AsyncTask<Void, Void, List<Event>> {

    private LoadEventsListener callback;

    private String userId;
    private Integer fromId;
    private Integer elements;


    public LoadEventsAttendedTask(String userId, Integer fromId, Integer elements, LoadEventsListener callback) {
        this.userId = userId;
        this.fromId = fromId;
        this.elements = elements;
        this.callback = callback;
    }

    @Override
    protected List<Event> doInBackground(Void... params) {
        return Factories.getBusinessFactory().getEventServices().getEventsAttendedByUser(userId, fromId, elements);
    }

    @Override
    protected void onPostExecute(List<Event> events) {
        super.onPostExecute(events);
        callback.onLoadEvents(events);
    }

}
