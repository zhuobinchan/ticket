package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="bi_seat")
@org.hibernate.annotations.Table(appliesTo="bi_seat",comment="座位设置")
public class Seat {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="area_id",length=20)
	@FieldDesc(comment="区域ID")
	private Integer areaId;
	@Column(name="area_code",length=10)
	@FieldDesc(comment="区域编号")
	private String areaCode;
	@Column(name="name",length=20)
	@FieldDesc(comment="座位名称")
	private String name;
	@Column(name="mapped_name",length=20)
	@FieldDesc(comment="显示名称")
	private String mappedName;
	@Column(name="row_num",length=3)
	@FieldDesc(comment="行数")
	private Integer rowNum;
	@Column(name="column_num",length=3)
	@FieldDesc(comment="列数")
	private Integer columnNum;
	@Column(name="price",length=3)
	@FieldDesc(comment="价格")
	private Integer price;

	@Column(length = 8, nullable = false, columnDefinition = "VARCHAR(8) DEFAULT '0230001' COMMENT '类别'")
	@FieldDesc(comment = "类别", dic = "0230")
	private String type;

	@Column(length = 200, columnDefinition = "VARCHAR(200) COMMENT '样式'")
	@FieldDesc(comment = "样式")
	private String style;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMappedName() {
		return mappedName;
	}
	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}
	public Integer getRowNum() {
		return rowNum;
	}
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	public Integer getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
}
