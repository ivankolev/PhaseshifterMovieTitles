package com.phaseshiftlab.phaseshiftermovietitles.first.parcels;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReviews implements Parcelable {
    public String id;
    public String author;
    public String content;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    public MovieReviews() {
    }

    protected MovieReviews(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<MovieReviews> CREATOR = new Parcelable.Creator<MovieReviews>() {
        public MovieReviews createFromParcel(Parcel source) {
            return new MovieReviews(source);
        }

        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };
}
