package com.example.edaibu.tv_bike.http;

import android.os.Handler;
import android.util.Log;


import com.example.edaibu.tv_bike.MainActivity;
import com.example.edaibu.tv_bike.bean.BikeInfo;
import com.example.edaibu.tv_bike.constant.Constant;
import com.example.edaibu.tv_bike.constant.HttpConstant;
import com.example.edaibu.tv_bike.http.api.BikeListApi;
import com.example.edaibu.tv_bike.http.req.Req;
import com.example.edaibu.tv_bike.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${gyj} on 2017/6/20.
 */

public class BikeInfoHttp extends BaseRequst {

    /**
     * 网络请求
     *
     * @param handler
     */
    public static void getBikeList(String companyNo, final Handler handler) {
        Req s = new Req();
        s.setCompanyNo(companyNo);
        Http.baseUrl = HttpConstant.BIKELIST_IP;
        Http.getRetrofit().create(BikeListApi.class).getBikeList(s).enqueue(new Callback<BikeInfo>() {
            @Override
            public void onResponse(Call<BikeInfo> call, Response<BikeInfo> response) {
                sendMessage(handler, Constant.GET_BIKELIST_SUCCESS, response.body());
            }
            @Override
            public void onFailure(Call<BikeInfo> call, Throwable t) {
                sendMessage(handler, Constant.REQUST_ERROR, null);
            }
        });
    }
}
