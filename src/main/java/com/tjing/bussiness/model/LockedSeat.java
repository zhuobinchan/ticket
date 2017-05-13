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
@Table(name = "bi_locked_seat")
@org.hibernate.annotations.Table(appliesTo = "bi_locked_seat", comment = "锁定的座位")
public class LockedSeat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;
	
	@Column(name = "seat_id", nullable = false, length = 11, columnDefinition = "INT COMMENT '座位ID'")
	@FieldDesc(comment = "座位ID", dic = "select id,name from bi_seat where id=?")
	private Integer seatId;
	
	@Column(name="row_num",length=3)
	@FieldDesc(comment="行数")
	private Integer rowNum;
	
	@Column(name="column_num",length=3)
	@FieldDesc(comment="列数")
	private Integer columnNum;
	
	@Column(name="area_code",length = 10, nullable = false, columnDefinition = "VARCHAR(10) COMMENT '区域编号'")
	@FieldDesc(comment = "区域编号")
	private String areaCode;
	
	@Column(length = 10,name="theater_sn", nullable = false, columnDefinition = "VARCHAR(10) COMMENT '剧院编号'")
	@FieldDesc(comment = "剧院编号")
	private String theaterSn;
	
	@Column(length = 30, nullable = false, columnDefinition = "VARCHAR(30) COMMENT '锁定者标识'")
	@FieldDesc(comment = "锁定者标识")
	private String code;
	
	@Column(name = "play_date", nullable = false, columnDefinition = "DATETIME COMMENT '观看日期'")
	@FieldDesc(comment = "观看日期")
	@JSONField(format = "yyyy-MM-dd")
	private Date playDate;
	
	@Column(name = "lock_time", nullable = false, columnDefinition = "DATETIME COMMENT '锁定时间'")
	@FieldDesc(comment = "锁定时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lockTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSeatId() {
		return seatId;
	}

	public void setSeatId(Integer seatId) {
		this.seatId = seatId;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getTheaterSn() {
		return theaterSn;
	}

	public void setTheaterSn(String theaterSn) {
		this.theaterSn = theaterSn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getPlayDate() {
		return playDate;
	}

	public void setPlayDate(Date playDate) {
		this.playDate = playDate;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
	
	
}
