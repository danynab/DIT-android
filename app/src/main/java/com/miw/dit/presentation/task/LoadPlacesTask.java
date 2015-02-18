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

    public LoadPlacesTask(int categoryId, LoadPlacesListener callback) {
        this.callback = callback;
        this.categoryId = categoryId;
    }

    @Override
    protected List<Place> doInBackground(Void... params) {
        List<Place> places = Factories.getBusinessFactory().getPlacesServices().getPlacesByCategoryId(categoryId, 0, 0, 0, null);
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
