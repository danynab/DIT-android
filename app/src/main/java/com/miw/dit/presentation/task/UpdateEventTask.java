package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Event;

/**
 * Created by Dani on 15/2/15.
 */
public class UpdateEventTask extends AsyncTask<Void, Void, Void> {

    private Event event;

    public UpdateEventTask(Event event) {
        this.event = event;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Factories.getBusinessFactory().getEventServices().updateEvent(event);
        return null;
    }
}
