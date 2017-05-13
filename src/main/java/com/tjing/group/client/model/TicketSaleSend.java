package com.tjing.group.client.model;

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
@Table(name = "bi_ticket_sale_send")
@org.hibernate.annotations.Table(appliesTo = "bi_ticket_sale_send", comment = "售票记录发送表")
public class TicketSaleSend implements ISendTable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;

	@Column(name = "source_id", nullable = false, unique = true, columnDefinition = "INT COMMENT '源记录ID'")
	private Integer sourceId;

	@Column(name = "send_status", length = 8, columnDefinition = "VARCHAR(8) DEFAULT '0234001' COMMENT '记录发送状态'")
	@FieldDesc(comment = "记录发送状态", dic = "0234")
	private String sendStatus;

	@Column(name = "send_time", columnDefinition = "DATETIME COMMENT '发送时间'")
	@FieldDesc(comment = "发送时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date sendTime;

	@Column(name = "send_count", columnDefinition = "INT COMMENT '发送次数'")
	private Integer sendCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

}
