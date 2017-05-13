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
@Table(name = "bi_member_card_point_exchange")
@org.hibernate.annotations.Table(appliesTo = "bi_member_card_point_exchange", comment = "会员卡、消费卡、充值卡积分兑换表")
public class MemberCardPointExchange {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;

	@Column(name = "member_card_id", nullable = false, columnDefinition = "INT COMMENT '会员卡ID'")
	@FieldDesc(comment = "会员卡ID", dic = "select id,code from bi_member_card where id=?")
	private Integer cardId;

	@Column(nullable = false, columnDefinition = "INT DEFAULT 0 COMMENT '兑换积分值'")
	@FieldDesc(comment = "兑换积分值")
	private Integer point;

	@Column(nullable = false, columnDefinition = "INT DEFAULT 0 COMMENT '兑换后积分值'")
	@FieldDesc(comment = "兑换后积分值")
	private Integer balance;

	@Column(length = 100, columnDefinition = "VARCHAR(100) COMMENT '兑换内容'")
	@FieldDesc(comment = "兑换内容")
	private String content;

	@Column(length = 8, columnDefinition = "VARCHAR(8) COMMENT '兑换场所'")
	@FieldDesc(comment = "兑换场所", dic = "0220")
	private String branch;

	@Column(length = 8, nullable = false, columnDefinition = "VARCHAR(8) DEFAULT '0202001' COMMENT '状态'")
	@FieldDesc(comment = "状态", dic = "0202")
	private String status;

	@Column(length = 200, columnDefinition = "VARCHAR(200) COMMENT '备注'")
	@FieldDesc(comment = "备注")
	private String remark;

	@Column(name = "create_time", nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
	@FieldDesc(comment = "创建时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Column(name = "create_user_id", nullable = false, columnDefinition = "INT COMMENT '创建操作员ID'")
	@FieldDesc(comment = "创建操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer createUserId;

	@Column(name = "update_time", nullable = false, columnDefinition = "DATETIME COMMENT '更新时间'")
	@FieldDesc(comment = "更新时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@Column(name = "update_user_id", nullable = false, columnDefinition = "INT COMMENT '更新操作员ID'")
	@FieldDesc(comment = "更新操作员ID", dic = "select id,name from tj_user where id=?")
	private Integer updateUserId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}
