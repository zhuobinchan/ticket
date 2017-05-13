package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="bi_area")
@org.hibernate.annotations.Table(appliesTo="bi_area",comment="区域设置")
public class Area {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="name",length=20)
	@FieldDesc(comment="区域名称")
	private String name;
	@Column(length=20)
	@FieldDesc(comment="区域说明")
	private String descs;
	@Column(name="floor_num",length=3)
	@FieldDesc(comment="楼层数")
	private String floorNum; //楼层，加座
	@Column(name="default_num",length=4)
	@FieldDesc(comment="默认人数")
	private Integer defaultNum;
	@Column(name="from_row_num",length=3)
	@FieldDesc(comment="起始行")
	private Integer fromRowNum;
	@Column(name="from_column_num",length=3)
	@FieldDesc(comment="起始列")
	private Integer fromColumnNum;
	@Column(name="total_row_num",length=3)
	@FieldDesc(comment="行数")
	private Integer totalRowNum;
	@Column(name="total_column_num",length=3)
	@FieldDesc(comment="列数")
	private Integer totalColumnNum;
	@Column(name="price",length=5)
	@FieldDesc(comment="价格")
	private Integer price;
	@Column(name="code",length=15)
	@FieldDesc(comment="编号")
	private String code;
	@Column(name="belongto",length=1)
	@FieldDesc(comment="属于")
	private Integer belongto;
	@Column(name="mark",length=1)
	@FieldDesc(comment="输出标记")
	private Integer mark; //是否输出排号

	@Column(name = "block_id", length = 11, columnDefinition = "INT COMMENT '剧场分块ID'")
	@FieldDesc(comment = "剧场分块ID", dic = "select id,name from bi_block where id=?")
	private Integer blockId;

	@Column(length = 3, nullable = false, columnDefinition = "INT(3) DEFAULT 1 COMMENT '排序'")
	@FieldDesc(comment = "块内排序")
	private Integer orders;
	
	@Column(length = 3, nullable = false, columnDefinition = "INT(3) DEFAULT 1 COMMENT '排序'")
	@FieldDesc(comment = "排序")
	private Integer orderno;

	@Column(name="background_color",length = 7, columnDefinition = "VARCHAR(7) COMMENT '背景颜色'")
	@FieldDesc(comment = "背景颜色")
	private String backgroundColor;

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
	
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public String getFloorNum() {
		return floorNum;
	}
	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}
	public Integer getDefaultNum() {
		return defaultNum;
	}
	public void setDefaultNum(Integer defaultNum) {
		this.defaultNum = defaultNum;
	}
	public Integer getFromRowNum() {
		return fromRowNum;
	}
	public void setFromRowNum(Integer fromRowNum) {
		this.fromRowNum = fromRowNum;
	}
	public Integer getFromColumnNum() {
		return fromColumnNum;
	}
	public void setFromColumnNum(Integer fromColumnNum) {
		this.fromColumnNum = fromColumnNum;
	}
	public Integer getTotalRowNum() {
		return totalRowNum;
	}
	public void setTotalRowNum(Integer totalRowNum) {
		this.totalRowNum = totalRowNum;
	}
	public Integer getTotalColumnNum() {
		return totalColumnNum;
	}
	public void setTotalColumnNum(Integer totalColumnNum) {
		this.totalColumnNum = totalColumnNum;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getBelongto() {
		return belongto;
	}
	public void setBelongto(Integer belongto) {
		this.belongto = belongto;
	}
	public Integer getMark() {
		return mark;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public Integer getBlockId() {
		return blockId;
	}
	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	
}
