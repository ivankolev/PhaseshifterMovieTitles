package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieReviews;

import java.util.List;

public class MovieReviewsAdapter extends BaseAdapter {

    private Context context;
    private List<MovieReviews> reviewsList;

    public MovieReviewsAdapter(Context context, List<MovieReviews> results) {
        this.reviewsList = results;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviewsList.size();
    }

    @Override
    public MovieReviews getItem(int position) {
        return reviewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MovieReviews reviewItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_review_item, parent, false);
            viewHolder.review_author = (TextView) convertView.findViewById(R.id.review_author);
            viewHolder.review_content = (TextView) convertView.findViewById(R.id.review_content);
            viewHolder.review_url = (Button) convertView.findViewById(R.id.review_url);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.review_author.setText("Author: " + reviewItem.author);
        viewHolder.review_content.setText(reviewItem.content);
        viewHolder.review_url.setText(reviewItem.url);
        viewHolder.review_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(reviewItem.url));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder extends MovieReviews {
        TextView review_author;
        TextView review_content;
        Button review_url;
    }
}
