package com.example.edaibu.tv_bike.http;

import android.os.Handler;

import com.example.edaibu.tv_bike.bean.NameBean;
import com.example.edaibu.tv_bike.constant.Constant;
import com.example.edaibu.tv_bike.constant.HttpConstant;
import com.example.edaibu.tv_bike.http.api.NameApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${gyj} on 2017/6/20.
 */

public class NameHttp extends BaseRequst {

    /**
     * 网络请求
     *
     * @param handler
     */
    public static void getName(String type , final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        Http.baseUrl = HttpConstant.IP;
        Http.getRetrofit().create(NameApi.class).getName(map).enqueue(new Callback<NameBean>() {
            @Override
            public void onResponse(Call<NameBean> call, Response<NameBean> response) {
                sendMessage(handler, Constant.GET_NAME_SUCCESS, response.body());
            }

            @Override
            public void onFailure(Call<NameBean> call, Throwable t) {
                sendMessage(handler, Constant.REQUST_ERROR, null);
            }
        });
    }
}
