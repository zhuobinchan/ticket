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
@Table(name = "bi_goods")
@org.hibernate.annotations.Table(appliesTo = "bi_goods", comment = "商品")
public class Goods {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '商品ID'")
	private Integer id;

	@Column(name = "type_id", nullable = false, columnDefinition = "INT COMMENT '类别ID'")
	@FieldDesc(comment = "类别ID", dic = "select id,name from bi_goods_type where id=?")
	private Integer typeId;

	@Column(length = 50, nullable = false, columnDefinition = "VARCHAR(50) COMMENT '商品名称'")
	@FieldDesc(comment = "商品名称")
	private String name;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '单位'")
	@FieldDesc(comment = "单位", dic = "0222")
	private String unit;

	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '单价'")
	@FieldDesc(comment = "单价")
	private Float price;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '是否打折'")
	@FieldDesc(comment = "是否打折", dic = "0223")
	private String discount;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '是否配送'")
	@FieldDesc(comment = "是否配送", dic = "0223")
	private String dispatch;

	@Column(length = 50, columnDefinition = "VARCHAR(50) COMMENT '编码，与仓库一致'")
	@FieldDesc(comment = "编码")
	private String code;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '是否加工'")
	@FieldDesc(comment = "是否加工", dic = "0223")
	private String process;

	@Column(columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '成本价'")
	@FieldDesc(comment = "成本价")
	private Float cost;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '出品区域'")
	@FieldDesc(comment = "出品区域", dic = "0224")
	private String area;

	@Column(nullable = false, length = 8, columnDefinition = "VARCHAR(8) DEFAULT '0229001' COMMENT '提成比例'")
	@FieldDesc(comment = "提成比例", dic = "0229")
	private String commision;

	@Column(name = "bar_code", length = 100, columnDefinition = "VARCHAR(100) COMMENT '条形码'")
	@FieldDesc(comment = "条形码")
	private String barCode;

	@Column(length = 3, nullable = false, columnDefinition = "INT(3) COMMENT '排序'")
	@FieldDesc(comment = "排序")
	private Integer orderno;

	@Column(length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "status", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '状态'")
	@FieldDesc(comment = "状态", dic = "0202")
	private String status;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public String getCommision() {
		return commision;
	}

	public void setCommision(String commision) {
		this.commision = commision;
	}

}
