package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class MovieItemDetail extends AppCompatActivity {

    private static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item_detail);
        MovieInfo movieInfo = getIntent().getExtras().getParcelable(MOVIE_PARCEL);
        populateViews(movieInfo);
    }

    private void populateViews(MovieInfo movieInfo) {
        TextView originalTitle = (TextView)findViewById(R.id.originalTitle);
        originalTitle.setText(movieInfo.original_title);

        ImageView moviePosterThumb = (ImageView)findViewById(R.id.moviePosterThumb);
        Context context = moviePosterThumb.getContext();
        String path = context.getResources().getString(R.string.image_url) + "/" +
                context.getResources().getString(R.string.width_185) + "/" +
                movieInfo.poster_path;
        Picasso.with(context)
                .load(path)
                .into(moviePosterThumb);

        TextView plotSynopsis = (TextView)findViewById(R.id.plotSynopsis);
        plotSynopsis.setText(movieInfo.overview);

        TextView userRating = (TextView)findViewById(R.id.userRating);
        userRating.setText(context.getResources().getString(R.string.rating) + movieInfo.vote_average.toString());

        TextView releaseDate = (TextView)findViewById(R.id.releaseDate);
        releaseDate.setText(context.getResources().getString(R.string.release_date) + movieInfo.release_date);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
