package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Place;

import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public class LoadPlacesTask extends AsyncTask<Void, Void, List<Place>> {

    private LoadPlacesListener callback;
    private int categoryId;
    private double lat;
    private double lng;
    private double radius;
    private int elements;

    public LoadPlacesTask(int categoryId, double lat, double lng, double radius, int elements, LoadPlacesListener callback) {
        this.callback = callback;
        this.categoryId = categoryId;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.elements = elements;
    }

    @Override
    protected List<Place> doInBackground(Void... params) {
        List<Place> places = Factories.getBusinessFactory().getPlacesServices().getPlacesByCategoryId(categoryId, lat, lng, radius, elements);
        return places;
    }

    @Override
    protected void onPostExecute(List<Place> places) {
        super.onPostExecute(places);
        callback.onLoadPlaces(places);
    }

    public interface LoadPlacesListener {
        public void onLoadPlaces(List<Place> places);
    }
}
