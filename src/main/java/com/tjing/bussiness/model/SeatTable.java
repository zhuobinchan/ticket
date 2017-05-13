package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.tjing.frame.annotation.FieldDesc;

/*
 * 暂时作废
 */
public class SeatTable {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name="area_id",length=20)
	@FieldDesc(comment="区域名称")
	private Integer areaId;
	@Lob
	@Column(name="table_html")
	@FieldDesc(comment="区域名称")
	private String tableHtml;
	@Column(name="belongto",length=1)
	@FieldDesc(comment="属于")
	private Integer belongto;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	
	public String getTableHtml() {
		return tableHtml;
	}
	public void setTableHtml(String tableHtml) {
		this.tableHtml = tableHtml;
	}
	public Integer getBelongto() {
		return belongto;
	}
	public void setBelongto(Integer belongto) {
		this.belongto = belongto;
	}
	

}
