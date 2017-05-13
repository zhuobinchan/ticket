package com.tjing.frame.object;
/*
 * 实体的字段信息
 */
public class FieldInfo {
	private String name;
	private String dbName;
	private String type;
	private String dic;
	private String comment;
	private String pattern;
	private int length;
	private String orderby;
	
	public FieldInfo() {
		super();
	}
	
	public FieldInfo(String name, String dbName, String type) {
		super();
		this.name = name;
		this.dbName = dbName;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDic() {
		return dic;
	}

	public void setDic(String dic) {
		this.dic = dic;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	
}
