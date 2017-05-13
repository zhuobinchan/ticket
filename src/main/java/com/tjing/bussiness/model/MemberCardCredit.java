package com.tjing.bussiness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_member_card_credit")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_credit", comment = "会员卡（充值卡）挂账清单")
public class MemberCardCredit {
	@Id
	@Column(name = "id", unique = true, length = 11, columnDefinition = "INT COMMENT '会员卡充值ID'")
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

	@Column(name = "credit_amount", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '挂账金额'")
	@FieldDesc(comment = "挂账金额")
	private Float creditAmount;

	@Column(columnDefinition = "INT COMMENT '营销员ID'")
	@FieldDesc(comment = "营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer salesmanId;

	@Column(name = "status", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '挂账状态'")
	@FieldDesc(comment = "挂账状态", dic = "0221")
	private String status;

	@Column(name = "repay_type", length = 8, columnDefinition = "VARCHAR(8) COMMENT '还款方式'")
	@FieldDesc(comment = "还款方式", dic = "0207")
	private String repayType;

	@Column(name = "repay_branch", length = 8, columnDefinition = "VARCHAR(8) COMMENT '还款网点'")
	@FieldDesc(comment = "还款网点", dic = "0220")
	private String repayBranch;

	@Column(name = "repay_amount", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '还款金额'")
	@FieldDesc(comment = "还款金额")
	private Float repayAmount;

	@Column(name = "repay_time", columnDefinition = "DATETIME COMMENT '还款时间'")
	@FieldDesc(comment = "还款时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date repayTime;

	@Column(columnDefinition = "INT COMMENT '还款营销员ID'")
	@FieldDesc(comment = "还款营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer repaySalesmanId;

	@Column(name = "user_id", nullable = false, columnDefinition = "INT COMMENT '经办人ID'")
	@FieldDesc(comment = "经办人ID", dic = "select id,name from tj_user where id=?")
	private Integer userId;

	@Column(name = "remark", length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	public Date getRepayTime() {
		return repayTime;
	}

	public Integer getRepaySalesmanId() {
		return repaySalesmanId;
	}

	public void setRepaySalesmanId(Integer repaySalesmanId) {
		this.repaySalesmanId = repaySalesmanId;
	}

	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Float getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Float creditAmount) {
		this.creditAmount = creditAmount;
	}

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

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRepayBranch() {
		return repayBranch;
	}

	public void setRepayBranch(String repayBranch) {
		this.repayBranch = repayBranch;
	}

	public Float getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(Float repayAmount) {
		this.repayAmount = repayAmount;
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
