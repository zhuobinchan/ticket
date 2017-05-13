package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name = "bi_venue_commision")
@org.hibernate.annotations.Table(appliesTo = "bi_venue_commision", comment = "场内消费提成比例表")
public class VenueCommision {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "INT COMMENT '记录ID'")
	private Integer id;

	@Column(nullable = false, length = 8, columnDefinition = "VARCHAR(8) COMMENT '员工类别'")
	@FieldDesc(comment = "员工类别", dic = "0228")
	private String type;

	@Column(name = "goods_commision", nullable = false, length = 8, columnDefinition = "VARCHAR(8) COMMENT '商品提成比例'")
	@FieldDesc(comment = "商品提成比例", dic = "0229")
	private String goodsCommision;

	@Column(nullable = false, columnDefinition = "DECIMAL(13,2) DEFAULT 0 COMMENT '提成比例'")
	@FieldDesc(comment = "提成比例")
	private Float commision;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGoodsCommision() {
		return goodsCommision;
	}

	public void setGoodsCommision(String goodsCommision) {
		this.goodsCommision = goodsCommision;
	}

	public Float getCommision() {
		return commision;
	}

	public void setCommision(Float commision) {
		this.commision = commision;
	}

}
