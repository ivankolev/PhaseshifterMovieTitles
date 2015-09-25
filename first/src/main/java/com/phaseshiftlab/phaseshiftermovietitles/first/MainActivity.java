package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.Toast;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity implements MovieDetailsFragment.OnFragmentInteractionListener {

    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MDFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        MovieGridFragment movieGridFragment = (MovieGridFragment) getFragmentManager().findFragmentById(R.id.container);

        if (id == R.id.action_sort_popularity) {
            movieGridFragment.onChangeSortingOrder("popularity.desc");
            return true;
        } else if (id == R.id.action_sort_rating) {
            movieGridFragment.onChangeSortingOrder("vote_average.desc");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class MovieGridFragment extends Fragment {

        private final Boolean FETCH_LOCAL = false;
        private String BASE_URL;
        private String API_KEY;
        private String sortBy;
        private MovieInfoResponse movieInfo;
        private MovieInfoAdapter movieInfoAdapter = new MovieInfoAdapter();
        private EndlessRecyclerViewAdapter recyclerViewAdapter;
        private RecyclerView recyclerView;

        public MovieGridFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            this.sortBy = getArguments() != null ? getArguments().getString("sortBy") : "popularity.desc";

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            initValues(rootView.getContext().getResources());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

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
            sortBy = sortOrder;
            movieInfoAdapter.clear();
            fetchMovieData(recyclerView, recyclerView.getContext());

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
            ServiceGenerator.createService(TheMovieDbService.class, this.BASE_URL, FETCH_LOCAL, ctx).discover(this.sortBy, this.API_KEY, getPage(), new Callback<MovieInfoResponse>() {
                @Override
                public void success(MovieInfoResponse movieInfoResponse, Response response) {
                    movieInfo = movieInfoResponse;
                    movieInfoAdapter.appendItems(movieInfo.results);
                    rv.setAdapter(createRecyclerViewAdapter(rv, rv.getContext(), movieInfoAdapter));
                    recyclerViewAdapter.onDataReady(true);
                    rv.scrollToPosition(movieInfoAdapter.getItemCount() - 22);
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
    }
}

