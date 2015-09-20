package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.makeText;


public class MovieItemDetailActivity extends AppCompatActivity {

    @Bind(R.id.originalTitle) TextView originalTitle;
    @Bind(R.id.moviePosterThumb) ImageView moviePosterThumb;
    @Bind(R.id.plotSynopsis) TextView plotSynopsis;
    @Bind(R.id.userRating) TextView userRating;
    @Bind(R.id.releaseDate) TextView releaseDate;

    private static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item_detail);
        MovieInfo movieInfo = getIntent().getExtras().getParcelable(MOVIE_PARCEL);
        ButterKnife.bind(this);
        populateViews(movieInfo);
    }

    private void populateViews(MovieInfo movieInfo) {
        originalTitle.setText(movieInfo.original_title);

        Context context = moviePosterThumb.getContext();
        String path = context.getResources().getString(R.string.image_url) + "/" +
                context.getResources().getString(R.string.width_342) + "/" +
                movieInfo.poster_path;
        Picasso.with(context)
                .load(path)
                .into(moviePosterThumb);

        plotSynopsis.setText(movieInfo.overview);
        userRating.setText(context.getResources().getString(R.string.rating) + movieInfo.vote_average.toString());
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

    public void toggleFavorite(View view) {
        makeText(view.getContext(), view.getContext().getResources().getString(R.string.toggle_add_to_fav), Toast.LENGTH_SHORT).show();

    }
}
