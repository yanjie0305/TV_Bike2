package com.example.edaibu.tv_bike.http.api;

import com.example.edaibu.tv_bike.bean.NameBean;
import com.example.edaibu.tv_bike.constant.Constant;
import com.example.edaibu.tv_bike.constant.HttpConstant;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by lyn on 2017/4/17.
 */
public interface NameApi {
    @GET(HttpConstant.NAME)
    Call<NameBean> getName(@QueryMap Map<String, String> map);



}
