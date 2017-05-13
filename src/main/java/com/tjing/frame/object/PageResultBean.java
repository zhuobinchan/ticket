package com.tjing.frame.object;

import java.util.ArrayList;
import java.util.List;

public class PageResultBean {
	private Long totalRecordNum;
	private Long totalPageNum;
	private int showPageNum;
	@SuppressWarnings("rawtypes")
	private List list = new ArrayList();
	
	public PageResultBean() {
		super();
	}
	@SuppressWarnings("rawtypes")
	public PageResultBean(Long totalRecordNum, List list) {
		super();
		this.totalRecordNum = totalRecordNum;
		this.list = list;
	}
	public Long getTotalRecordNum() {
		return totalRecordNum;
	}
	public void setTotalRecordNum(Long totalRecordNum) {
		this.totalRecordNum = totalRecordNum;
	}
	
	public Long getTotalPageNum() {
		return totalPageNum;
	}
	public void setTotalPageNum(Long totalPageNum) {
		this.totalPageNum = totalPageNum;
	}
	
	
	public int getShowPageNum() {
		return showPageNum;
	}
	public void setShowPageNum(int showPageNum) {
		this.showPageNum = showPageNum;
	}
	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}
	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}
	
}
