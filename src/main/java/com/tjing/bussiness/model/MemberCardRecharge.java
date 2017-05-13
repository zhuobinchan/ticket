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
@Table(name = "bi_member_card_recharge")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_recharge", comment = "会员卡（充值卡）充值")
public class MemberCardRecharge {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '充值ID'")
	private Integer id;

	@Column(name = "member_card_id", nullable = false, columnDefinition = "INT COMMENT '会员卡ID'")
	@FieldDesc(comment = "会员卡ID", dic = "select id,code from bi_member_card where id=?")
	private Integer cardId;

	@Column(name = "create_time", nullable = false, columnDefinition = "DATETIME COMMENT '充值时间'")
	@FieldDesc(comment = "充值时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "recharge_amount", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '充值金额'")
	@FieldDesc(comment = "充值金额")
	private Float rechargeAmount;

	@Column(name = "real_amount", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '实收金额'")
	@FieldDesc(comment = "实收金额")
	private Float realAmount;

	@Column(columnDefinition = "INT COMMENT '营销员ID'")
	@FieldDesc(comment = "营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer salesmanId;

	@Column(name = "status", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '记录状态'")
	@FieldDesc(comment = "记录状态", dic = "0202")
	private String status;

	@Column(name = "pay_type", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '充值方式'")
	@FieldDesc(comment = "充值方式", dic = "0207")
	private String payType;

	@Column(length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '网点'")
	@FieldDesc(comment = "网点", dic = "0220")
	private String branch;

	@Column(name = "user_id", nullable = false, columnDefinition = "INT COMMENT '经办人ID'")
	@FieldDesc(comment = "经办人ID", dic = "select id,name from tj_user where id=?")
	private Integer userId;

	@Column(name = "remark", length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Float getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Float rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Float getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Float realAmount) {
		this.realAmount = realAmount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

}
