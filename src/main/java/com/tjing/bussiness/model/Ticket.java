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
@Table(name="bi_ticket")
@org.hibernate.annotations.Table(appliesTo="bi_ticket",comment="门票")
public class Ticket {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length=11,name="parent_id")
	@FieldDesc(comment="原门票ID")
	private Integer parentId;
	@Column(length=30,name="number")
	@FieldDesc(comment="门票号码")
	private String number;
	@Column(length=11,name="number_id")
	@FieldDesc(comment="库存ID")
	private Integer numberId;
	@Column(length=11,name="show_number_id")
	@FieldDesc(comment="场次ID",dic="select id,name from bi_show_number where id=?")
	private Integer showNumberId;
	@Column(length=11,name="check_user_id")
	@FieldDesc(comment="检票人ID")
	private Integer checkUserId;
	@Column(length=11,name="sale_id")
	@FieldDesc(comment="销售ID")
	private Integer saleId;
	@Column(length=11,name="reserve_id")
	@FieldDesc(comment="预订ID")
	private Integer reserveId;
	@Column(length=11,name="area_id")
	@FieldDesc(comment="片区ID")
	private Integer areaId;
	@Column(length=25,name="area_name")
	@FieldDesc(comment="片区名称")
	private String areaName;
	@Column(length=11,name="seat_id")
	@FieldDesc(comment="座位ID")
	private Integer seatId;
	@Column(length=15,name="seat_name")
	@FieldDesc(comment="座位名称")
	private String seatName;
	@Column(length=11,name="strategy_id")
	@FieldDesc(comment="门票策略ID")
	private Integer strategyId;
	@Column(length=50,name="strategy_name")
	@FieldDesc(comment="门票策略名称")
	private String strategyName;
	@Column(length=11,name="price")
	@FieldDesc(comment="门票原价")
	private Integer price;
	@Column(length=11,name="real_price")
	@FieldDesc(comment="门票实价")
	private Integer realPrice;
	@Column(length=5,name="total_num")
	@FieldDesc(comment="总人数")
	private Integer totalNum = 1;
	@Column(length=5,name="allow_num")
	@FieldDesc(comment="剩余人数")
	private Integer allowNum = 1;
	@Column(length=8,name="status")
	@FieldDesc(comment="状态",dic="0203")
	private String status;
	@Column(name="play_date")
	@FieldDesc(comment="演出日期")
	@JSONField(format="yyyy-MM-dd")
	private Date playDate;
	@Column(name="play_hh")
	@FieldDesc(comment="演出时")
	private Integer playHh;
	@Column(name="play_mi")
	@FieldDesc(comment="演出分")
	private Integer playMi;
	@Column(name="in_time")
	@FieldDesc(comment="检票时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date inTime;
	@Column(name="create_time")
	@FieldDesc(comment="创建时间")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Column(name="create_user_id")
	@FieldDesc(comment="创建人ID",dic="select id,name from tj_user where id=?")
	private Integer createUserId;
	@Column(name="theater_sn",length=20)
	@FieldDesc(comment="剧院编号")
	private String theaterSn;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Integer getNumberId() {
		return numberId;
	}
	public void setNumberId(Integer numberId) {
		this.numberId = numberId;
	}
	
	public Integer getShowNumberId() {
		return showNumberId;
	}
	public void setShowNumberId(Integer showNumberId) {
		this.showNumberId = showNumberId;
	}
	
	public Integer getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(Integer checkUserId) {
		this.checkUserId = checkUserId;
	}
	public Integer getSaleId() {
		return saleId;
	}
	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}
	
	public Integer getReserveId() {
		return reserveId;
	}
	public void setReserveId(Integer reserveId) {
		this.reserveId = reserveId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getSeatId() {
		return seatId;
	}
	public void setSeatId(Integer seatId) {
		this.seatId = seatId;
	}
	public String getSeatName() {
		return seatName;
	}
	public void setSeatName(String seatName) {
		this.seatName = seatName;
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
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(Integer realPrice) {
		this.realPrice = realPrice;
	}
	
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getAllowNum() {
		return allowNum;
	}
	public void setAllowNum(Integer allowNum) {
		this.allowNum = allowNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getPlayDate() {
		return playDate;
	}
	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}
	public Integer getPlayHh() {
		return playHh;
	}
	public void setPlayHh(Integer playHh) {
		this.playHh = playHh;
	}
	public Integer getPlayMi() {
		return playMi;
	}
	public void setPlayMi(Integer playMi) {
		this.playMi = playMi;
	}
	
	public Date getInTime() {
		return inTime;
	}
	public void setInTime(Date inTime) {
		this.inTime = inTime;
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
	public String getTheaterSn() {
		return theaterSn;
	}
	public void setTheaterSn(String theaterSn) {
		this.theaterSn = theaterSn;
	}
	
}
