package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Category;

/**
 * Created by Dani on 7/2/15.
 */
public class LoadCategoryTask extends AsyncTask<Void, Void, Category> {

    private LoadCategoryListener callback;
    private int categoryId;

    public LoadCategoryTask(int categoryId, LoadCategoryListener callback) {
        this.callback = callback;
        this.categoryId = categoryId;
    }

    @Override
    protected Category doInBackground(Void... params) {
        Category category = Factories.getBusinessFactory().getCategoryServices().getCategory(categoryId);
        return category;
    }

    @Override
    protected void onPostExecute(Category category) {
        super.onPostExecute(category);
        callback.onLoadCategory(category);
    }

    public interface LoadCategoryListener {
        public void onLoadCategory(Category category);
    }
}
