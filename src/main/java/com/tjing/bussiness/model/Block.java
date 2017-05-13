package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_block")
@org.hibernate.annotations.Table(appliesTo = "bi_block", comment = "剧场楼层分区分块（按列）")
public class Block {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '分区ID'")
	private Integer id;

	@Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '分块名称'")
	@FieldDesc(comment = "分块名称")
	private String name;

	@Column(name = "partition_id", nullable = false, length = 11, columnDefinition = "INT COMMENT '剧场分区ID'")
	@FieldDesc(comment = "剧场分区ID", dic = "select id,name from bi_partition where id=?")
	private Integer partitionId;

	@Column(length = 3, nullable = false, columnDefinition = "INT(3) DEFAULT 1 COMMENT '排序'")
	@FieldDesc(comment = "排序")
	private Integer orders;

	@Column(name = "remark", length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	@Column(length = 200, columnDefinition = "VARCHAR(200) COMMENT '样式'")
	@FieldDesc(comment = "样式")
	private String style;

	@Column(length = 1000, columnDefinition = "VARCHAR(200) COMMENT '内容'")
	@FieldDesc(comment = "内容")
	private String content;

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

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
