package com.tjing.bussiness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_package_goods_view")
@org.hibernate.annotations.Table(appliesTo = "bi_package_goods_view", comment = "套餐商品对应表视图")
public class PackageGoodsView {
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "package_id")
	@FieldDesc(comment = "套餐ID", dic = "select id,name from bi_package where id=?")
	private Integer packageId;

	@Column(name = "goods_id")
	@FieldDesc(comment = "商品ID", dic = "select id,name from bi_goods where id=?")
	private Integer goodsId;

	@Column(name = "goods_count")
	@FieldDesc(comment = "商品数量")
	private Integer goodsCount;

	@Column(name = "goods_name")
	@FieldDesc(comment = "商品名称")
	private String goodsName;

	@Column(name = "package_name")
	@FieldDesc(comment = "套餐名称")
	private String packageName;

	@Column(name = "goods_price")
	@FieldDesc(comment = "商品单价")
	private Float goodsPrice;

	@Column(name = "goods_total")
	@FieldDesc(comment = "商品总价")
	private Float goodsTotal;

	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "create_time")
	@FieldDesc(comment = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "update_time")
	@FieldDesc(comment = "更新时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Column(name = "create_user_id")
	@FieldDesc(comment = "创建操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "update_user_id")
	@FieldDesc(comment = "更新操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer updateUserId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
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

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Float getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(Float goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

}
