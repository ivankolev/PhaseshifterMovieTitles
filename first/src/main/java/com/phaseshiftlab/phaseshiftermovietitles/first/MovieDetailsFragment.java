package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.phaseshiftlab.phaseshiftermovietitles.first.data.FavoriteMoviesContract;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {
    private static final String TAG = "MovieDetailsFragment";
    protected static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";
    private MovieInfo movieInfo = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.originalTitle) TextView originalTitle;
    @Bind(R.id.moviePosterThumb) ImageView moviePosterThumb;
    @Bind(R.id.plotSynopsis) TextView plotSynopsis;
    @Bind(R.id.userRating) TextView userRating;
    @Bind(R.id.releaseDate) TextView releaseDate;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ScrollView sv = (ScrollView) inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent intent =  getActivity().getIntent();
        if(intent == null){
            return null;
        } else {
            if(intent.getExtras() != null){
                movieInfo = getActivity().getIntent().getExtras().getParcelable(MOVIE_PARCEL);
            } else if(getArguments() != null){
                movieInfo = getArguments().getParcelable(MOVIE_PARCEL);
            }

            ButterKnife.bind(this, sv);
            if(movieInfo != null){
                populateViews();
            }
            return sv;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public long onToggleFavorite(View view) {
        Log.d(TAG, movieInfo.id.toString());
        long movieId;

        // First, check if the location with this city name exists in the db
        Cursor favoritesCursor = view.getContext().getContentResolver().query(
                FavoriteMoviesContract.FavoritesEntry.CONTENT_URI,
                new String[]{FavoriteMoviesContract.FavoritesEntry._ID},
                FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieInfo.id)},
                null);

        if (favoritesCursor.moveToFirst()) {
            int favoriteIdIndex = favoritesCursor.getColumnIndex(FavoriteMoviesContract.FavoritesEntry._ID);
            movieId = favoritesCursor.getInt(favoriteIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues favoritesValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            favoritesValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID , movieInfo.id);
            favoritesValues.put(FavoriteMoviesContract.FavoritesEntry.COLUMN_IS_FAVORITE, true);


            // Finally, insert location data into the database.
            Uri insertedUri = view.getContext().getContentResolver().insert(
                    FavoriteMoviesContract.FavoritesEntry.CONTENT_URI,
                    favoritesValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            movieId = ContentUris.parseId(insertedUri);
        }

        favoritesCursor.close();
        // Wait, that worked?  Yes!
        return movieId;


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}