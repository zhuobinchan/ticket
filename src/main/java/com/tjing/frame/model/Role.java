package com.tjing.frame.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="TJ_ROLE")
public class Role implements Serializable{
	private static final long serialVersionUID = 6190142645569767158L;
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=40)
	private Integer id;
	@Column(length=50)
	@FieldDesc(comment="角色名称")
	private String name;
	@Column(length=300)
	@FieldDesc(comment="描述")
	private String descs;
	
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
}
