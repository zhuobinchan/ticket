package com.tjing.frame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.tjing.frame.annotation.FieldDesc;

/**
 * 组织架构
 * @author zengping
 */
@Entity
@Table(name="TJ_ORGANIZATION")
@org.hibernate.annotations.Table(appliesTo="TJ_ORGANIZATION",comment="机构表")
public class Organization implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=40)
	private Integer id;
	@Column(length=40,name="PARENT_ID")
	@FieldDesc(comment="上级机构")
	private Integer parentId;
    @Column(length=100)
    @FieldDesc(comment="机构名称")
    private String name;
    @FieldDesc(comment="层级")
    private Integer tier;
    @Column(length=100,name="full_name")
    @FieldDesc(comment="机构全称")
    private String fullName;
    @FieldDesc(comment="排序")
    private Integer orderno;
    @FieldDesc(dic="000101",comment="机构类型")
    private String orgType;
    @FieldDesc(dic="201101",comment="业务类型")
    private String busyType;//业务类型，水电、火电
    @Transient
    private List<Organization> orgs = new ArrayList<Organization>();
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTier() {
		return tier;
	}
	public void setTier(Integer tier) {
		this.tier = tier;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getBusyType() {
		return busyType;
	}
	public void setBusyType(String busyType) {
		this.busyType = busyType;
	}
	public List<Organization> getOrgs() {
		return orgs;
	}
	public void setOrgs(List<Organization> orgs) {
		this.orgs = orgs;
	}
    
	
	
}