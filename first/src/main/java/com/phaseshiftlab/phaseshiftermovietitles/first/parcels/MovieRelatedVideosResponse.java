package com.phaseshiftlab.phaseshiftermovietitles.first.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieRelatedVideosResponse implements Parcelable {
    public List<MovieRelatedVideos> results;
    int id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.results);
        dest.writeInt(this.id);
    }

    public MovieRelatedVideosResponse() {
    }

    protected MovieRelatedVideosResponse(Parcel in) {
        this.results = in.createTypedArrayList(MovieRelatedVideos.CREATOR);
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<MovieRelatedVideosResponse> CREATOR = new Parcelable.Creator<MovieRelatedVideosResponse>() {
        public MovieRelatedVideosResponse createFromParcel(Parcel source) {
            return new MovieRelatedVideosResponse(source);
        }

        public MovieRelatedVideosResponse[] newArray(int size) {
            return new MovieRelatedVideosResponse[size];
        }
    };
}
