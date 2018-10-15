package com.example.cheesepuff.technewsapp;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving tech news data from GUARDIAN.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // declare constance variables
    private static final String publication = "sectionName";
    private static final String headline = "webTitle";
    private static final String date = "webPublicationDate";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the GUARDIAN dataset and return a list of {@link Technews} objects.
     */
    public static List<Technews> fetchTechnewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Technews}s
        List<Technews> technewss = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Technews}s
        return technewss;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        // try method to setup http connection, read timeout 10 - 15 sec, then get http method
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the tech news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Technews} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Technews> extractFeatureFromJson(String technewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(technewsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding tech news to
        List<Technews> technewss = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(technewsJSON);
            JSONObject baseJsonResponseResults = baseJsonResponse.getJSONObject("response");

            // create a JSONArray from the key results
            JSONArray technewsArray = baseJsonResponseResults.getJSONArray("results");

            // For each tech news in the technewsArray, create an {@link Technews} object
            for (int i = 0; i < technewsArray.length(); i++) {

                // Get a single tech news at position i within the list of tech news
                JSONObject currentTechnews = technewsArray.getJSONObject(i);

                // For a given tech news, extract the JSONObject associated with the
                // key called "sectionName", which represents a list of all publication places
                // for that tech news.
                String  publication = currentTechnews.getString("sectionName");
                String headline = currentTechnews.getString("webTitle");

                // Extract the value for the key called "time"
                String date = formatDate(currentTechnews.getString("webPublicationDate"));

                // Extract the value for the key called "url"
                String url = currentTechnews.getString("webUrl");

                // Create a new {@link Technews} object with the publication, location, time,
                // and url from the JSON response.
                Technews technews = new Technews(publication, headline, date, url);

                // Add the new {@link Technews} to the list of tech news.
                technewss.add(technews);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the tech news JSON results", e);
        }

        // Return the list of tech news
        return technewss;
    }

    private static String formatDate(String date) {
        // date format only
        return date.substring(0,date.indexOf("T"));
    }

}

