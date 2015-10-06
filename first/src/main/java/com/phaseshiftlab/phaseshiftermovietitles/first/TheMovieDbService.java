package com.phaseshiftlab.phaseshiftermovietitles.first;

import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieReviewsResponse;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieInfoResponse;
import com.phaseshiftlab.phaseshiftermovietitles.first.parcels.MovieRelatedVideosResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TheMovieDbService {
    @GET("/discover/movie")
    void discover(@Query("sort_by") String sortParam, @Query("api_key") String apiKey, @Query("page") Integer page, Callback<MovieInfoResponse> cb);

    @GET("/movie/{id}/videos")
    void videos(@Path("id") String id, @Query("api_key") String apiKey, Callback<MovieRelatedVideosResponse> cb);

    @GET("/movie/{id}/reviews")
    void reviews(@Path("id") String id, @Query("api_key") String apiKey, Callback<MovieReviewsResponse> cb);
}
