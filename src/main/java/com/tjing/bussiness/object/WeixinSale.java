package com.tjing.bussiness.object;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class WeixinSale {
	private List<JSONObject> saledSeatInfo;
	private List<JSONObject> reservedSeatInfo;
	private List<JSONObject> lockedSeatInfo;
	private List<JSONObject> pickedSeatInfo;//我锁定的座位
	public WeixinSale() {
		super();
	}
	public WeixinSale(List<JSONObject> saledSeatInfo,List<JSONObject> reservedSeatInfo,
			List<JSONObject> lockedSeatInfo,List<JSONObject> pickedSeatInfo) {
		super();
		this.saledSeatInfo = saledSeatInfo;
		this.reservedSeatInfo = reservedSeatInfo;
		this.pickedSeatInfo = pickedSeatInfo;
		this.lockedSeatInfo = lockedSeatInfo;
	}
	public List<JSONObject> getSaledSeatInfo() {
		return saledSeatInfo;
	}
	public void setSaledSeatInfo(List<JSONObject> saledSeatInfo) {
		this.saledSeatInfo = saledSeatInfo;
	}
	public List<JSONObject> getReservedSeatInfo() {
		return reservedSeatInfo;
	}
	public void setReservedSeatInfo(List<JSONObject> reservedSeatInfo) {
		this.reservedSeatInfo = reservedSeatInfo;
	}
	public List<JSONObject> getLockedSeatInfo() {
		return lockedSeatInfo;
	}
	public void setLockedSeatInfo(List<JSONObject> lockedSeatInfo) {
		this.lockedSeatInfo = lockedSeatInfo;
	}
	public List<JSONObject> getPickedSeatInfo() {
		return pickedSeatInfo;
	}
	public void setPickedSeatInfo(List<JSONObject> pickedSeatInfo) {
		this.pickedSeatInfo = pickedSeatInfo;
	}
	
}
