package com.example.edaibu.tv_bike;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edaibu.tv_bike.adapter.MyAdapter;
import com.example.edaibu.tv_bike.bean.BikeInfo;
import com.example.edaibu.tv_bike.bean.NameBean;
import com.example.edaibu.tv_bike.constant.Constant;
import com.example.edaibu.tv_bike.fragment.MapFragment;
import com.example.edaibu.tv_bike.http.BikeInfoHttp;
import com.example.edaibu.tv_bike.http.NameHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private  final String TAG = MainActivity.this.getClass().getSimpleName();
    public static String ALL = "";
    public static String ZHIYING = "02";
    public static String LIANYING = "03";
    public static String JINGQU = "04";
    private String type, companyId;
    public DrawerLayout drawerLayout;
    private MapFragment mapFragment;
    private Spinner spinner1, spinner2;
    private MyAdapter myAdapter;
    List<String> list = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    private volatile boolean isRefreshUI = true;
    private static final int REFRESH_TIME = 2000;   //5秒刷新一次
    private NameBean nameBean;
    public List<NameBean.Data> dataLists;
    private BikeInfo bikeInfo;
    public List<BikeInfo.DataBean> listBike;
    private MyAdapter adapter;
    boolean isInitSp = true;
    private Handler mHandler = new Handler() {
        //刷新界面的Handler
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //每五秒刷新一次界面
                case Constant.REFRESH_UI:
                    if (isRefreshUI && !TextUtils.isEmpty(companyId)) {
//                        Log.e("MAIN", "--" + companyId);
                        BikeInfoHttp.getBikeList(companyId, mHandler);
                    }
                    break;
                case Constant.GET_NAME_SUCCESS:
                    nameBean = (NameBean) msg.obj;

                    if (nameBean.getData() == null) {
                        return;
                    }
                    if (nameBean.getStatus() == 200) {
                        dataLists = nameBean.getData();
                        initSpinner2Data();
                    }
                    break;

                case Constant.GET_BIKELIST_SUCCESS:
                    clearTask();

                    bikeInfo = (BikeInfo) msg.obj;
                    if (null == bikeInfo) {
                        return;
                    }
                    if (bikeInfo.getCode() == 200 && bikeInfo.getData() != null) {
                        //更新地图界面mark
                        if (null!=listBike&&listBike.size()>0){
                            listBike.clear();
                        }
                        listBike = bikeInfo.getData();
                        Log.e(TAG,"number="+bikeInfo.getData().size());
                        getBikeInfoSuccess();
                    }
                    break;

                case Constant.REQUST_ERROR:
                    Toast.makeText(MainActivity.this, nameBean.getMsg(), Toast.LENGTH_SHORT);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private ProgressDialog progressDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initData();
        initView();
        initMap();
        init();
    }

    private void getBikeInfoSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mapFragment.onBikeDataSuccess(listBike);
            }
        });

    }

    private void initSpinner2Data() {
        nameList.clear();
        if (dataLists == null || dataLists.size() == 0) {
            adapter.notifyDataSetChanged();
            return;
        }
        for (int i = 0; i < dataLists.size(); i++) {
            nameList.add(dataLists.get(i).getName());
        }
        adapter.notifyDataSetChanged();
        isInitSp = true;
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("MAIN", "onItemSelected" + companyId);

                if (isInitSp) {
                    isInitSp = false;
                    return;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                companyId = dataLists.get(i).getId();
                Log.e("MAIN", "companyId" + companyId);
                mapFragment.clearBikingMap();
                showProgress(getString(R.string.loading));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        BikeInfoHttp.getBikeList(companyId, mHandler);

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("MAIN", "onNothingSelected" + companyId);

            }
        });
        spinner2.setSelection(0);
        companyId = dataLists.get(0).getId();
        isRefreshUI = true;
    }

    //定时器，每5秒刷新一次UI
    private Timer refreshTimer = new Timer(true);
    private TimerTask refreshTask = new TimerTask() {
        @Override
        public void run() {
            if (isRefreshUI) {
                Message msg = mHandler.obtainMessage();
                msg.what = Constant.REFRESH_UI;
                mHandler.sendMessage(msg);
            }
        }
    };

    private void init() {
        refreshTimer.schedule(refreshTask, 0, REFRESH_TIME);


    }

    /**
     * 开启地图
     */
    private void initMap() {
        mapFragment = new MapFragment();
        showFragment(mapFragment, true, R.id.fragment_map);

    }

    private void showFragment(Fragment fg, boolean b, int containerViewId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (b) {
            if (!fg.isAdded()) {
                fragmentTransaction.add(containerViewId, fg);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } else {
            if (fg.isAdded()) {
                fragmentTransaction.remove(fg);
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        myAdapter = new MyAdapter(MainActivity.this, list);
        spinner1.setAdapter(myAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemName = spinner1.getItemAtPosition(i).toString();
                if (itemName.equals("直营")) {
                    type = ZHIYING;
                    NameHttp.getName(type, mHandler);
                } else if (itemName.equals("联营")) {
                    type = LIANYING;
                    NameHttp.getName(type, mHandler);
                } else if (itemName.equals("景区")) {
                    type = JINGQU;
                    NameHttp.getName(type, mHandler);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter = new MyAdapter(MainActivity.this, nameList);
        spinner2.setAdapter(adapter);
    }

    private void initData() {
        list.add(getString(R.string.directly_manufacturer));
        list.add(getString(R.string.pool));
        list.add(getString(R.string.scenic));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.e("MAIN", "KEYCODE_DPAD_RIGHT");
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.e("MAIN", "KEYCODE_DPAD_LEFT");
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            case KeyEvent.KEYCODE_MENU:
                Log.e("MAIN", "KEYCODE_MENU");
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            default:
                break;
        }


        return super.onKeyDown(keyCode, event);
    }

    public void showProgress(String msg) {
        //如果已经存在并且在显示中就不处理
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 取消进度条
     */
    public void clearTask() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(TAG,"onSaveInstanceState");
    }
}
