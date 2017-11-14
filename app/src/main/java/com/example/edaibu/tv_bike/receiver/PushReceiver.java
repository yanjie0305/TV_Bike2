package com.example.edaibu.tv_bike.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.edaibu.tv_bike.bean.MessageBean;
import com.example.edaibu.tv_bike.service.MqttService;
import com.example.edaibu.tv_bike.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${gyj} on 2017/11/7.
 */

public class PushReceiver extends BroadcastReceiver {
    //消息的网络请求返回
    public static final String MSG = "msg";
    private Context context;
    private String msgId;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (MqttService.NOTIFICATION_RECEIVED_PROXY.equals(intent.getAction())) {
            String data = intent.getStringExtra(MqttService.EXTRA_DATA);
            switch (getAction(data)) {
                case MSG:
//                    parseMsg(data);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 解析消息
     */
//    private void parseMsg(String data) {
//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            msgId = jsonObject.getString("id");
//            LogUtils.e("获取到推送的id=" + msgId);
//            MessageBean messageBean = querMsgById(msgId);
//            if (messageBean != null) {
//                return;
//            }
//            OrderHttp.getMsgDetail(msgId, handler);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * 获取推送消息意图
     **/
    private String getAction(String data) {
        String action = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            action = jsonObject.getString("action");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return action;
    }
}
