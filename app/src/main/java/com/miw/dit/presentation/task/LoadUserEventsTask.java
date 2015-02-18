package com.miw.dit.presentation.task;

import android.os.AsyncTask;
import android.util.Log;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 11/2/15.
 */
public class LoadUserEventsTask extends AsyncTask<Void, Void, List<Event>> {

    private LoadEventsListener callback;
    private String userId;
    private Integer fromId;
    private Integer elements;

    public LoadUserEventsTask(LoadEventsListener callback, String userId, Integer fromId, Integer elements) {
        this.callback = callback;
        this.userId = userId;
        this.fromId = fromId;
        this.elements = elements;
    }

    @Override
    protected List<Event> doInBackground(Void... params) {
        return Factories.getBusinessFactory().getEventServices().getEventsByUser(userId, fromId, elements);
    }

    @Override
    protected void onPostExecute(List<Event> events) {
        super.onPostExecute(events);
        Log.d(this.getClass().getSimpleName(), "onPostExecute: events { " + events + " }");
        callback.onLoadEvents(events);
    }
}
