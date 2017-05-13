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
@Table(name = "bi_member_card_history")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_history", comment = "会员卡历史，用于记录被删除的会员卡")
public class MemberCardHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '会员卡历史ID'")
	private Integer id;

	@Column(name = "card_id", nullable = false, columnDefinition = "INT COMMENT '会员卡ID'")
	@FieldDesc(comment = "会员卡ID")
	private Integer cardId;

	@Column(length = 20, nullable = false, columnDefinition = "VARCHAR(20) COMMENT '会员卡号'")
	@FieldDesc(comment = "会员卡号")
	private String code;

	@Column(name = "member_id", length = 11, columnDefinition = "INT COMMENT '会员ID'")
	@FieldDesc(comment = "会员ID", dic = "select id,name from bi_member where id=?")
	private Integer memberId;

	@Column(name = "password", length = 32, columnDefinition = "VARCHAR(32) COMMENT '密码，MD5密文'")
	@FieldDesc(comment = "密码")
	private String password;

	@Column(name = "name", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '卡名称'")
	@FieldDesc(comment = "卡名称", dic = "0215")
	private String name;

	@Column(name = "type", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '卡类别'")
	@FieldDesc(comment = "卡类别", dic = "0216")
	private String type;

	@Column(name = "status", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '卡状态'")
	@FieldDesc(comment = "卡状态", dic = "0218")
	private String status;

	@Column(name = "expiry_time", columnDefinition = "DATETIME COMMENT '过期时间'")
	@FieldDesc(comment = "过期时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date expiryTime;

	@Column(name = "effective_time", columnDefinition = "DATETIME COMMENT '生效时间'")
	@FieldDesc(comment = "生效时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date effectiveTime;

	@Column(columnDefinition = "INT COMMENT '营销员ID'")
	@FieldDesc(comment = "营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer salesmanId;

	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '账户余额'")
	@FieldDesc(comment = "账户余额")
	private Float balance;

	@Column(length = 11, nullable = false, columnDefinition = "INT DEFAULT 0 COMMENT '积分'")
	@FieldDesc(comment = "积分")
	private Integer point;

	@Column(name = "last_rechargege_way", length = 8, columnDefinition = "VARCHAR(8) COMMENT '最后充值方式'")
	@FieldDesc(comment = "最后充值方式", dic = "0219")
	private String lastRechargeWay;

	@Column(name = "create_time", nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
	@FieldDesc(comment = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "update_time", nullable = false, columnDefinition = "DATETIME COMMENT '更新时间'")
	@FieldDesc(comment = "更新时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Column(name = "create_user_id", nullable = false, columnDefinition = "INT COMMENT '创建操作员ID'")
	@FieldDesc(comment = "创建操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "update_user_id", nullable = false, columnDefinition = "INT COMMENT '更新操作员ID'")
	@FieldDesc(comment = "更新操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer updateUserId;

	@Column(name = "remark", length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "delete_time", nullable = false, columnDefinition = "DATETIME COMMENT '删除时间'")
	@FieldDesc(comment = "删除时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date deleteTime;

	@Column(name = "delete_user_id", nullable = false, columnDefinition = "INT COMMENT '删除操作员ID'")
	@FieldDesc(comment = "删除操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer deleteUserId;

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getLastRechargeWay() {
		return lastRechargeWay;
	}

	public void setLastRechargeWay(String lastRechargeWay) {
		this.lastRechargeWay = lastRechargeWay;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(Integer deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
