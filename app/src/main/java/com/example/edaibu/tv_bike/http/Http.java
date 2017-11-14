package com.example.edaibu.tv_bike.http;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.example.edaibu.tv_bike.constant.HttpConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lyn on 2017/4/13.
 */

public class Http {
    public  static String baseUrl ="";
//    public static String baseUrl = HttpConstant.IP;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static OkHttpClient getOkHttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.writeTimeout(15, TimeUnit.SECONDS);
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.addInterceptor(new LogInterceptor());
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }


    public static synchronized Retrofit getRetrofit() {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(baseUrl);
            builder.addConverterFactory(GsonConverterFactory.create());
            builder.callFactory(getOkHttp());
            retrofit = builder.build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitNoInterceptor() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(GsonConverterFactory.create());
        OkHttpClient.Builder okBuilder = new OkHttpClient().newBuilder();
        okBuilder.connectTimeout(15, TimeUnit.SECONDS);
        okBuilder.writeTimeout(15, TimeUnit.SECONDS);
        okBuilder.readTimeout(15, TimeUnit.SECONDS);
//        okBuilder.addInterceptor(new LogInterceptor());
        builder.callFactory(okBuilder.build());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    /**
     * 上传文件
     */
    public static void upLoadFile(String url, String fileKey, File file, Map<String, String> map, Callback callback) {
//        map = ParameterUtils.getInstance().getParameter(map,2);
        //创建RequestBody
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(null!=file){
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart(fileKey, file.getName(), body);
        }
        if (map != null) {
            for (String key : map.keySet()) {
                builder.addFormDataPart(key, map.get(key));
            }
        }
        RequestBody requestBody = builder.build();
        //创建Request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = new OkHttpClient.Builder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(callback);
    }


    /**
     * 下载文件
     */
    public static void dowload(String dowloadUrl, final String outPath, final Handler mHandler, final Callback callback) {
        Request request = new Request.Builder().url(dowloadUrl).build();
        Call call = new OkHttpClient.Builder().readTimeout(60 * 5, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (callback != null) {
                        callback.onResponse(call, response);
                    }
                    return;
                }
                InputStream is = response.body().byteStream();
                long length = response.body().contentLength();
                File file = new File(outPath);
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte[] buf = new byte[1024];
                long l = 0;
                final NumberFormat format = NumberFormat.getPercentInstance();// 获取格式化类实例
                while ((len = is.read(buf)) != -1) {
                    l += len;
                    fos.write(buf, 0, len);
                    if (null != mHandler) {
                        format.setMinimumFractionDigits(0);// 设置小数位
                        Message msg = new Message();
//                        msg.what = HandlerConstant1.DOWNLOAD_PRORESS;
                        msg.obj = format.format((float) l / (float) length);
                        mHandler.sendMessage(msg);
                    }
                }
                fos.flush();
                is.close();
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

        });
    }


    /**
     * get请求
     * @param url
     */
    public static void getMonth(String url, final Handler mHandler, final int index){
        try {
            if(TextUtils.isEmpty(url)){
                return;
            }
            Map<String, String> requstMap = new HashMap<>();
//            requstMap = ParameterUtils.getInstance().getParameter(requstMap);
            if(null==requstMap){
                return;
            }
            StringBuffer stringBuffer=new StringBuffer();
            Iterator entries = requstMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String)entry.getKey();
                String value = (String) entry.getValue();
                stringBuffer.append("&"+key+"="+value);
            }

            url+=stringBuffer.toString();
//            LogUtils.e(url+"...................");
            Request request = new Request.Builder().url(url).build();
            Call call = new OkHttpClient.Builder().readTimeout(60 * 5, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    sendMessage(null,mHandler,index);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.body()!=null){
                        sendMessage(response.body().string(),mHandler,index);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void sendMessage(Object o, Handler mHandler, int index){
        Message message = Message.obtain();
        if (o == null) {
//            message.what = HandlerConstant.REQUST_ERROR;
            mHandler.sendMessage(message);
            return;
        }
        message.what = index;
        message.obj = o;
        mHandler.sendMessage(message);
    }
}
