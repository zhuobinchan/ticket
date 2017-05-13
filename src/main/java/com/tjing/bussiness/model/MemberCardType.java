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
@Table(name = "bi_member_card_type")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_type", comment = "会员卡类别信息")
public class MemberCardType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '卡ID'")
	private Integer id;

	@Column(length = 8, nullable = false, unique = true, columnDefinition = "VARCHAR(8) COMMENT '会员卡名称编号'")
	@FieldDesc(comment = "会员卡名称编号", dic = "0215")
	private String name;

	@Column(length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '会员卡类别编号'")
	@FieldDesc(comment = "会员卡类别编号", dic = "0216")
	private String type;

	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '充值累积金额'")
	@FieldDesc(comment = "充值累积金额")
	private Float cumulation;

	@Column(name = "ticket_discount", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '购票折扣'")
	@FieldDesc(comment = "购票折扣")
	private Float ticketDiscount;

	@Column(name = "ticket_reduce", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '购票减免'")
	@FieldDesc(comment = "购票减免")
	private Float ticketReduce;

	@Column(name = "ticket_algorithm", columnDefinition = "VARCHAR(1000) COMMENT '购票优惠算法'")
	@FieldDesc(comment = "购票优惠算法")
	private Float ticketAlgorithm;

	@Column(name = "ticket_point_method", length = 8, nullable = false, columnDefinition = "VARCHAR(8) DEFAULT '0233001' COMMENT '购票积分方法'")
	@FieldDesc(comment = "购票积分方法", dic = "0233")
	private String ticketPointMethod;

	@Column(name = "ticket_point_ratio", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 1 COMMENT '购票积分比率'")
	@FieldDesc(comment = "购票积分比率")
	private Float ticketPointRatio;

	@Column(name = "venue_discount", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '场内消费折扣'")
	@FieldDesc(comment = "场内消费折扣")
	private Float venueDiscount;

	@Column(name = "venue_reduce", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '场内消费减免'")
	@FieldDesc(comment = "场内消费减免")
	private Float venueReduce;

	@Column(name = "venue_algorithm", columnDefinition = "VARCHAR(1000) COMMENT '场内消费优惠算法'")
	@FieldDesc(comment = "场内消费优惠算法")
	private Float venueAlgorithm;

	@Column(name = "venue_point_method", length = 8, nullable = false, columnDefinition = "VARCHAR(8) DEFAULT '0233001' COMMENT '场内消费积分方法'")
	@FieldDesc(comment = "场内消费积分方法", dic = "0233")
	private String venuePointMethod;

	@Column(name = "venue_point_ratio", nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 1 COMMENT '场内消费积分比率'")
	@FieldDesc(comment = "场内消费积分比率")
	private Float venuePointRatio;

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

	public Integer getId() {
		return id;
	}

	public Float getCumulation() {
		return cumulation;
	}

	public void setCumulation(Float cumulation) {
		this.cumulation = cumulation;
	}

	public Float getTicketDiscount() {
		return ticketDiscount;
	}

	public void setTicketDiscount(Float ticketDiscount) {
		this.ticketDiscount = ticketDiscount;
	}

	public Float getVenueDiscount() {
		return venueDiscount;
	}

	public void setVenueDiscount(Float venueDiscount) {
		this.venueDiscount = venueDiscount;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getTicketReduce() {
		return ticketReduce;
	}

	public void setTicketReduce(Float ticketReduce) {
		this.ticketReduce = ticketReduce;
	}

	public Float getTicketAlgorithm() {
		return ticketAlgorithm;
	}

	public void setTicketAlgorithm(Float ticketAlgorithm) {
		this.ticketAlgorithm = ticketAlgorithm;
	}

	public String getTicketPointMethod() {
		return ticketPointMethod;
	}

	public void setTicketPointMethod(String ticketPointMethod) {
		this.ticketPointMethod = ticketPointMethod;
	}

	public Float getTicketPointRatio() {
		return ticketPointRatio;
	}

	public void setTicketPointRatio(Float ticketPointRatio) {
		this.ticketPointRatio = ticketPointRatio;
	}

	public Float getVenueReduce() {
		return venueReduce;
	}

	public void setVenueReduce(Float venueReduce) {
		this.venueReduce = venueReduce;
	}

	public Float getVenueAlgorithm() {
		return venueAlgorithm;
	}

	public void setVenueAlgorithm(Float venueAlgorithm) {
		this.venueAlgorithm = venueAlgorithm;
	}

	public String getVenuePointMethod() {
		return venuePointMethod;
	}

	public void setVenuePointMethod(String venuePointMethod) {
		this.venuePointMethod = venuePointMethod;
	}

	public Float getVenuePointRatio() {
		return venuePointRatio;
	}

	public void setVenuePointRatio(Float venuePointRatio) {
		this.venuePointRatio = venuePointRatio;
	}

}
