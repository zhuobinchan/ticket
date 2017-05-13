package com.tjing.bussiness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="bi_ticket_strategy")
@org.hibernate.annotations.Table(appliesTo="bi_ticket_strategy",comment="门票策略")
public class TicketStrategy {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length=50)
	@FieldDesc(comment="策略名称")
	private String name;
	@Column(length=50,name="cheap_price")
	@FieldDesc(comment="优惠金额")
	private Integer cheapPrice;
	@Column(length=50,name="offset_price")
	@FieldDesc(comment="抵扣金额")
	private Integer offsetPrice;
	@Column(length=50,name="discount")
	@FieldDesc(comment="折扣比例%")
	private Integer discount;
	@Column(length=4,name="price")
	@FieldDesc(comment="指定价格")
	private Integer price;
	@Column(length=9)
	@FieldDesc(dic="0208",comment="计价方式")
	private String valueType;
	@Column(length=9)
	@FieldDesc(dic="0202",comment="记录状态")
	private String status;
	@Column(length=9)
	@FieldDesc(comment="排序")
	private Integer orderno;
	@Column(length=9)
	@FieldDesc(comment="是否必须打印门票",dic="0212")
	private String mustPrint;
	
	@Column(length = 1000, columnDefinition = "VARCHAR(1000) COMMENT '优惠算法'")
	@FieldDesc(comment = "优惠算法")
	private String algorithm;

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
	public Integer getCheapPrice() {
		return cheapPrice;
	}
	public void setCheapPrice(Integer cheapPrice) {
		this.cheapPrice = cheapPrice;
	}
	
	public Integer getOffsetPrice() {
		return offsetPrice;
	}
	public void setOffsetPrice(Integer offsetPrice) {
		this.offsetPrice = offsetPrice;
	}
	
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public String getMustPrint() {
		return mustPrint;
	}
	public void setMustPrint(String mustPrint) {
		this.mustPrint = mustPrint;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	
}
