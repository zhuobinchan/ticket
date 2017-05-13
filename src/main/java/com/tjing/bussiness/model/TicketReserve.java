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
@Table(name="bi_ticket_reserve")
@org.hibernate.annotations.Table(appliesTo="bi_ticket_reserve",comment="预订记录")
public class TicketReserve {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="ticket_num")
	@FieldDesc(comment="订座数量")
	private Integer ticketNum;
	@Column(length=11,name="show_number_id")
	@FieldDesc(comment="场次ID",dic="select id,name from bi_show_number where id=?")
	private Integer showNumberId;
	@Column(length=11,name="customer_id")
	@FieldDesc(comment="客户ID")
	private Integer customerId;
	@Column(name="customer_name",length=40)
	@FieldDesc(comment="客户简称")
	private String customerName;
	@Column(name="strategy_id")
	@FieldDesc(comment="门票策略ID")
	private Integer strategyId;
	@Column(name="strategy_name")
	@FieldDesc(comment="门票策略")
	private String strategyName;
	@Column(name="cheap_price")
	@FieldDesc(comment="立减金额")
	private Integer cheapPrice;
	@Column(name="customer_cheap")
	@FieldDesc(comment="客户优惠")
	private Integer customerCheap;
	@Column(name="foregift")
	@FieldDesc(comment="预收金额")
	private Integer foregift;
	@Column(name="应付金额")
	@FieldDesc(comment="应付金额")
	private Integer amount;
	@Column(name="实收金额")
	@FieldDesc(comment="实收金额")
	private Integer realAmount;
	@Column(name="descs",length=200)
	@FieldDesc(comment="备注")
	private String descs;
	@Column(name="saller_code",length=30)
	@FieldDesc(comment="营销编号")
	private String sallerCode;
	@Column(name="member_no",length=30)
	@FieldDesc(comment="会员卡号")
	private String memberNo;
	
	@Column(name="member_id")
	@FieldDesc(comment="会员ID")
	private Integer memberId;
	
	@Column(name="play_date")
	@FieldDesc(comment="演出日期")
	@JSONField(format="yyyy-MM-dd")
	private Date playDate;
	@Column(name="create_time")
	@FieldDesc(comment="售票时间")
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date createTime;
	@Column(name="create_user_id")
	@FieldDesc(comment="售票人ID")
	private Integer createUserId; 
	@Column(name="create_user_name")
	@FieldDesc(comment="售票人")
	private String createUserName;
	@Column(name="status",length=9)
	@FieldDesc(comment="记录状态",dic="0205")
	private String status;
	@Column(name="theater_sn",length=20)
	@FieldDesc(comment="剧院编号")
	private String theaterSn;
	@Column(name="reserve_type",length=9)
	@FieldDesc(comment="预订方式",dic="0214")
	private String reserveType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTicketNum() {
		return ticketNum;
	}
	public void setTicketNum(Integer ticketNum) {
		this.ticketNum = ticketNum;
	}
	
	public Integer getShowNumberId() {
		return showNumberId;
	}
	public void setShowNumberId(Integer showNumberId) {
		this.showNumberId = showNumberId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}
	
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public Integer getCheapPrice() {
		return cheapPrice;
	}
	public void setCheapPrice(Integer cheapPrice) {
		this.cheapPrice = cheapPrice;
	}
	
	public Integer getCustomerCheap() {
		return customerCheap;
	}
	public void setCustomerCheap(Integer customerCheap) {
		this.customerCheap = customerCheap;
	}
	public Integer getForegift() {
		return foregift;
	}
	public void setForegift(Integer foregift) {
		this.foregift = foregift;
	}
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(Integer realAmount) {
		this.realAmount = realAmount;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public String getSallerCode() {
		return sallerCode;
	}
	public void setSallerCode(String sallerCode) {
		this.sallerCode = sallerCode;
	}
	
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public Date getPlayDate() {
		return playDate;
	}
	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTheaterSn() {
		return theaterSn;
	}
	public void setTheaterSn(String theaterSn) {
		this.theaterSn = theaterSn;
	}
	public String getReserveType() {
		return reserveType;
	}
	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	
}
