package com.tjing.bussiness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_member_card_view")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_view", comment = "会员卡视图，包括卡信息和会员信息")
public class MemberCardView {
	@Id
	private Integer id;

	@FieldDesc(comment = "会员卡号")
	private String code;

	@Column(name = "member_id")
	@FieldDesc(comment = "会员ID", dic = "select id,name from bi_member where id=?")
	private Integer memberId;

	@FieldDesc(comment = "密码")
	private String password;

	@FieldDesc(comment = "卡名称", dic = "0215")
	private String name;

	@FieldDesc(comment = "卡类别", dic = "0216")
	private String type;

	@FieldDesc(comment = "卡状态", dic = "0218")
	private String status;

	@Column(name = "expiry_time")
	@FieldDesc(comment = "过期时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date expiryTime;

	@Column(name = "effective_time")
	@FieldDesc(comment = "生效时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date effectiveTime;

	@Column(name = "salesman_id")
	@FieldDesc(comment = "营销员ID", dic = "select id,name from bi_salesman where id=?")
	private Integer salesmanId;

	@FieldDesc(comment = "账户余额")
	private Float balance;

	@FieldDesc(comment = "积分")
	private Integer point;

	@Column(name = "last_rechargege_way")
	@FieldDesc(comment = "最后充值方式", dic = "0207")
	private String lastRechargeWay;

	@Column(name = "create_time")
	@FieldDesc(comment = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "update_time")
	@FieldDesc(comment = "更新时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Column(name = "create_user_id")
	@FieldDesc(comment = "创建操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "update_user_id")
	@FieldDesc(comment = "更新操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer updateUserId;

	@Column(name = "remark")
	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "member_name")
	@FieldDesc(comment = "姓名")
	private String memberName;

	@Column(name = "member_gender")
	@FieldDesc(comment = "性别", dic = "0102")
	private String memberGender;

	@Column(name = "member_mobile")
	@FieldDesc(comment = "手机号码")
	private String memberMobile;

	@Column(name = "member_telephone")
	@FieldDesc(comment = "固定电话")
	private String memberTelephone;

	@Column(name = "member_email")
	@FieldDesc(comment = "电子邮箱")
	private String memberEmail;

	@Column(name = "member_address")
	@FieldDesc(comment = "联系地址")
	private String memberAddress;

	@Column(name = "member_birthday")
	@FieldDesc(comment = "生日")
	@JSONField(format = "yyyy-MM-dd")
	private Date memberBirthday;

	@Column(name = "member_company")
	@FieldDesc(comment = "工作单位")
	private String memberCompany;

	@Column(name = "member_idcard_type")
	@FieldDesc(comment = "证件类别", dic = "0217")
	private String memberIdcardType;

	@Column(name = "member_idcard_no")
	@FieldDesc(comment = "证件号码")
	private String memberIdcardNo;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getLastRechargeWay() {
		return lastRechargeWay;
	}

	public void setLastRechargeWay(String lastRechargeWay) {
		this.lastRechargeWay = lastRechargeWay;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberGender() {
		return memberGender;
	}

	public void setMemberGender(String memberGender) {
		this.memberGender = memberGender;
	}

	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

	public String getMemberTelephone() {
		return memberTelephone;
	}

	public void setMemberTelephone(String memberTelephone) {
		this.memberTelephone = memberTelephone;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}

	public Date getMemberBirthday() {
		return memberBirthday;
	}

	public void setMemberBirthday(Date memberBirthday) {
		this.memberBirthday = memberBirthday;
	}

	public String getMemberCompany() {
		return memberCompany;
	}

	public void setMemberCompany(String memberCompany) {
		this.memberCompany = memberCompany;
	}

	public String getMemberIdcardType() {
		return memberIdcardType;
	}

	public void setMemberIdcardType(String memberIdcardType) {
		this.memberIdcardType = memberIdcardType;
	}

	public String getMemberIdcardNo() {
		return memberIdcardNo;
	}

	public void setMemberIdcardNo(String memberIdcardNo) {
		this.memberIdcardNo = memberIdcardNo;
	}

}
