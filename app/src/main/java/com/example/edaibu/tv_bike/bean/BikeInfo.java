package com.example.edaibu.tv_bike.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 车基本信息
 * 
 * 
 */
public class BikeInfo  {

	private int code;
	private String msgX;
	private List<DataBean> data;
	public void setCode(int status) {
		this.code = status;
	}
	public int getCode() {
		return code;
	}

	public String getMsgX() {
		return msgX;
	}

	public void setMsgX(String msgX) {
		this.msgX = msgX;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * bikeCode : 0001181
		 * loc : [116.29251856310168,39.88804989858221]
		 * lockVersion : 1.007
		 * status : 1
		 */

		private String bikeCode;
		private String lockVersion;
		private String status;
		private List<Double> loc;

		public DataBean(String bikeCode, String lockVersion, String status, List<Double> loc) {
			this.bikeCode = bikeCode;
			this.lockVersion = lockVersion;
			this.status = status;
			this.loc = loc;
		}

		public String getBikeCode() {
			return bikeCode;
		}

		public void setBikeCode(String bikeCode) {
			this.bikeCode = bikeCode;
		}

		public String getLockVersion() {
			return lockVersion;
		}

		public void setLockVersion(String lockVersion) {
			this.lockVersion = lockVersion;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<Double> getLoc() {
			return loc;
		}

		public void setLoc(List<Double> loc) {
			this.loc = loc;
		}
	}
}
