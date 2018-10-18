package com.example.cheesepuff.technewsapp;

/**
 * An {@link Technews} object contains information related to a single tech news.
 */
public class Technews {

    // place that publication the tech news
    private String mPublication;

    // headline of the tech news
    private String mHeadline;

    // date that the tech news is published
    private String mDate;

    // url that the tech news
    private String mUrl;

    // contributor on each tech news
    private String mContributor;

    /**
     * Constructs a new {@link Technews} object.
     *
     * @param publication is the place the tech news that is being published
     * @param headline is the title the tech news that is being published
     * @param date is the date the tech news that is being published
     * @param url is the website URL to find more details about the tech news
     */
    public Technews(String publication, String headline, String date, String url, String contributor) {
        mPublication = publication;
        mHeadline = headline;
        mDate = date;
        mUrl = url;
        mContributor = contributor;
    }

    // return the publication place of the tech news
    public String getPublication() {
        return mPublication;
    }

    // return the headline of the tech news
    public String getHeadline() {
        return mHeadline;
    }

    // return the time of the tech news
    public String getDate() {
        return mDate;
    }

    // return the URL of the tech news
    public String getUrl() {
        return mUrl;
    }

    // return the contributor of the tech news
    public String getContributor()  {return mContributor; }

}
