package com.phaseshiftlab.phaseshiftermovietitles.first;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TheMovieDbService {
    @GET("/discover/movie")
    void discover(@Query("sort_by") String sortParam, @Query("api_key") String apiKey, @Query("page") Integer page, Callback<MovieInfoResponse> cb);
}
