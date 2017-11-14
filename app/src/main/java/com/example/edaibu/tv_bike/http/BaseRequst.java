package com.example.edaibu.tv_bike.http;

import android.os.Handler;
import android.os.Message;

import com.example.edaibu.tv_bike.constant.Constant;

public class BaseRequst {


    public static void sendMessage(Handler handler, int wat, Object obj) {
        Message message = Message.obtain();
        message.what = wat;
        message.obj = obj;
        if(obj==null){
            message.what = Constant.REQUST_ERROR;
        }
        handler.sendMessage(message);
    }


}
