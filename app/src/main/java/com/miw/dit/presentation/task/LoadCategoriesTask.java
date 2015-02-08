package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Category;

import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public class LoadCategoriesTask extends AsyncTask<Void, Void, List<Category>> {

    private onLoadCategoriesListener callback;

    public LoadCategoriesTask(onLoadCategoriesListener callback) {
        this.callback = callback;
    }

    @Override
    protected List<Category> doInBackground(Void... params) {
        List<Category> categories = Factories.getBusinessFactory().getCategoryServices().getCategories();
        return categories;
    }

    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        callback.onLoadCategories(categories);
    }

    public interface onLoadCategoriesListener {
        public void onLoadCategories(List<Category> categories);
    }
}
