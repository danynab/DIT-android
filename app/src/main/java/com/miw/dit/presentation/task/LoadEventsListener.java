package com.miw.dit.presentation.task;

import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 12/2/15.
 */
public interface LoadEventsListener {
    public void onLoadEvents(List<Event> events);

}
