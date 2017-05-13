package com.tjing.frame.object;

import com.alibaba.fastjson.JSONObject;


/*
 * @description 存放页面grid数据
 */
public class GridHtmlData {
	private String tableStr;
	private JSONObject userData;
	private PageInfo pageInfo;
	public GridHtmlData() {
		super();
	}
	public GridHtmlData(String tableStr) {
		this.tableStr = tableStr;
	}
	public GridHtmlData(String tableStr,JSONObject userData) {
		this.tableStr = tableStr;
		this.userData = userData;
	}
	public String getTableStr() {
		return tableStr;
	}
	public void setTableStr(String tableStr) {
		this.tableStr = tableStr;
	}
	public JSONObject getUserData() {
		return userData;
	}
	public void setUserData(JSONObject userData) {
		this.userData = userData;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
}
