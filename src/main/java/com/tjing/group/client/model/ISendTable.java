package com.tjing.group.client.model;

import java.util.Date;

public interface ISendTable {
	public Integer getId();

	public void setId(Integer id);

	public Integer getSourceId();

	public void setSourceId(Integer sourceId);

	public String getSendStatus();

	public void setSendStatus(String sendStatus);

	public Date getSendTime();

	public void setSendTime(Date sendTime);

	public Integer getSendCount();

	public void setSendCount(Integer sendCount);

}
