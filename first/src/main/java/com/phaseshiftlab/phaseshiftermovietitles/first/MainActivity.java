package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private String BASE_URL;
        private String API_KEY;
        private MovieInfoResponse movieInfo;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final ImageView image = (ImageView) rootView.findViewById(R.id.imageView2);

            initValues(rootView.getContext().getResources());

            // Create a very simple REST adapter which points the GitHub API endpoint.
            TheMovieDbService client = ServiceGenerator.createService(TheMovieDbService.class, this.BASE_URL);

            // Fetch and print a list of the contributors to this library.
            client.discover("popularity.desc", this.API_KEY, new Callback<MovieInfoResponse>() {
                @Override
                public void success(MovieInfoResponse movieInfoResponse, Response response) {
                    // here you do stuff with returned tasks
                    movieInfo = movieInfoResponse;
                    Log.d("RETURN", movieInfoResponse.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("RETURN", error.toString());
                }
            });


            Picasso
                    .with(image.getContext())
                    .load("http://i.imgur.com/DvpvklR.png")
//                    .placeholder(R.drawable.abc_item_background_holo_light)   //TODO change to proper placeholder and error images
//                    .error(R.drawable.abc_btn_default_mtrl_shape)
//                    .fit()
//                    .centerInside()
                    .into(image);

            return rootView;
        }

        private void initValues(Resources resources){
            this.BASE_URL = resources.getString(R.string.base_url);
            this.API_KEY = resources.getString(R.string.tmdb_api_key);

        }
    }
}
