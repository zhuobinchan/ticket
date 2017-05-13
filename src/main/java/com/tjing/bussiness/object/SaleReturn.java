package com.tjing.bussiness.object;

import java.util.List;

public class SaleReturn {
	private int status;
	private String msg;
	private List<PrintInfo> list;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<PrintInfo> getList() {
		return list;
	}
	public void setList(List<PrintInfo> list) {
		this.list = list;
	}
	
}
