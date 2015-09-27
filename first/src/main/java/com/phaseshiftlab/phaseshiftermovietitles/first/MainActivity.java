package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MovieDetailsFragment.OnFragmentInteractionListener,
        MovieGridFragment.ItemSelectedCallback,
        MovieGridFragment.OnFragmentInteractionListener {

    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MDFTAG";
    private static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";
    private boolean mTwoPane;
    MovieGridFragment movieGridFragment;


    public enum SortMode{
        POPULARITY("popularity"),
        VOTE_AVG("vote_average"),
        FAVORITES("favorites"),
        DESC("desc"),
        ASC("asc");

        private String value;

        SortMode(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String get(SortMode mode){
            return this.getValue() + "." + mode.getValue();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieGridFragment = (MovieGridFragment) getFragmentManager().findFragmentById(R.id.container);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailsFragment(), MOVIE_DETAILS_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            movieGridFragment.onChangeSortingOrder(SortMode.POPULARITY.get(SortMode.DESC));
            return true;
        } else if (id == R.id.action_sort_rating) {
            movieGridFragment.onChangeSortingOrder(SortMode.VOTE_AVG.get(SortMode.DESC));
            return true;
        } else if (id == R.id.action_sort_favorite) {
            movieGridFragment.onChangeSortingOrder(SortMode.FAVORITES.get(SortMode.DESC));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemSelected(MovieInfo movieInfo) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.MOVIE_PARCEL, movieInfo);

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieItemDetailActivity.class)
                    .putExtra(MOVIE_PARCEL, movieInfo);

            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(MovieInfo movieInfo) {
        if(mTwoPane){
              onItemSelected(movieInfo);
        }

    }
}

