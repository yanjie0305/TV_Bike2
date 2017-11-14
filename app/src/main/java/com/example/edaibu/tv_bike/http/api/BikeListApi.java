package com.example.edaibu.tv_bike.http.api;

import com.example.edaibu.tv_bike.bean.BikeInfo;
import com.example.edaibu.tv_bike.constant.HttpConstant;
import com.example.edaibu.tv_bike.http.req.Req;
import com.example.edaibu.tv_bike.util.Util;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by lyn on 2017/4/17.
 */
public interface BikeListApi {
    @POST(HttpConstant.BIKELIST)
    Call<BikeInfo> getBikeList(@Body Req req);



}
