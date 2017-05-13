package com.tjing.frame.object;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class RequestObject {
	private String poName;
	private String id;
	private List<JSONObject> data;
	private List<String> arr;//一般保存ID
	private String method;
	private String message;
	private boolean promise;
	public String getPoName() {
		return poName;
	}
	public void setPoName(String poName) {
		this.poName = poName;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<JSONObject> getData() {
		return data;
	}
	public void setData(List<JSONObject> data) {
		this.data = data;
	}
	
	
	public List<String> getArr() {
		return arr;
	}
	public void setArr(List<String> arr) {
		this.arr = arr;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isPromise() {
		return promise;
	}
	public void setPromise(boolean promise) {
		this.promise = promise;
	}
	
}
