package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="bi_show_number")
@org.hibernate.annotations.Table(appliesTo="bi_show_number",comment="演出场次")
public class ShowNumber {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="name",length=20)
	@FieldDesc(comment="场次描述")
	private String name;
	@Column(length=8,name="status")
	@FieldDesc(comment="状态",dic="0202")
	private String status;
	@Column(length=3,name="orderno")
	@FieldDesc(comment="排序")
	private Integer orderno;
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
	
}
