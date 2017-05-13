package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_floor")
@org.hibernate.annotations.Table(appliesTo = "bi_floor", comment = "剧场楼层")
public class Floor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '楼层ID'")
	private Integer id;

	@Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '楼层名称'")
	@FieldDesc(comment = "楼层名称")
	private String name;

	@Column(name = "theater_id", nullable = false, length = 11, columnDefinition = "INT COMMENT '剧场ID'")
	@FieldDesc(comment = "剧场ID", dic = "select id,name from bi_theater where id=?")
	private Integer theaterId;

	@Column(length = 3, nullable = false, columnDefinition = "INT(3) DEFAULT 1 COMMENT '排序'")
	@FieldDesc(comment = "排序")
	private Integer orders;
	
	@Column(length = 7,columnDefinition = "VARCHAR(7) COMMENT '默认显示'")
	@FieldDesc(comment = "默认显示",dic="0103")
	private String defaults;

	@Column(name = "remark", length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTheaterId() {
		return theaterId;
	}

	public void setTheaterId(Integer theaterId) {
		this.theaterId = theaterId;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}


}
