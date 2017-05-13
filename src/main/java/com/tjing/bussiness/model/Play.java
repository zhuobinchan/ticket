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
@Table(name="bi_play")
@org.hibernate.annotations.Table(appliesTo="bi_play",comment="演出剧目")
public class Play {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="name",length=20)
	@FieldDesc(comment="剧目名称")
	private String name;
	@Column(length=8,name="status")
	@FieldDesc(comment="状态",dic="0204")
	private String status;
	@Column(name="play_date")
	@FieldDesc(comment="演出日期")
	@JSONField(format="yyyy-MM-dd")
	private Date playDate;
	@Column(length=2,name="hh")
	@FieldDesc(comment="演出时")
	private Integer hh;
	@Column(length=2,name="mi")
	@FieldDesc(comment="演出分")
	private Integer mi;
	@Column(name="create_time")
	@FieldDesc(comment="创建时间")
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date createTime;
	@Column(name="create_user_id")
	@FieldDesc(comment="创建人ID",dic="select id,name from tj_user where id=?")
	private Integer createUserId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getPlayDate() {
		return playDate;
	}
	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}
	
	public Integer getHh() {
		return hh;
	}
	public void setHh(Integer hh) {
		this.hh = hh;
	}
	public Integer getMi() {
		return mi;
	}
	public void setMi(Integer mi) {
		this.mi = mi;
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
	
}
