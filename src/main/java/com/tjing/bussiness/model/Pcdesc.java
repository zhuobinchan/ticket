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
@Table(name = "bi_pcdesc")
@org.hibernate.annotations.Table(appliesTo = "bi_pcdesc", comment = "维护电脑编号和流水号的关系")
public class Pcdesc {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;
	@Column(length=5,name="pccode")
	@FieldDesc(comment="电脑编号")//每天更新
	private String pccode;
	@Column(length=5,name="snno")
	@FieldDesc(comment="流水号数字")//每天更新
	private Integer snno = 0;
	
	@Column(name="create_date")
	@JSONField(format="yyyy-MM-dd")
	@FieldDesc(comment="创建日期")
	private Date createDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPccode() {
		return pccode;
	}
	public void setPccode(String pccode) {
		this.pccode = pccode;
	}
	public Integer getSnno() {
		return snno;
	}
	public void setSnno(Integer snno) {
		this.snno = snno;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
