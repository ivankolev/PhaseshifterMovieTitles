package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.*;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.phaseshiftlab.phaseshiftermovietitles.first.data.FavoriteMoviesContract;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieInfo;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieRelatedVideos;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieRelatedVideosResponse;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.Objects;

import static android.widget.Toast.makeText;


public class MovieDetailsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "MovieDetailsFragment";
    protected static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieInfo";
    private static final int URL_LOADER = 1;
    private MovieInfo movieInfo = null;

    @Bind(R.id.originalTitle) TextView originalTitle;
    @Bind(R.id.moviePosterThumb) ImageView moviePosterThumb;
    @Bind(R.id.plotSynopsis) TextView plotSynopsis;
    @Bind(R.id.userRating) TextView userRating;
    @Bind(R.id.releaseDate) TextView releaseDate;
    @Bind(R.id.videosListView) ListView videosListView;

    private Context context;
    private ScrollView scrollView;
    private String BASE_URL;
    private String API_KEY;

    private MovieRelatedVideosResponse movieRelatedVideosResponse;
    private MovieRelatedVideosAdapter relatedVideosAdapter;

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
                initValues(scrollView.getContext().getResources());
                populateViews();
                initFavoritesButton(movieInfo.is_favorite, movieInfo.id);
                fetchTrailersData();
                setVideosClickHandler();
            }
            return scrollView;
        }


    }

    private void setVideosClickHandler() {
        videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieRelatedVideos video = movieRelatedVideosResponse.results.get(position);
                if(video.site.equalsIgnoreCase("youtube")){
                    watchYoutubeVideo(video.key);
                }
            }
        });
    }

    //    Credit: http://stackoverflow.com/a/12439378
    private void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            startActivity(intent);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            movieRelatedVideosResponse = savedInstanceState.getParcelable("movieRelatedVideos");
            assert movieRelatedVideosResponse != null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putParcelable("movieRelatedVideos", movieRelatedVideosResponse);
    }

    private void fetchTrailersData() {
        ServiceGenerator.createService(TheMovieDbService.class, this.BASE_URL, context).videos(movieInfo.id.toString(), this.API_KEY, new Callback<MovieRelatedVideosResponse>(){

            @Override
            public void success(MovieRelatedVideosResponse relatedVideosResponse, Response response) {
                movieRelatedVideosResponse = relatedVideosResponse;
                relatedVideosAdapter = new MovieRelatedVideosAdapter(context, movieRelatedVideosResponse.results);
                videosListView.setAdapter(relatedVideosAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                makeText(context, context.getResources().getString(R.string.retrofit_error_message), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initValues(Resources resources) {
        this.BASE_URL = resources.getString(R.string.base_url);
        this.API_KEY = resources.getString(R.string.tmdb_api_key);
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
