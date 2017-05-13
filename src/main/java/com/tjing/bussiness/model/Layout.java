package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.tjing.frame.annotation.FieldDesc;
/**
 * 
 * @author coin
 * @todo 画出观众区域的大体布局
 */
@Entity
@Table(name = "bi_layout")
@org.hibernate.annotations.Table(appliesTo = "bi_layout", comment = "看台布局")
public class Layout {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '布局ID'")
	private Integer id;
	@Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(15) COMMENT '方位描述'")
	@FieldDesc(comment = "方位描述")
	private String name;
	@Column(name = "floor_id", nullable = false, length = 11, columnDefinition = "INT COMMENT '楼层ID'")
	@FieldDesc(comment = "楼层ID", dic = "select id,name from bi_floor where id=?")
	private Integer floorId;
	@Column(length = 3, nullable = false, columnDefinition = "INT(3) DEFAULT 1 COMMENT '排序'")
	@FieldDesc(comment = "排序")
	private Integer orderno;
	@Column(length = 7, columnDefinition = "VARCHAR(7) COMMENT '单元格位置'")
	@FieldDesc(comment = "单元格位置", dic = "0301")
	private String position;
	@Column(length = 1, columnDefinition = "INT COMMENT '单元格行跨'")
	@FieldDesc(comment = "单元格行跨")
	private String rowspan;
	@Column(length = 1, columnDefinition = "INT COMMENT '单元格列跨'")
	@FieldDesc(comment = "单元格列跨")
	private String colspan;
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

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

}
