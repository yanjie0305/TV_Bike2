package com.example.edaibu.tv_bike.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${gyj} on 2017/11/7.
 */

public class MessageBean implements Parcelable {
    private  Long msg_id;
    private  double lat;
    private  double lng;
    private  String status;
    private  String bikeCode;

    protected MessageBean(Parcel in) {
       Long msg_id = in.readLong();
        lat = in.readDouble();
         lng = in.readDouble();
         status = in.readString();
        bikeCode = in.readString();

    }

    public MessageBean(Long msg_id,double lat,double lng,String status,String bikeCode) {
        this.msg_id= msg_id;
        this.lat =lat;
        this.lng = lng;
        this.status = status;
        this.bikeCode = bikeCode;
    }

    public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel in) {
            return new MessageBean(in);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(msg_id);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(status);
        dest.writeString(bikeCode);
    }

    public Long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(Long msg_id) {
        this.msg_id = msg_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBikeCode() {
        return bikeCode;
    }

    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }
}
