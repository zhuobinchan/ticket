package com.tjing.group.client.model;

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
@Table(name = "bi_venue_sales_send_view")
@org.hibernate.annotations.Table(appliesTo = "bi_venue_sales_send_view", comment = "场内消费发送情况视图")
public class VenueSalesSendView {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;

	@Column(length = 20, name = "sale_no", columnDefinition = "VARCHAR(20) COMMENT '销售编号'")
	@FieldDesc(comment = "销售编号")
	private String saleNo;

	@Column(name = "seat_id", columnDefinition = "INT COMMENT '座位'")
	@FieldDesc(comment = "座位", dic = "select id,name from bi_seat where id=?")
	private Integer seatId;

	@Column(nullable = false, columnDefinition = "INT COMMENT '营销员ID'")
	@FieldDesc(comment = "营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer salesmanId;

	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '应收金额'")
	@FieldDesc(comment = "应收金额")
	private Float receivable;

	@Column(name = "real_pay", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '实收金额'")
	@FieldDesc(comment = "实收金额")
	private Float realPay;

	@Column(name = "pay_type", length = 8, columnDefinition = "VARCHAR(8) COMMENT '支付方式'")
	@FieldDesc(comment = "支付方式", dic = "0207")
	private String payType;

	@Column(name = "fill_difference", columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '充值卡补差价金额'")
	@FieldDesc(comment = "充值卡补差价金额")
	private Float fillDifference;

	@Column(name = "fill_pay_type", length = 8, columnDefinition = "VARCHAR(8) COMMENT '支付方式'")
	@FieldDesc(comment = "支付方式", dic = "0207")
	private String fillPayType;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '状态'")
	@FieldDesc(comment = "状态", dic = "0227")
	private String status;

	@Column(name = "member_card_id", columnDefinition = "INT COMMENT '会员卡ID'")
	@FieldDesc(comment = "会员卡ID", dic = "select id,code from bi_member_card where id=?")
	private Integer cardId;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '优惠策略'")
	@FieldDesc(comment = "优惠策略", dic = "0225")
	private String strategy;

	@Column(name = "package_id", columnDefinition = "INT COMMENT '套餐ID'")
	@FieldDesc(comment = "套餐ID", dic = "select id,name from bi_package where id=?")
	private Integer packageId;

	@Column(length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

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

	@Column(name = "theater_sn", length = 20)
	@FieldDesc(comment = "剧场编号", dic = "select id,sn from bi_theater where id=?")
	private String theaterSn;

	@Column(name = "send_status", length = 8, columnDefinition = "VARCHAR(8) DEFAULT '0234001' COMMENT '记录发送状态'")
	@FieldDesc(comment = "记录发送状态", dic = "0234")
	private String sendStatus;

	@Column(name = "send_time", columnDefinition = "DATETIME COMMENT '发送时间'")
	@FieldDesc(comment = "发送时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date sendTime;

	@Column(name = "send_count", columnDefinition = "INT COMMENT '发送次数'")
	private Integer sendCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Integer getSeatId() {
		return seatId;
	}

	public void setSeatId(Integer seatId) {
		this.seatId = seatId;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Float getReceivable() {
		return receivable;
	}

	public void setReceivable(Float receivable) {
		this.receivable = receivable;
	}

	public Float getRealPay() {
		return realPay;
	}

	public void setRealPay(Float realPay) {
		this.realPay = realPay;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Float getFillDifference() {
		return fillDifference;
	}

	public void setFillDifference(Float fillDifference) {
		this.fillDifference = fillDifference;
	}

	public String getFillPayType() {
		return fillPayType;
	}

	public void setFillPayType(String fillPayType) {
		this.fillPayType = fillPayType;
	}

	public String getTheaterSn() {
		return theaterSn;
	}

	public void setTheaterSn(String theaterSn) {
		this.theaterSn = theaterSn;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

}
