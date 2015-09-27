package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.widget.Toast.makeText;

public class MovieGridFragment extends Fragment {

    private final Boolean FETCH_LOCAL = false;
    private String BASE_URL;
    private String API_KEY;
    private String sortBy;
    private MovieInfoResponse movieInfo;
    private MovieInfoAdapter movieInfoAdapter;
    private EndlessRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private Integer NUMBER_OF_GRID_COLUMNS = 2;

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

        movieInfoAdapter = new MovieInfoAdapter(this);
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


    public interface ItemSelectedCallback {
        void onItemSelected(MovieInfo movieInfo);
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
        void onFragmentInteraction(MovieInfo movieInfo);
    }
}
