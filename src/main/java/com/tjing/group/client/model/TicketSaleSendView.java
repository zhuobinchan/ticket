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
@Table(name = "bi_ticket_sale_send_view")
@org.hibernate.annotations.Table(appliesTo = "bi_ticket_sale_send_view", comment = "售票记录发送情况视图")
public class TicketSaleSendView {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "ticket_num")
	@FieldDesc(comment = "售票数量")
	private Integer ticketNum;

	@Column(length = 11, name = "show_number_id")
	@FieldDesc(comment = "场次ID", dic = "select id,name from bi_show_number where id=?")
	private Integer showNumberId;

	@Column(length = 11, name = "reserve_id")
	@FieldDesc(comment = "预订ID")
	private Integer reserveId;

	@Column(length = 11, name = "play_id")
	@FieldDesc(comment = "剧目ID")
	private Integer playId;

	@Column(length = 11, name = "customer_id")
	@FieldDesc(comment = "客户ID")
	private Integer customerId;

	@Column(name = "customer_name", length = 40)
	@FieldDesc(comment = "客户简称")
	private String customerName;

	@Column(name = "strategy_id")
	@FieldDesc(comment = "门票策略ID")
	private Integer strategyId;

	@Column(name = "strategy_name", length = 40)
	@FieldDesc(comment = "门票策略")
	private String strategyName;

	@Column(name = "pay_type")
	@FieldDesc(comment = "付款方式", dic = "0207")
	private String payType;

	@Column(name = "offset_price")
	@FieldDesc(comment = "抵扣金额")
	private Integer offsetPrice;

	@Column(name = "discount")
	@FieldDesc(comment = "折扣比例%")
	private Integer discount;

	@Column(name = "cheap_price")
	@FieldDesc(comment = "立减金额")
	private Integer cheapPrice;

	@Column(name = "descs", length = 200)
	@FieldDesc(comment = "备注")
	private String descs;

	@Column(name = "saller_code", length = 30)
	@FieldDesc(comment = "营销编号")
	private String sallerCode;

	@Column(name = "play_date")
	@FieldDesc(comment = "演出日期")
	@JSONField(format = "yyyy-MM-dd")
	private Date playDate;

	@Column(name = "balance_time")
	@FieldDesc(comment = "结算时间")
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date balanceTime;

	@Column(name = "create_time")
	@FieldDesc(comment = "售票时间")
	@JSONField(format = "yyyy-MM-dd hh:mm:ss")
	private Date createTime;

	@Column(name = "create_user_id")
	@FieldDesc(comment = "售票人ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "create_user_name")
	@FieldDesc(comment = "售票人")
	private String createUserName;

	@Column(name = "member_no")
	@FieldDesc(comment = "会员卡号")
	private String memberNo;

	@Column(name = "member_id")
	@FieldDesc(comment = "会员ID")
	private Integer memberId;

	@Column(length = 8, name = "price_show_type")
	@FieldDesc(comment = "打印方式", dic = "0209")
	private String priceShowType;

	@Column(length = 8, name = "is_show_seat")
	@FieldDesc(comment = "是否显示座位", dic = "0103")
	private String isShowSeat;

	@Column(length = 8, name = "has_tea")
	@FieldDesc(comment = "是否含茶", dic = "0103")
	private String hasTea;

	@Column(length = 6, name = "plus_show_price")
	@FieldDesc(comment = "浮动打印金额")
	private Integer plusShowPrice;

	@Column(length = 2, name = "gift_num")
	@FieldDesc(comment = "赠送数量")
	private Integer giftNum;

	@Column(length = 8, name = "status")
	@FieldDesc(comment = "记录结算状态", dic = "0211")
	private String status = "0211001";

	@Column(length = 20, name = "sale_no")
	@FieldDesc(comment = "销售编号")
	private String saleNo;

	@Column(length = 4, name = "print_num")
	@FieldDesc(comment = "用票数量")
	private Integer printNum = 0;

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
	public Integer getPlayId() {
		return playId;
	}
	public void setPlayId(Integer playId) {
		this.playId = playId;
	}

	public Integer getReserveId() {
		return reserveId;
	}
	public void setReserveId(Integer reserveId) {
		this.reserveId = reserveId;
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

	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Integer getOffsetPrice() {
		return offsetPrice;
	}
	public void setOffsetPrice(Integer offsetPrice) {
		this.offsetPrice = offsetPrice;
	}
	public Integer getCheapPrice() {
		return cheapPrice;
	}
	public void setCheapPrice(Integer cheapPrice) {
		this.cheapPrice = cheapPrice;
	}

	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
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

	public Date getPlayDate() {
		return playDate;
	}
	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

	public Date getBalanceTime() {
		return balanceTime;
	}
	public void setBalanceTime(Date balanceTime) {
		this.balanceTime = balanceTime;
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
	public String getPriceShowType() {
		return priceShowType;
	}
	public void setPriceShowType(String priceShowType) {
		this.priceShowType = priceShowType;
	}

	public String getIsShowSeat() {
		return isShowSeat;
	}
	public void setIsShowSeat(String isShowSeat) {
		this.isShowSeat = isShowSeat;
	}
	public String getHasTea() {
		return hasTea;
	}
	public void setHasTea(String hasTea) {
		this.hasTea = hasTea;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public Integer getPlusShowPrice() {
		return plusShowPrice;
	}
	public void setPlusShowPrice(Integer plusShowPrice) {
		this.plusShowPrice = plusShowPrice;
	}
	public Integer getGiftNum() {
		return giftNum;
	}
	public void setGiftNum(Integer giftNum) {
		this.giftNum = giftNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSaleNo() {
		return saleNo;
	}
	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}
	public Integer getPrintNum() {
		return printNum;
	}
	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public String getTheaterSn() {
		return theaterSn;
	}
	public void setTheaterSn(String theaterSn) {
		this.theaterSn = theaterSn;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
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
