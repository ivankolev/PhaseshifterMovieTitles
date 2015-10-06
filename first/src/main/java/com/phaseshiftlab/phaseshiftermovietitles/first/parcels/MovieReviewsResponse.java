package com.phaseshiftlab.phaseshiftermovietitles.first.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieReviewsResponse implements Parcelable {
    public List<MovieReviews> results;
    int id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeInt(this.id);
    }

    public MovieReviewsResponse() {
    }

    protected MovieReviewsResponse(Parcel in) {
        this.results = in.createTypedArrayList(MovieReviews.CREATOR);
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<MovieReviewsResponse> CREATOR = new Parcelable.Creator<MovieReviewsResponse>() {
        public MovieReviewsResponse createFromParcel(Parcel source) {
            return new MovieReviewsResponse(source);
        }

        public MovieReviewsResponse[] newArray(int size) {
            return new MovieReviewsResponse[size];
        }
    };
}
