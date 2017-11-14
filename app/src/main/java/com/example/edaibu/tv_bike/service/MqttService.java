package com.example.edaibu.tv_bike.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.example.edaibu.tv_bike.constant.HttpConstant;
import com.example.edaibu.tv_bike.util.LogUtils;
import com.example.edaibu.tv_bike.util.SPUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * MQTT接收推送的service
 **/
public class MqttService extends Service {
    public static final String TAG = MqttService.class.getSimpleName();
    /**
     * 接收到推送过来的消息
     */
    public static final String NOTIFICATION_RECEIVED_PROXY = "notification_received_proxy";
    /**
     * 消息中的数据
     */
    public static final String EXTRA_DATA = "extra_data";
    /**
     * 存放用户唯一标示
     */
    public static final String USER_FLG = "user_flg";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private static MqttAndroidClient client;
    private static MqttConnectOptions conOpt;
    private String host = HttpConstant.HOST;
    private String userName = HttpConstant.UESR_NAME;
    private String passWord = "";
    /**
     * 推送消息的标识
     **/
    private static String myTopic = "";
    private static String clientId = HttpConstant.CLIENT_ID;
    private static final String ACTION_START = clientId + ".START";
    private static final String ACTION_STOP = clientId + ".STOP";
    //超时时间（秒）
    private int CONNECTION_TIMEOUT = 10;
    //心跳包间隔（秒）
    private int KEEP_ALIVE_INTERVAL = 30;
    private static boolean mStarted = false;
    public SPUtil spUtil;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("onCreate");
        spUtil = new SPUtil(this);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取手机IMEI
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        clientId = clientId + telephonyManager.getDeviceId();
        LogUtils.e("clientId=" + clientId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("onStartCommand");
        if (intent == null) {
            return START_STICKY;
        }
        if (ACTION_START.equals(intent.getAction())) {
            myTopic = spUtil.getString(USER_FLG);
            if (TextUtils.isEmpty(myTopic)) {
                return START_STICKY;
            }
            init();
        } else if (ACTION_STOP.equals(intent.getAction())) {
            LogUtils.e("stopSelf");
            disconnect();
            myTopic = "";
            mStarted = false;
            // this.stopSelf();
            return START_NOT_STICKY;
        }
        return START_STICKY;
    }


    // Static method to start the service
    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, MqttService.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }

    // Static method to stop the service
    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, MqttService.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }

    public static void publish(String msg) {
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        try {
            client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private synchronized void init() {
        if (mStarted) {
            return;
        }
        mStarted = true;
        LogUtils.e("init");
        initConnection();
    }

    private void initConnection() {
        // 服务器地址（协议+地址+端口号）
        client = new MqttAndroidClient(this, host, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);
        conOpt = new MqttConnectOptions();
        //设置自动重连
        //conOpt.setAutomaticReconnect(true);
        // 清除缓存
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(CONNECTION_TIMEOUT);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());
        LogUtils.e("myTopic= " + myTopic);
        conOpt.setWill(myTopic, clientId.getBytes(), 0, false);
        doClientConnection(conOpt);
    }


    private void reconnectIfNecessary() {
        LogUtils.e("重连 ");
        if (isNetWork()) {
            doClientConnection(conOpt);
            LogUtils.e("网络链接正常：");
        } else {
            LogUtils.e("没有网络：");
            //disconnect();
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.e("onDestroy");
        disconnect();
        super.onDestroy();
    }

    private void disconnect() {
        try {
            if (client != null) {
                client.disconnect();
                client = null;
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接MQTT服务器
     */
    private synchronized void doClientConnection(MqttConnectOptions mqttConnectOptions) {
        if (client != null && !client.isConnected() && isNetWork() && !TextUtils.isEmpty(myTopic)) {
            try {
                client.connect(mqttConnectOptions, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            if (client == null) {
                return;
            }
            LogUtils.e("连接成功 ");
            try {
                // 订阅myTopic话题
                LogUtils.e("myTopic= " + myTopic);
                client.subscribe(myTopic, 2);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
            LogUtils.e("连接失败 ");
            reconnectIfNecessary();
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String msg = new String(message.getPayload());
            LogUtils.e("messageArrived:" + msg);
            if ("close".equals(msg)) {
                return;
            }
            if (topic.equals(myTopic)) {
                Intent intent = new Intent();
                intent.setAction(NOTIFICATION_RECEIVED_PROXY);
                //msg = "{\"id\"=\"58d9f7c1b5e27f2f1b8ea170\",\"action\":\"msg\"} ";
                intent.putExtra(EXTRA_DATA, msg);
                sendBroadcast(intent);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            LogUtils.e("messageArrived:" + arg0.toString());

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            LogUtils.e("push链接断开:");
            reconnectIfNecessary();
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isNetWork() {
        info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected()) ? true : false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}