package com.tjing.bussiness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="bi_customer")
@org.hibernate.annotations.Table(appliesTo="bi_customer",comment="客户资料")
public class Customer {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="code",length=20)
	@FieldDesc(comment="客户编号")
	private String code;
	@Column(name="name",length=100)
	@FieldDesc(comment="客户名称")
	private String name;
	@Column(name="shorname",length=100)
	@FieldDesc(comment="客户简称")
	private String shortname;
	@Column(name="spell",length=10)
	@FieldDesc(comment="字母检索")
	private String spell;
	@Column(name="mobileno",length=40)
	@FieldDesc(comment="手机号码")
	private String mobileno;
	@Column(name="phone",length=40)
	@FieldDesc(comment="座机号码")
	private String phone;
	@Column(name="address",length=140)
	@FieldDesc(comment="联系地址")
	private String address;
	@Column(name="type",length=9)
	@FieldDesc(comment="客户类型",dic="0206")
	private String type;
	@Column(name="status",length=9)
	@FieldDesc(comment="记录状态",dic="0202")
	private String status;
	@Column(name="create_time")
	@FieldDesc(comment="创建时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Column(name="cheap_price",length=3)
	@FieldDesc(comment="每张立减")
	private Integer cheapPrice;
	@Column(name="use_num",length=11)
	@FieldDesc(comment="使用次数")
	private Integer useNum;
	@Column(length=11)
	@FieldDesc(comment="账户余额")
	private Integer balance;
	@Column(length=11)
	@FieldDesc(comment="充值金额")
	private Integer rechargeAmount;
	@Column(name="recharge_time")
	@FieldDesc(comment="上次充值时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date rechargeTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getCheapPrice() {
		return cheapPrice;
	}
	public void setCheapPrice(Integer cheapPrice) {
		this.cheapPrice = cheapPrice;
	}
	
	public Integer getUseNum() {
		return useNum;
	}
	public void setUseNum(Integer useNum) {
		this.useNum = useNum;
	}
	public Integer getBalance() {
		return balance;
	}
	
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public Integer getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(Integer rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public Date getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

}
