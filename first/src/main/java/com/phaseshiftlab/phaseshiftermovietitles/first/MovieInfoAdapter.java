package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by phaseshiftlab on 8/23/2015. Using template from http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
 */
public class MovieInfoAdapter extends RecyclerView.Adapter<MovieInfoAdapter.ViewHolder> {
    private static final String MOVIE_PARCEL = "com.phaseshiftlab.phaseshiftermovietitles.first.MovieInfo";
    private List<MovieInfo> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int id = -1;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.iv);
        }
    }

    public MovieInfoAdapter() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
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
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MovieItemDetailActivity.class);
                intent.putExtra(MOVIE_PARCEL, mDataset.get(position));
                view.getContext().startActivity(intent);
            }

        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
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

}