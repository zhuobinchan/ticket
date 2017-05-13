package com.tjing.frame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="tj_field_log")
@org.hibernate.annotations.Table(appliesTo="tj_field_log",comment="字段修改日志")
public class FieldLog {
	@Id
	@Column(length=40)
	private String id;
	@Column(length=40,name="oid")
	@FieldDesc(comment="主表id")
	private String oid;
	@Column(length=100,name="field_name")
	@FieldDesc(comment="字段名称")
	private String fieldName;
	@Column(length=500,name="old_value")
	@FieldDesc(comment="旧值")
	private String oldValue;
	@Column(length=500)
	@FieldDesc(comment="新值",name="new_value")
	private String newValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
}
