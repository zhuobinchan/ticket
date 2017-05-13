package com.tjing.frame.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.tjing.frame.annotation.FieldDesc;
import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name="tj_operate_log")
@org.hibernate.annotations.Table(appliesTo="tj_operate_log",comment="操作日志")
public class OperateLog {
	@Id
	@Column(length=40)
	private String id;
	@Column(length=40,name="user_id")
	@FieldDesc(comment="用户id",dic="select id,name from tc_user where id=?")
	private Integer userId;
	@Column(length=140,name="user_code")
	@FieldDesc(comment="用户帐号")
	private String userCode;
	@Column(length=20,name="user_name")
	@FieldDesc(comment="用户名称")
	private String userName;
	@Column(length=20,name="table_name")
	@FieldDesc(comment="表名")
	private String tableName;
	@Column(name="record_date")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@FieldDesc(comment="记录时间")
	private Date recordDate;
	@Column(length=100)
	@FieldDesc(comment="描述")
	private String descs;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	
}
