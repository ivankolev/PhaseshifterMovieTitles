package com.phaseshiftlab.phaseshiftermovietitles.first.parcels;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable {
    public Integer id;

    public Boolean adult;
    public String backdrop_path;
    public Integer budget;
    public String homepage;
    public String imdb_id;
    public String original_title;
    public String overview;
    public Double popularity;
    public String poster_path;
    public String release_date;
    public Integer revenue;
    public Integer runtime;
    public String tagline;
    public String title;
    public Double vote_average;
    public Integer vote_count;
    public Boolean is_favorite = false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.adult);
        dest.writeString(this.backdrop_path);
        dest.writeValue(this.budget);
        dest.writeString(this.homepage);
        dest.writeString(this.imdb_id);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeValue(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeValue(this.revenue);
        dest.writeValue(this.runtime);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeValue(this.vote_average);
        dest.writeValue(this.vote_count);
        dest.writeValue(this.is_favorite);
    }

    public MovieInfo() {
    }

    protected MovieInfo(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.backdrop_path = in.readString();
        this.budget = (Integer) in.readValue(Integer.class.getClassLoader());
        this.homepage = in.readString();
        this.imdb_id = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.revenue = (Integer) in.readValue(Integer.class.getClassLoader());
        this.runtime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tagline = in.readString();
        this.title = in.readString();
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.is_favorite = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
