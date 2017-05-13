package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_partition")
@org.hibernate.annotations.Table(appliesTo = "bi_partition", comment = "剧场楼层分区（按行）")
public class Partition {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '分区ID'")
	private Integer id;

	@Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '分区名称'")
	@FieldDesc(comment = "分区名称")
	private String name;

	@Column(name = "floor_id", nullable = false, length = 11, columnDefinition = "INT COMMENT '剧场楼层ID'")
	@FieldDesc(comment = "剧场楼层ID", dic = "select id,name from bi_floor where id=?")
	private Integer floorId;

	@Column(length = 3, nullable = false, columnDefinition = "INT(3) DEFAULT 1 COMMENT '排序'")
	@FieldDesc(comment = "排序")
	private Integer orders;

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

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

}
