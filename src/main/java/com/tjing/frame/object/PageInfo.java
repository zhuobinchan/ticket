package com.tjing.frame.object;

public class PageInfo {
	private int showPageNum;
	private int records;
	private long totalPageNum;
	private long totalRecords;
	private String orderby;
	public int getShowPageNum() {
		return showPageNum;
	}
	public void setShowPageNum(int showPageNum) {
		this.showPageNum = showPageNum;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public long getTotalPageNum() {
		return totalPageNum;
	}
	public void setTotalPageNum(long totalPageNum) {
		this.totalPageNum = totalPageNum;
	}
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	
}
