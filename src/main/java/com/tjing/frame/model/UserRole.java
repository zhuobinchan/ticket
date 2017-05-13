package com.tjing.frame.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;
@Entity
@Table(name="TJ_USER_ROLE")
@org.hibernate.annotations.Table(appliesTo="TJ_USER_ROLE",comment="用户角色表")
public class UserRole {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length=6,name="user_id")
	@FieldDesc(comment="用户ID")
	private Integer userId;
	@Column(length=20,name="user_name")
	@FieldDesc(comment="用户名")
	private String userName;
	@Column(length=24,name="role_id")
	@FieldDesc(comment="角色ID")
	private Integer roleId;
	@Column(length=4,name="role_name")
	@FieldDesc(comment="角色名称")
	private String roleName;
	@Column(length=4,name="role_desc")
	@FieldDesc(comment="角色描述")
	private String roleDesc;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
