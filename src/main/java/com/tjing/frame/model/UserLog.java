package com.tjing.frame.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="tj_user_log")
@org.hibernate.annotations.Table(comment="登录/登出日志", appliesTo = "tj_user_log")
public class UserLog {
	@Id
	@Column(length=40)
	private String id;
	@Column(length=40,name="user_id")
	@FieldDesc(comment="用户id")
	private String userId;
	@Column(length=60,name="sessionid")
	@FieldDesc(comment="会话ID")
	private String sessionId;
	@Column(length=16,name="match_code")
	@FieldDesc(comment="匹配编号")
	private String matchCode;
	@Column(length=140,name="user_code")
	@FieldDesc(comment="用户帐号")
	private String userCode;
	@Column(length=20,name="user_name")
	@FieldDesc(comment="用户名称")
	private String userName;
	@Column(length=20,name="ip")
	@FieldDesc(comment="用户IP")
	private String ip;
	@Column(length=40,name="mac")
	@FieldDesc(comment="用户物理地址")
	private String mac;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getMatchCode() {
		return matchCode;
	}
	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
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
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
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
