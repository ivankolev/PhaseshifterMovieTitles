package com.phaseshiftlab.phaseshiftermovietitles.first.parcels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by phaseshiftlab on 10/5/2015.
 */
public class MovieRelatedVideos implements Parcelable {
    public String id;
    public String iso_6391_1;
    public String key;
    public String name;
    public String site;
    public String type;
    public Integer size;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso_6391_1);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.type);
        dest.writeValue(this.size);
    }

    public MovieRelatedVideos() {
    }

    protected MovieRelatedVideos(Parcel in) {
        this.id = in.readString();
        this.iso_6391_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.type = in.readString();
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieRelatedVideos> CREATOR = new Parcelable.Creator<MovieRelatedVideos>() {
        public MovieRelatedVideos createFromParcel(Parcel source) {
            return new MovieRelatedVideos(source);
        }

        public MovieRelatedVideos[] newArray(int size) {
            return new MovieRelatedVideos[size];
        }
    };
}
