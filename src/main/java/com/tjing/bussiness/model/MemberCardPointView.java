package com.tjing.bussiness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_member_card_point_view")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_point_view", comment = "会员卡、消费卡、充值卡积分明细视图，补充卡信息和会员信息")
public class MemberCardPointView {
	@Id
	private Integer id;

	@FieldDesc(comment = "会员卡号")
	private String code;

	@Column(name = "card_type", length = 8, columnDefinition = "VARCHAR(8) COMMENT '卡类别'")
	@FieldDesc(comment = "卡类别", dic = "0216")
	private String cardType;

	@Column(name = "card_name", length = 8, columnDefinition = "VARCHAR(8) COMMENT '卡名称'")
	@FieldDesc(comment = "卡名称", dic = "0215")
	private String cardName;

	@Column(name = "member_id")
	@FieldDesc(comment = "会员ID", dic = "select id,name from bi_member where id=?")
	private Integer memberId;

	@Column(name = "member_name")
	@FieldDesc(comment = "姓名")
	private String memberName;

	@Column(name = "member_mobile")
	@FieldDesc(comment = "手机号码")
	private String memberMobile;

	@Column(name = "sale_no")
	@FieldDesc(comment = "销售编号")
	private String saleNo;

	@FieldDesc(comment = "类型", dic = "0232")
	private String type;

	@Column(name = "member_card_id")
	@FieldDesc(comment = "会员卡ID", dic = "select id,code from bi_member_card where id=?")
	private Integer cardId;

	@FieldDesc(comment = "积分值")
	private Integer point;

	@FieldDesc(comment = "本次积分后积分值")
	private Integer balance;

	@FieldDesc(comment = "消费场所", dic = "select code, text from tj_dic where code = ?")
	private String branch;

	@FieldDesc(comment = "兑换内容")
	private String content;

	@FieldDesc(comment = "状态", dic = "0202")
	private String status;

	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "create_time")
	@FieldDesc(comment = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "create_user_id")
	@FieldDesc(comment = "创建操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "update_time")
	@FieldDesc(comment = "更新时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Column(name = "update_user_id")
	@FieldDesc(comment = "更新操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer updateUserId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberMobile() {
		return memberMobile;
	}

	public void setMemberMobile(String memberMobile) {
		this.memberMobile = memberMobile;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
