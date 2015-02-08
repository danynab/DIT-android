package com.miw.dit.presentation.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Dani on 5/2/15.
 */
public class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private OnLoadImageListener listener;

    public LoadImageTask(String url, OnLoadImageListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        listener.onLoadImage(bitmap);
    }

    public interface OnLoadImageListener {
        public void onLoadImage(Bitmap image);
    }
}
