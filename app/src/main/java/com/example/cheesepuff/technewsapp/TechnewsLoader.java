package com.example.cheesepuff.technewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of tech news by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class TechnewsLoader extends AsyncTaskLoader<List<Technews>> {

    /** Tag for log messages */
    private static final String LOG_TAG = TechnewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link TechnewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public TechnewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Technews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of tech news.
        List<Technews> technewss = QueryUtils.fetchTechnewsData(mUrl);
        return technewss;
    }
}

