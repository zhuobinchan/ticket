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
@Table(name="bi_recharge")
@org.hibernate.annotations.Table(appliesTo="bi_recharge",comment="客户充值记录")
public class Recharge {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length=11,name="customer_id")
	@FieldDesc(comment="客户ID",dic="select id,name from bi_customer where id=?")
	private Integer customerId;
	@Column(length=11)
	@FieldDesc(comment="充值金额")
	private Integer rechargeAmount;
	@Column(name="create_time")
	@FieldDesc(comment="充值时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Column(name="create_user_id",length=11)
	@FieldDesc(comment="经办人ID")
	private Integer createUserId;
	@Column(name="create_user_name",length=40)
	@FieldDesc(comment="经办人")
	private String createUserName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(Integer rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
