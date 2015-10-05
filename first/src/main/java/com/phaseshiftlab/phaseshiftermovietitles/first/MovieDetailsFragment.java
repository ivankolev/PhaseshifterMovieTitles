package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.phaseshiftlab.phaseshiftermovietitles.first.data.FavoriteMoviesContract;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MovieDetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "MovieDetailsFragment";
    protected static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";
    private static final int URL_LOADER = 1;
    private MovieInfo movieInfo = null;

    @Bind(R.id.originalTitle) TextView originalTitle;
    @Bind(R.id.moviePosterThumb) ImageView moviePosterThumb;
    @Bind(R.id.plotSynopsis) TextView plotSynopsis;
    @Bind(R.id.userRating) TextView userRating;
    @Bind(R.id.releaseDate) TextView releaseDate;

    private Context context;
    private ScrollView scrollView;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scrollView = (ScrollView) inflater.inflate(R.layout.fragment_movie_details, container, false);
        context = scrollView.getContext();
        Intent intent =  getActivity().getIntent();
        if(intent == null){
            return null;
        } else {
            if(intent.getExtras() != null){
                movieInfo = getActivity().getIntent().getExtras().getParcelable(MOVIE_PARCEL);
            } else if(getArguments() != null){
                movieInfo = getArguments().getParcelable(MOVIE_PARCEL);
            }

            ButterKnife.bind(this, scrollView);
            if(movieInfo != null){
                populateViews();
                initFavoritesButton(movieInfo.is_favorite, movieInfo.id);
            }

            return scrollView;

        }


    }


    public void initFavoritesButton(Boolean isFavorite, Integer movieId){
        if(Objects.equals(movieInfo.id, movieId)){
            Button btnStart = (Button) scrollView.findViewById(R.id.toggleFavorites);
            if(isFavorite){
                btnStart.setText(R.string.remove_fav);
            }
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onToggleFavorite(view);
                }
            });
        }
    }


    private void populateViews() {
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


    public void onToggleFavorite(View view) {
        Log.d(TAG, movieInfo.id.toString());
        if(movieInfo.is_favorite){
            ((AppCompatButton) view).setText(context.getResources().getString(R.string.add_fav));
        } else {
            ((AppCompatButton) view).setText(context.getResources().getString(R.string.remove_fav));
        }
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),
                        FavoriteMoviesContract.FavoritesEntry.CONTENT_URI,
                        new String[]{FavoriteMoviesContract.FavoritesEntry._ID},
                        FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(movieInfo.id)},
                        null            // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "loadFinished in DetailsFragment");

        long movieId;

        if (data.moveToFirst()) {
            int favoriteIdIndex = data.getColumnIndex(FavoriteMoviesContract.FavoritesEntry._ID);
            movieId = data.getInt(favoriteIdIndex);

            String mSelectionClause = FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID +  "= ?";
            String[] mSelectionArgs = {String.valueOf(movieInfo.id)};

            int mRowsDeleted = 0;

            mRowsDeleted = context.getContentResolver().delete(
                    FavoriteMoviesContract.FavoritesEntry.CONTENT_URI,   // the user dictionary content URI
                    mSelectionClause,                    // the column to select on
                    mSelectionArgs                      // the value to compare to
            );

        } else {
            ContentValues favoritesValues = new ContentValues();

            favoritesValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID , movieInfo.id);
            favoritesValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_IS_FAVORITE, true);


            Uri insertedUri = context.getContentResolver().insert(
                    FavoriteMoviesContract.FavoritesEntry.CONTENT_URI,
                    favoritesValues
            );

            movieId = ContentUris.parseId(insertedUri);
        }

        MovieGridFragment movieGridFragment =(MovieGridFragment) getFragmentManager().findFragmentById(R.id.container);
        if(movieGridFragment != null) {
            getLoaderManager().initLoader(0, null, movieGridFragment);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
