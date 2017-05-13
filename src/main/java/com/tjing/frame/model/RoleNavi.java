package com.tjing.frame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;
@Entity
@Table(name="TJ_ROLE_NAVI")
@org.hibernate.annotations.Table(appliesTo="TJ_ROLE_NAVI",comment="角色权限表")
public class RoleNavi {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length=6,name="navi_id")
	@FieldDesc(comment="菜单ID")
	private Integer naviId;
	@Column(length=20,name="user_name")
	@FieldDesc(comment="菜单名")
	private String naviName;
	@Column(length=4,name="role_id")
	@FieldDesc(comment="角色ID")
	private Integer roleId;
	@Column(length=50,name="role_name")
	@FieldDesc(comment="角色名称")
	private String roleName;
	@Column(length=50,name="role_desc")
	@FieldDesc(comment="角色描述")
	private String roleDesc;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNaviId() {
		return naviId;
	}
	public void setNaviId(Integer naviId) {
		this.naviId = naviId;
	}
	public String getNaviName() {
		return naviName;
	}
	public void setNaviName(String naviName) {
		this.naviName = naviName;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	
}
