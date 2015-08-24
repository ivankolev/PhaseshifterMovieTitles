package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popularity) {
            sortMovies(new PlaceholderFragment(), "popularity.desc");
            return true;
        } else if (id == R.id.action_sort_rating) {
            sortMovies(new PlaceholderFragment(), "vote_average.desc");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortMovies(Fragment fragment, String sortBy){

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("sortBy", sortBy);
        fragment.setArguments(args);
        FragmentTransaction transaction;
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final Boolean FETCH_LOCAL = false;
        private String BASE_URL;
        private String API_KEY;
        private String sortBy;
        private MovieInfoResponse movieInfo;

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            this.sortBy = getArguments() != null ? getArguments().getString("sortBy") : "popularity.desc";

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            initValues(rootView.getContext().getResources());
            final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);

            rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rv.setItemAnimator(new DefaultItemAnimator());


            fetchMovieData(rv, rootView.getContext());

            return rootView;
        }

        private void initValues(Resources resources){
            this.BASE_URL = resources.getString(R.string.base_url);
            this.API_KEY = resources.getString(R.string.tmdb_api_key);
        }

        private void fetchMovieData(final RecyclerView rv, Context ctx){
            TheMovieDbService client = ServiceGenerator.createService(TheMovieDbService.class, this.BASE_URL, this.FETCH_LOCAL, ctx);

            // Fetch and print a list of the contributors to this library.
            client.discover(this.sortBy, this.API_KEY, new Callback<MovieInfoResponse>() {
                @Override
                public void success(MovieInfoResponse movieInfoResponse, Response response) {
                    // here you do stuff with returned tasks
                    movieInfo = movieInfoResponse;
                    rv.setAdapter(new MovieInfoAdapter(movieInfo.results));

                    Log.d("RETURN", movieInfoResponse.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("RETURN", error.toString());
                }
            });
        }
    }
}

