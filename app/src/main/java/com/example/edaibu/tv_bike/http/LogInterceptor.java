package com.example.edaibu.tv_bike.http;

import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lyn on 2017/4/13.
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Log.e("GAT",String.format("request %s on %s%n%s", request.url(), request.method(), request.headers()));


        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String body = response.body().string();
        Log.e("GAT",String.format("response %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, body));
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), body)).build();
    }
}
