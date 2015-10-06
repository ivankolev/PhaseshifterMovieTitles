package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.phaseshiftlab.phaseshiftermovietitles.first.data.FavoriteMoviesContract;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieInfo;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieInfoResponse;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.HashSet;

import static android.widget.Toast.makeText;

public class MovieGridFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private String BASE_URL;
    private String API_KEY;
    private String sortBy;
    private MovieInfoResponse movieInfo;
    private MovieInfoAdapter movieInfoAdapter;
    private EndlessRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private Integer NUMBER_OF_GRID_COLUMNS = 2; //TODO make it 3 columns on landscape
    private static final int URL_LOADER = 0;
    private HashSet<Integer> favoriteMoviesIdSet = new HashSet<>();

    public MovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.sortBy = getArguments() != null ? getArguments().getString("sortBy") : "popularity.desc";

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initValues(rootView.getContext().getResources());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NUMBER_OF_GRID_COLUMNS));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        movieInfoAdapter = new MovieInfoAdapter(this, rootView.getContext());
        recyclerViewAdapter = createRecyclerViewAdapter(recyclerView, rootView.getContext(), movieInfoAdapter);


        if (savedInstanceState == null) {
            fetchMovieData(recyclerView, rootView.getContext());
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            movieInfo = savedInstanceState.getParcelable("movieInfo");
            assert movieInfo != null;
            movieInfoAdapter.appendItems(movieInfo.results);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putParcelable("movieInfo", movieInfo);
    }

    public void onChangeSortingOrder(String sortOrder) {
        if(sortOrder.equalsIgnoreCase(MainActivity.SortMode.FAVORITES.get(MainActivity.SortMode.DESC))) {
             movieInfoAdapter.sortByFavorites();
        } else {
            sortBy = sortOrder;
            movieInfoAdapter.clear();
            fetchMovieData(recyclerView, recyclerView.getContext());
        }
    }

    private void initValues(Resources resources) {
        this.BASE_URL = resources.getString(R.string.base_url);
        this.API_KEY = resources.getString(R.string.tmdb_api_key);
    }

    private EndlessRecyclerViewAdapter createRecyclerViewAdapter(final RecyclerView rv, final Context ctx, MovieInfoAdapter movieInfoAdapter) {
        return new EndlessRecyclerViewAdapter(ctx, movieInfoAdapter, new EndlessRecyclerViewAdapter.RequestToLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                fetchMovieData(rv, ctx);
            }
        });
    }

    private void fetchMovieData(final RecyclerView rv, final Context ctx) {
        ServiceGenerator.createService(TheMovieDbService.class, this.BASE_URL, ctx).discover(this.sortBy, this.API_KEY, getPage(), new Callback<MovieInfoResponse>() {
            @Override
            public void success(MovieInfoResponse movieInfoResponse, Response response) {
                movieInfo = movieInfoResponse;
                movieInfoAdapter.appendItems(movieInfo.results);
                rv.setAdapter(createRecyclerViewAdapter(rv, rv.getContext(), movieInfoAdapter));
                recyclerViewAdapter.onDataReady(true);

                rv.scrollToPosition(movieInfoAdapter.getItemCount() - 22);

                MovieGridFragment movieGridFragment =(MovieGridFragment) getFragmentManager().findFragmentById(R.id.container);
                getLoaderManager().restartLoader(URL_LOADER, null, movieGridFragment);

                ((MovieGridFragment.OnFragmentInteractionListener) getActivity())
                        .onFragmentInteraction(movieInfo.results.get(0));
            }

            @Override
            public void failure(RetrofitError error) {
                makeText(ctx, ctx.getResources().getString(R.string.retrofit_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getPage() {
        return movieInfo == null ? 1 : movieInfoAdapter.getItemCount() / 20 + 1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                return new CursorLoader(
                        getActivity(),
                        FavoriteMoviesContract.FavoritesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("onLoadFinished", String.format("Cursor count: %d", data.getCount()));
        favoriteMoviesIdSet = new HashSet<>();
        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()){
            final Integer favoriteMovieId = data.getInt(data.getColumnIndex(FavoriteMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID));
            favoriteMoviesIdSet.add(favoriteMovieId);
        }
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) getFragmentManager().findFragmentById(R.id.movie_detail_container);
        movieInfoAdapter.appendFavoriteInfo(favoriteMoviesIdSet, movieDetailsFragment);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface ItemSelectedCallback {
        void onItemSelected(MovieInfo movieInfo);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(MovieInfo movieInfo);
    }
}
