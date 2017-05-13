package com.tjing.frame.object;

import java.util.Map;

public class ClassInfo {
	private String idType;
	private String name;
	private String fullName;
	private String tableName;
	private Map<String,FieldInfo> fileds;
	
	public ClassInfo() {
		super();
	}
	
	public ClassInfo(String fullName, String tableName) {
		super();
		this.fullName = fullName;
		this.tableName = tableName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Map<String, FieldInfo> getFileds() {
		return fileds;
	}
	public void setFileds(Map<String, FieldInfo> fileds) {
		this.fileds = fileds;
	}
	
}
