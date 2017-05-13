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
@Table(name = "bi_member")
@org.hibernate.annotations.Table(appliesTo = "bi_member", comment = "会员资料")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '会员ID'")
	private Integer id;

	@Column(name = "name", length = 100, nullable = false, columnDefinition = "VARCHAR(100) COMMENT '姓名'")
	@FieldDesc(comment = "姓名")
	private String name;

	@Column(name = "gender", length = 8, columnDefinition = "VARCHAR(8) COMMENT '性别'")
	@FieldDesc(comment = "性别", dic = "0102")
	private String gender;

	@Column(name = "mobileno", length = 40, columnDefinition = "VARCHAR(40) COMMENT '手机号码'")
	@FieldDesc(comment = "手机号码")
	private String mobileno;

	@Column(name = "telephone", length = 40, columnDefinition = "VARCHAR(40) COMMENT '固定电话'")
	@FieldDesc(comment = "固定电话")
	private String telephone;

	@Column(name = "email", length = 100, columnDefinition = "VARCHAR(100) COMMENT '电子邮箱'")
	@FieldDesc(comment = "电子邮箱")
	private String email;

	@Column(name = "address", length = 140, columnDefinition = "VARCHAR(140) COMMENT '联系地址'")
	@FieldDesc(comment = "联系地址")
	private String address;

	@Column(name = "birthday", columnDefinition = "DATETIME COMMENT '联系地址'")
	@FieldDesc(comment = "生日")
	@JSONField(format = "yyyy-MM-dd")
	private Date birthday;

	@Column(name = "company", length = 100, columnDefinition = "VARCHAR(100) COMMENT '工作单位'")
	@FieldDesc(comment = "工作单位")
	private String company;

	@Column(name = "idcard_type", length = 8, columnDefinition = "VARCHAR(8) COMMENT '证件类别'")
	@FieldDesc(comment = "证件类别", dic = "0217")
	private String idcardType;

	@Column(name = "idcard_no", length = 40, columnDefinition = "VARCHAR(40) COMMENT '证件号码'")
	@FieldDesc(comment = "证件号码")
	private String idcardNo;

	@Column(name = "hobby", length = 200, columnDefinition = "VARCHAR(200) COMMENT '爱好'")
	@FieldDesc(comment = "爱好")
	private String hobby;

	@Column(name = "remark", length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "status", length = 8, nullable = false, columnDefinition = "VARCHAR(8) COMMENT '会员状态'")
	@FieldDesc(comment = "会员状态", dic = "0202")
	private String status;

	@Column(name = "create_time", nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
	@FieldDesc(comment = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "update_time", nullable = false, columnDefinition = "DATETIME COMMENT '更新时间'")
	@FieldDesc(comment = "更新时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Column(name = "create_user_id", nullable = false, columnDefinition = "INT COMMENT '创建操作员ID'")
	@FieldDesc(comment = "创建操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "update_user_id", nullable = false, columnDefinition = "INT COMMENT '更新操作员ID'")
	@FieldDesc(comment = "更新操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer updateUserId;

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

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

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIdcardType() {
		return idcardType;
	}

	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
