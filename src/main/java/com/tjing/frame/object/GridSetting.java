package com.tjing.frame.object;


public class GridSetting {
	private String id;
	private String modelName;
	private String useMode;
	private String fieldsStr;
	private boolean hideCheckbox;
	private PageInfo pageInfo;
	private String service;//处理数据的service
	private String queryParams;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getFieldsStr() {
		return fieldsStr;
	}

	public void setFieldsStr(String fieldsStr) {
		this.fieldsStr = fieldsStr;
	}

	public String getUseMode() {
		return useMode;
	}

	public void setUseMode(String useMode) {
		this.useMode = useMode;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public boolean isHideCheckbox() {
		return hideCheckbox;
	}

	public void setHideCheckbox(boolean hideCheckbox) {
		this.hideCheckbox = hideCheckbox;
	}
	
}
