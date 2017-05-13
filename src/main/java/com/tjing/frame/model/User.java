package com.tjing.frame.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.tjing.frame.annotation.FieldDesc;
import com.alibaba.fastjson.annotation.JSONField;
@Entity
@Table(name="TJ_USER")
@org.hibernate.annotations.Table(comment="用户表", appliesTo = "TJ_USER")
public class User implements Serializable{
	
	private static final long serialVersionUID = -5148885969368798649L;
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=40)
	private Integer id;
	@Column(name="org_id",length=40)
	@FieldDesc(comment="部门ID",dic="select id,name from tj_organization where id=?")
	private Integer orgId;
	@Column(name="user_code",length=20,nullable=false,unique=true)
	@FieldDesc(comment="登录帐号")
	private String userCode;//登陆用户名
	@Column(length=20)
	@FieldDesc(comment="用户名称")
	private String name;
	@Column(name="theater_sn",length=20)
	@FieldDesc(comment="剧场编号")
	private String theaterSn;
	@Column(length=20)
	@FieldDesc(comment="最近使用的IP")
	private String ip;
	@Column(length=40)
	@FieldDesc(comment="最近使用的MAC")
	private String mac;
	@Column(length=12)
	@FieldDesc(comment="手机号码")
	private String mobileno;
	@Column(length=11)
	@FieldDesc(comment="办公电话")
	private String tel;
	@Column(length=51)
	@FieldDesc(comment="电子邮箱")
	private String email;
	@Column(length=20)
	@JsonIgnore
	@FieldDesc(comment="用户密码")
	private String password;
	@Column(length=8)
	@FieldDesc(dic="0102",comment="性别")
	private String gender;
	@Column(length=2,name="err_login_count")
	@FieldDesc(comment="错误登录次数")
	private Integer errLoginCount;
	@Column(length=5,name="snno")
	@FieldDesc(comment="流水号数字")//每天更新
	private Integer snno = 0;
	@Column(name="last_login_time")
	@JSONField(format="yyyy-MM-dd HH:mm")
	@FieldDesc(comment="最后登录时间")
	private Date lastLoginTime;
	@Column(name="fetureLetter",length=5)
	@FieldDesc(comment="特征字")
	private String fetureLetter;
	@Transient
	private String matchCode;
	@Transient
	private String roleString;
	@Transient
	private String pccode;
	public User(){
		super();
	}
	public User(String userCode){
		this.userCode = userCode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTheaterSn() {
		return theaterSn;
	}
	public void setTheaterSn(String theaterSn) {
		this.theaterSn = theaterSn;
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
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getErrLoginCount() {
		return errLoginCount;
	}
	public void setErrLoginCount(Integer errLoginCount) {
		this.errLoginCount = errLoginCount;
	}
	public Integer getSnno() {
		return snno;
	}
	public void setSnno(Integer snno) {
		this.snno = snno;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getFetureLetter() {
		return fetureLetter;
	}
	public void setFetureLetter(String fetureLetter) {
		this.fetureLetter = fetureLetter;
	}
	public String getMatchCode() {
		return matchCode;
	}
	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
	}
	public String getRoleString() {
		return roleString;
	}
	public void setRoleString(String roleString) {
		this.roleString = roleString;
	}
	public String getPccode() {
		return pccode;
	}
	public void setPccode(String pccode) {
		this.pccode = pccode;
	}
	
}