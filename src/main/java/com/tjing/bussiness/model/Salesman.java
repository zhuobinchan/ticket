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
@Table(name="bi_salesman")
@org.hibernate.annotations.Table(appliesTo="bi_salesman",comment="营销管理")
public class Salesman {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="code",length=20)
	@FieldDesc(comment="营销编号")
	private String code;
	@Column(name="name",length=20)
	@FieldDesc(comment="人员名称")
	private String name;
	@Column(name="mobileno",length=40)
	@FieldDesc(comment="手机号码")
	private String mobileno;
	@Column(name="dept_id",length=11)
	@FieldDesc(comment="所属部门",dic="select id,name from tj_organization where id=?")
	private Integer deptId;
	@Column(name="org_id",length=11)
	@FieldDesc(comment="所属单位",dic="select id,name from tj_organization where id=?")
	private Integer orgId;
	@Column(name="commision_rate",length=3)
	@FieldDesc(comment="提成比例")
	private Integer commisionRate;
	@Column(name="status",length=8)
	@FieldDesc(comment="记录状态",dic="0202")
	private String status;
	@Column(name="join_date")
	@FieldDesc(comment="入职日期")
	@JSONField(format="yyyy-MM-dd")
	private Date joinDate;
	@Column(length=8, columnDefinition = "VARCHAR(8) DEFAULT '0228001' COMMENT '员工类别'")
	@FieldDesc(comment="员工类别",dic="0228")
	private String type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	public Integer getCommisionRate() {
		return commisionRate;
	}
	public void setCommisionRate(Integer commisionRate) {
		this.commisionRate = commisionRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
