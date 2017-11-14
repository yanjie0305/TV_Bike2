package com.example.edaibu.tv_bike.constant;

/**
 * Created by ${gyj} on 2017/9/20.
 */

public class HttpConstant {

//    public static final String IP = "http://192.168.1.22:3033/";
    public static final String IP = "http://api-developer.zxbike.cn/";
    public static final String NAME = IP+"api/user/org";
//    public static final String BIKELIST_IP = "http://lot.v2.zxbike.top/";
    public static final String BIKELIST_IP = "http://lot.v2.zxbike.cc/";
    public static final String BIKELIST = BIKELIST_IP+"bike/company/area/list";
    /**
     * 推送正式主机域名
     */
//    public static final String HOST = "tcp://mqtt.zxbike.cc:1883";
    /**
     * 推送测试域名
     */
    public static final String HOST = "tcp://mqtt.zxbike.top:1883";
    /**
     * 推送客户端ID
     */
    public static final String CLIENT_ID = "edaibu_chewu_app";
    /**
     * 推送的用户名
     */
    public static final String UESR_NAME = "mosquitto";

    /**
     * 推送过来的消息详情
     **/
//    public static final String MSG_DETAIL = "msg/detail/v2";
    //7月3日变更
    public static final String MSG_DETAIL = "msg/detail";
}