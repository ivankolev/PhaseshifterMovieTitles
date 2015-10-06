package com.phaseshiftlab.phaseshiftermovietitles.first;

import android.content.Context;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class ServiceGenerator {

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Context ctx) {
        RestAdapter.Builder builder;
        builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(new OkHttpClient()));
        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}