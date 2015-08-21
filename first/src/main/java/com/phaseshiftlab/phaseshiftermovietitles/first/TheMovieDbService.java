package com.phaseshiftlab.phaseshiftermovietitles.first;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import java.util.List;

public interface TheMovieDbService {
    @GET("/repos/{owner}/{repo}/contributors")
    void contributors(@Path("owner") String owner, @Path("repo") String repo, Callback<List<MovieInfo>> cb);
}
