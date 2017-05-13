package com.tjing.bussiness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_member_card_recharge_view")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_recharge_view", comment = "会员卡（充值卡）充值，包括卡信息、会员信息")
public class MemberCardRechargeView {
	@Id
	private Integer id;

	@FieldDesc(comment = "会员卡号")
	private String code;

	@Column(name = "member_id")
	@FieldDesc(comment = "会员ID", dic = "select id,name from bi_member where id=?")
	private Integer memberId;

	@Column(name = "member_name")
	@FieldDesc(comment = "姓名")
	private String memberName;

	@Column(name = "member_mobile")
	@FieldDesc(comment = "手机号码")
	private String memberMobile;

	@Column(name = "member_card_id")
	@FieldDesc(comment = "会员卡ID", dic = "select id,code from bi_member_card where id=?")
	private Integer cardId;

	@Column(name = "create_time")
	@FieldDesc(comment = "充值时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "recharge_amount")
	@FieldDesc(comment = "充值金额")
	private Float rechargeAmount;

	@Column(name = "real_amount")
	@FieldDesc(comment = "实收金额")
	private Float realAmount;

	@Column(name = "salesman_id")
	@FieldDesc(comment = "营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer salesmanId;

	@FieldDesc(comment = "记录状态", dic = "0202")
	private String status;

	@Column(name = "pay_type")
	@FieldDesc(comment = "充值方式", dic = "0207")
	private String payType;

	@FieldDesc(comment = "网点", dic = "0220")
	private String branch;

	@Column(name = "user_id")
	@FieldDesc(comment = "经办人ID", dic = "select id,name from tj_user where id=?")
	private Integer userId;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

}
