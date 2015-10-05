package com.phaseshiftlab.phaseshiftermovietitles.first.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MovieInfoResponse implements Parcelable {
    public List<MovieInfo> results;
    int page;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeInt(this.page);
    }

    public MovieInfoResponse() {
    }

    protected MovieInfoResponse(Parcel in) {
        this.results = in.createTypedArrayList(MovieInfo.CREATOR);
        this.page = in.readInt();
    }

    public static final Parcelable.Creator<MovieInfoResponse> CREATOR = new Parcelable.Creator<MovieInfoResponse>() {
        public MovieInfoResponse createFromParcel(Parcel source) {
            return new MovieInfoResponse(source);
        }

        public MovieInfoResponse[] newArray(int size) {
            return new MovieInfoResponse[size];
        }
    };
}
