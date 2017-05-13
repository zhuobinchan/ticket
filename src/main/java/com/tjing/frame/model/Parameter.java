package com.tjing.frame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "tj_parameter")
@org.hibernate.annotations.Table(appliesTo = "tj_parameter", comment = "参数配置表")
public class Parameter {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;

	@Column(length = 200, nullable = false, columnDefinition = "VARCHAR(200) COMMENT '参数类型'")
	@FieldDesc(comment = "参数类型")
	private String type;

	@Column(length = 50, nullable = false, columnDefinition = "VARCHAR(50) COMMENT '参数值'")
	@FieldDesc(comment = "参数值")
	private String value;

	@Column(length = 1000, nullable = false, columnDefinition = "VARCHAR(1000) COMMENT '配置值1'")
	@FieldDesc(comment = "配置值1")
	private String config1;

	@Column(length = 1000, columnDefinition = "VARCHAR(1000) COMMENT '配置值2'")
	@FieldDesc(comment = "配置值2")
	private String config2;

	@Column(length = 1000, columnDefinition = "VARCHAR(1000) COMMENT '配置值3'")
	@FieldDesc(comment = "配置值3")
	private String config3;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConfig1() {
		return config1;
	}

	public void setConfig1(String config1) {
		this.config1 = config1;
	}

	public String getConfig2() {
		return config2;
	}

	public void setConfig2(String config2) {
		this.config2 = config2;
	}

	public String getConfig3() {
		return config3;
	}

	public void setConfig3(String config3) {
		this.config3 = config3;
	}
}
