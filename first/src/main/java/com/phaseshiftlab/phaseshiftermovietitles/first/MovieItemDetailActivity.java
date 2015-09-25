package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static android.widget.Toast.makeText;


public class MovieItemDetailActivity extends AppCompatActivity implements MovieDetailsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item_detail);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.movie_detail_container, new MovieDetailsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void toggleFavorite(View view) {
        makeText(view.getContext(), view.getContext().getResources().getString(R.string.toggle_add_to_fav), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
