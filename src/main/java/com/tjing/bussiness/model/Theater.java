package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_theater")
@org.hibernate.annotations.Table(appliesTo = "bi_theater", comment = "剧场")
public class Theater {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '剧场ID'")
	private Integer id;

	@Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '剧场名称'")
	@FieldDesc(comment = "剧场名称")
	private String name;
	
	@Column(name = "sn", length = 20, nullable = false, columnDefinition = "VARCHAR(20) COMMENT '序列号'")
	@FieldDesc(comment = "序列号")
	private String sn;
	
	@Column(length=7,name="stage_position")
	@FieldDesc(dic="0302",comment="舞台位置")
	private String stagePosition;
	
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
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public String getStagePosition() {
		return stagePosition;
	}

	public void setStagePosition(String stagePosition) {
		this.stagePosition = stagePosition;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
