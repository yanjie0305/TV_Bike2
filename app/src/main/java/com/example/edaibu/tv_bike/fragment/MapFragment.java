package com.example.edaibu.tv_bike.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.vi.VDeviceAPI;
import com.example.edaibu.tv_bike.R;
import com.example.edaibu.tv_bike.bean.BikeInfo;
import com.example.edaibu.tv_bike.constant.BitmapConstant;
import com.example.edaibu.tv_bike.constant.Constant;
import com.example.edaibu.tv_bike.util.OverlayManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${gyj} on 2017/9/20.
 */
public class MapFragment extends BaseFragment {
    private static String TAG = MapFragment.class.getSimpleName();
    private View view;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    /**
     * key:bikeCode
     */
    private Map<String, List<LatLng>> bikingMap = new HashMap<>();
    private LatLng latLng;
    private String moduleName;

    public void clearBikingMap() {
        bikingMap.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setMapCustomFile(getActivity(),"custom_config_fresh_black");
        super.onCreate(savedInstanceState);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.DISTANCE:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.map_layout, container, false);
        mapView = (MapView) view.findViewById(R.id.bmapView);
        MapView.setMapCustomEnable(true);
        initMap();
        return view;

    }

    private void initMap() {

        Log.e(TAG, "initMap");
        // 地图设置
        mBaiduMap = mapView.getMap();
    /*    mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
//                MapView.setMapCustomEnable(true);
            }
        });*/
        //去掉放大缩小按钮
        mapView.showZoomControls(false);
        // 去掉logo
        int count = mapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls || child instanceof ImageView) {
                child.setVisibility(View.GONE);
            }
        }
        UiSettings mUiSetting = mBaiduMap.getUiSettings();
        // 设置不允许旋转地图
        mUiSetting.setRotateGesturesEnabled(false);
        mUiSetting.setCompassEnabled(false);
        mUiSetting.setOverlookingGesturesEnabled(false);
        mBaiduMap.showMapPoi(false);
       /* mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");*/
    }

    /**
     * 刷新地图上车辆
     *
     * @param listBike
     */
    public void onBikeDataSuccess(List<BikeInfo.DataBean> listBike) {
        saveData(listBike);
        draw();
    }

    /**
     * 保存数据
     * @param listBike
     */
    private void saveData(List<BikeInfo.DataBean> listBike) {
        try {
            int size = listBike.size();
            for (int i = 0; i < size; i++) {
                BikeInfo.DataBean bike = listBike.get(i);

                if (!bike.getStatus().equals("3")) {
                    continue;
                }

                String bikeCode = bike.getBikeCode();
                if (bikeCode == null) {
                    continue;
                }
                if (bikeCode.trim().length() <= 0) {
                    continue;
                }

                List<LatLng> latLngList = bikingMap.get(bikeCode);
                if (latLngList == null) {
                    latLngList = new ArrayList<LatLng>();
                    bikingMap.put(bikeCode, latLngList);
                }
                double lat = bike.getLoc().get(1);
                double lng = bike.getLoc().get(0);
                Log.e(MapFragment.class.getSimpleName(), "lat=" + lat + ",lng=" + lng);
                if (lat == 0d && lng == 0d) {
                    continue;
                }
                LatLng latLng = new LatLng(lat, lng);
                latLngList.add(latLng);
               /*  if (latLngList.size() >= 20) {
                 latLngList.remove(0);
                   }*/
            }
        } catch (Exception e) {
            Log.e(MapFragment.class.getSimpleName(), e.getMessage());
        }
    }

    private void draw() {
        Log.e(TAG, "drawMarker");
        if (null == mBaiduMap) {
            return;
        }
        mBaiduMap.clear();

        final List<OverlayOptions> overlayOptions = new ArrayList<OverlayOptions>();
        // 管理多个覆盖物
        final OverlayManager overlayManager = new OverlayManager(mBaiduMap) {
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return overlayOptions;
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }

            @Override
            public boolean onPolylineClick(Polyline polyline) {
                return true;
            }
        };
        BitmapDescriptor bitmapDescriptor = null;
        try {
            Bitmap bitmap = BitmapConstant.readBitMap(getActivity(), R.mipmap.good_car);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        } catch (OutOfMemoryError e) {
            Log.e(MapFragment.class.getSimpleName(), e.getMessage());
        }

        drawBikes(overlayOptions, bitmapDescriptor);
        overlayManager.addToMap();
        boolean tabletDevice = isTabletDevice(getActivity());
        Log.e(TAG, "isTV----" + tabletDevice);
         overlayManager.zoomToSpan(); //仅对mark起作用
        // if (tabletDevice){
        // overlayManager.zoomToSpan(); //仅对mark起作用
        // }else {
        // 将地图移到最后一个经纬度位置
        // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);

        // mBaiduMap.setMapStatus(u);
        //  }

    }


    /**
     * 画所有车的轨迹
     * @param overlayOptions
     * @param bitmapDescriptor
     */
    private void drawBikes(List<OverlayOptions> overlayOptions, BitmapDescriptor bitmapDescriptor) {
        if (null == mBaiduMap) {
            return;
        }
        mBaiduMap.clear();

        for (Map.Entry<String, List<LatLng>> entry : bikingMap.entrySet()) {
            String bikeCode = entry.getKey();
            List<LatLng> latLngList = entry.getValue();
            drawOneBike(bitmapDescriptor, overlayOptions, bikeCode, latLngList);
        }
        Log.e(TAG, "drawBike");


    }

    /**
     * 画一辆车的轨迹
     * @param bitmapDescriptor
     * @param overlayOptions
     * @param bikeCode
     * @param latLngList
     */
    private void drawOneBike(BitmapDescriptor bitmapDescriptor, List<OverlayOptions> overlayOptions, String bikeCode, List<LatLng> latLngList) {
        if (null == latLngList || latLngList.size() < 2) {
            return;
        }
        if (latLngList.isEmpty()) {
            return;
        }
        // draw line
        PolylineOptions options = new PolylineOptions();
        options.points(latLngList);
        options.width(10);
        options.zIndex(1000);
        options.color(getResources().getColor(R.color.bike_line));
        mBaiduMap.addOverlay(options);
        // draw bike icon
        latLng = latLngList.get(latLngList.size() - 1);
        MarkerOptions mo = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        overlayOptions.add(mo);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
        mapView.onPause();
    }


    /**
     * 判断是否为电视设备
     *
     * @return
     */

    private boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     *
     * 设置个性化地图config文件路径
     */
    public static void setMapCustomFile(Context context, String PATH) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/" + PATH);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + PATH);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "moduleName----"+moduleName+"/"+ PATH);
        MapView.setCustomMapStylePath(moduleName + "/" + PATH);

    }
}