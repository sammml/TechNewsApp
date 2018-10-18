package com.example.cheesepuff.technewsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Technews>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /** Adapter for the list of tech news */
    private TechnewsAdapter mAdapter;

    /** URL for earthquake data from the guardian dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?order-by=newest&use-date=published&show-tags=contributor&q=tech&api-key=test";

    /**
     * Constant value for the tech news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int TECHNEWS_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView technewsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        technewsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of tech news as input
        mAdapter = new TechnewsAdapter(this, new ArrayList<Technews>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        technewsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected tech news.
        technewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current tech news that was clicked on
                Technews currentTechnews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri technewsUri = Uri.parse(currentTechnews.getUrl());

                // Create a new intent to view the tech news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, technewsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(TECHNEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Technews>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new TechnewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Technews>> loader, List<Technews> technewss) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No tech news found."
        mEmptyStateTextView.setText(R.string.no_content);

        // Clear the adapter of previous tech news data
        //mAdapter.clear();

        // If there is a valid list of {@link Technews}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (technewss != null && !technewss.isEmpty()) {
            mAdapter.addAll(technewss);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Technews>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}

