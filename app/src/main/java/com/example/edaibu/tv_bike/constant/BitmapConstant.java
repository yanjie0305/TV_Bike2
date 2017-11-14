package com.example.edaibu.tv_bike.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.example.edaibu.tv_bike.R;
import com.example.edaibu.tv_bike.fragment.MapFragment;

import java.io.InputStream;

public class BitmapConstant {

//    public static BitmapDescriptor bitmap1= BitmapDescriptorFactory.fromResource(R.mipmap.good_car);
//    public static BitmapDescriptor bitmap2= BitmapDescriptorFactory.fromResource(R.mipmap.fault_car);
//
    public static BitmapDescriptor bitmap3= BitmapDescriptorFactory.fromResource(R.mipmap.good_car);
    /**
     * 以最省内存的方式读取本地资源的图片
     * */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
}
