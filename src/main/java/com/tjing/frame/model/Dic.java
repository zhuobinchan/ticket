package com.tjing.frame.model;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tjing.frame.annotation.FieldDesc;
@Entity
@Table(name="tj_dic")
@org.hibernate.annotations.Table(appliesTo="tj_dic",comment="数据词典")
public class Dic{
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=40)
	private Integer id;
	@Column(length=40,name="parent_id")
	@FieldDesc(comment="父节点ID")
	protected Integer parentId;
	@Column(length=8)
	@FieldDesc(comment="词条索引")
	protected String code;
	@Column(length=200)
	@FieldDesc(comment="词条解释")
	protected String text;
	@Column(length=3)
	@FieldDesc(comment="排序")
	protected Integer orderno;
	@Column(length=3)
	@FieldDesc(comment="层级")
	protected Integer tier;
	@Transient
	private LinkedHashMap<String,Dic> dicMap = new LinkedHashMap<String,Dic>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public LinkedHashMap<String, Dic> getDicMap() {
		return dicMap;
	}

	public void setDicMap(LinkedHashMap<String, Dic> dicMap) {
		this.dicMap = dicMap;
	}
	
}
