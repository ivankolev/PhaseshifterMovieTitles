package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by phaseshiftlab on 8/23/2015. Using template from http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
 */
public class MovieInfoAdapter extends RecyclerView.Adapter<MovieInfoAdapter.ViewHolder> {
    private static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";
    private List<MovieInfo> mDataset;
    private MovieGridFragment movieGridFragment;
    private Context context;
    private View view;

    public void sortByFavorites() {
        Collections.sort(mDataset, new Comparator<MovieInfo>(){

            @Override
            public int compare(MovieInfo lhs, MovieInfo rhs) {
                return (lhs.is_favorite == rhs.is_favorite)? 0 : lhs.is_favorite? -1 : 1;
            }
        });
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int id = -1;
        public ImageView imageView;
        public TextView movieTextView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.iv);
            movieTextView = (TextView) v.findViewById(R.id.movie_favorite_text);
        }
    }

    public MovieInfoAdapter(MovieGridFragment movieGridFragment, Context context) {
        this.movieGridFragment = movieGridFragment;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final MovieInfo movieInfo = mDataset.get(position);
        Context context = holder.imageView.getContext();
        String path = context.getResources().getString(R.string.image_url) + "/" +
                context.getResources().getString(R.string.width_342) + "/" +
                movieInfo.poster_path;
        Picasso.with(context)
                .load(path)
                .fit()
                .into(holder.imageView);
        String favoriteBadge = movieInfo.is_favorite ? "\u2605" : "";
        holder.movieTextView.setText(favoriteBadge);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   callClickItem(position);
            }

        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    public void callClickItem(Integer position){
        ((MovieGridFragment.ItemSelectedCallback) movieGridFragment.getActivity())
                .onItemSelected(mDataset.get(position));
    }

    public void appendItems(List<MovieInfo> items) {
        if(mDataset != null){
            mDataset.addAll(items);
        }  else {
            mDataset = items;
        }
        int count = getItemCount();
        notifyItemRangeInserted(count, items.size());
    }

    public void clear(){
        mDataset.clear();
        mDataset = null;
    }


    public void appendFavoriteInfo(HashSet<Integer> favoriteIds, MovieDetailsFragment movieDetailsFragment){
          if(mDataset != null) {
              if(favoriteIds != null && favoriteIds.size() != 0) {
                  for (MovieInfo movieInfo : mDataset) {
                      movieInfo.is_favorite = favoriteIds.contains(movieInfo.id);
                      if(movieDetailsFragment != null){
                          movieDetailsFragment.initFavoritesButton(movieInfo.is_favorite, movieInfo.id);
                      }
                  }
                  notifyItemRangeChanged(0, mDataset.size() - 1);
              }
          }
    }
}