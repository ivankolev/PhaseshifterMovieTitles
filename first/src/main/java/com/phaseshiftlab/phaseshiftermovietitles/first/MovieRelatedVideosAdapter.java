package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieRelatedVideos;

import java.util.List;

/**
 * Created by phaseshiftlab on 10/5/2015.
 */
public class MovieRelatedVideosAdapter extends BaseAdapter{
    private List<MovieRelatedVideos> videosDataset;
    private Context context;

    public MovieRelatedVideosAdapter(Context context, List<MovieRelatedVideos> videosDataset) {

        // super(context, R.layout.movie_video_item, objects);
        this.videosDataset = videosDataset;
        this.context = context;
    }

    @Override
    public int getCount() {
        return videosDataset.size();
    }

    @Override
    public Object getItem(int position) {
        MovieRelatedVideos movieRelatedVideos =  videosDataset.get(position)  ;
        return movieRelatedVideos;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MovieRelatedVideos relatedVideos = (MovieRelatedVideos) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_video_item, parent, false);
            viewHolder.video_name = (TextView) convertView.findViewById(R.id.video_name);
            viewHolder.video_site = (TextView) convertView.findViewById(R.id.video_site);
            viewHolder.video_type = (TextView) convertView.findViewById(R.id.video_type);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.video_name.setText("Name: " + relatedVideos.name);
        viewHolder.video_type.setText("Type: " + relatedVideos.type);
        viewHolder.video_site.setText("Site: " + relatedVideos.site);

        // Return the completed view to render on screen
        return convertView;
    }

    public class ViewHolder extends MovieRelatedVideos {
        TextView video_name;
        TextView video_type;
        TextView video_site;
    }
}
