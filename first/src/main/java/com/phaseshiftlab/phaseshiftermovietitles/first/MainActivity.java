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
import android.view.*;
import android.widget.Toast;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity {

    Fragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mainFragment = new PlaceholderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit();
        } else {
            mainFragment = getFragmentManager().getFragment(
                    savedInstanceState, "mainFragment");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getFragmentManager().putFragment(outState, "mainFragment", mainFragment);
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

        private final Boolean FETCH_LOCAL = false;
        private String BASE_URL;
        private String API_KEY;
        private String sortBy;
        private MovieInfoResponse movieInfo;
        private MovieInfoAdapter movieInfoAdapter = new MovieInfoAdapter();
        private EndlessRecyclerViewAdapter recyclerViewAdapter;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            this.sortBy = getArguments() != null ? getArguments().getString("sortBy") : "popularity.desc";

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            initValues(rootView.getContext().getResources());
            final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);

            rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            rv.setItemAnimator(new DefaultItemAnimator());

            recyclerViewAdapter = createRecyclerViewAdapter(rv, rv.getContext(), movieInfoAdapter);

            if(savedInstanceState == null){
                fetchMovieData(rv, rootView.getContext());
            }

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            final RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.rv);
            if (savedInstanceState != null) {
                //Restore the fragment's state here
                movieInfo = savedInstanceState.getParcelable("movieInfo");
                assert movieInfo != null;
                movieInfoAdapter.appendItems(movieInfo.results);
                rv.setAdapter(recyclerViewAdapter);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            //Save the fragment's state here
            outState.putParcelable("movieInfo", movieInfo);
        }
        private void initValues(Resources resources){
            this.BASE_URL = resources.getString(R.string.base_url);
            this.API_KEY = resources.getString(R.string.tmdb_api_key);
        }

        private EndlessRecyclerViewAdapter createRecyclerViewAdapter(final RecyclerView rv, final Context ctx, MovieInfoAdapter movieInfoAdapter){
              return new EndlessRecyclerViewAdapter(ctx, movieInfoAdapter, new EndlessRecyclerViewAdapter.RequestToLoadMoreListener() {
                  @Override
                  public void onLoadMoreRequested() {
                      fetchMovieData(rv, ctx);
                  }
              });
        }

        private void fetchMovieData(final RecyclerView rv, final Context ctx){
            TheMovieDbService client = ServiceGenerator.createService(TheMovieDbService.class, this.BASE_URL, FETCH_LOCAL, ctx);
            client.discover(this.sortBy, this.API_KEY, getPage(), new Callback<MovieInfoResponse>() {
                @Override
                public void success(MovieInfoResponse movieInfoResponse, Response response) {
                    // here you do stuff with returned tasks
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

        private int getPage(){
            if(movieInfo == null){
                return 1;
            }
            else {
                return movieInfoAdapter.getItemCount()/20 + 1;
            }
        }
    }
}

