package com.example.cheesepuff.technewsapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link TechnewsAdapter} knows how to create a list item layout for each tech news
 * in the data source (a list of {@link Technews} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class TechnewsAdapter extends ArrayAdapter<Technews> {

    static class ViewHolder {
        TextView publication;
        TextView headline;
        TextView date;
        TextView contributor;
    }

    /**
     * Constructs a new {@link TechnewsAdapter}.
     *
     * @param context of the app
     * @param technewss is the list of tech news, which is the data source of the adapter
     */
    public TechnewsAdapter(Context context, List<Technews> technewss) {
        super(context, 0, technewss);
    }

    /**
     * Returns a list item view that displays information about the tech news at the given position
     * in the list of tech news.ds
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.technews_list_item, parent, false);
        }

        // create a view holder instance with current tech news data
        ViewHolder holder = new ViewHolder();
        Technews currentTechnews = getItem(position);

        // connect view holder with layout for tech news publication, headline and date
        holder.publication = listItemView.findViewById(R.id.publication);
        holder.publication.setText(currentTechnews.getPublication());

        holder.headline = listItemView.findViewById(R.id.headline);
        holder.headline.setText(currentTechnews.getHeadline());

        holder.date = listItemView.findViewById(R.id.date);
        holder.date.setText(currentTechnews.getDate());

        holder.contributor = listItemView.findViewById(R.id.contributor);
        holder.contributor.setText(currentTechnews.getContributor());


        // return the list data
        return listItemView;
    }
}

