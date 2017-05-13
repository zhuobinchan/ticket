package com.tjing.frame.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;

@Entity
@Table(name="TJ_NAVI")
@org.hibernate.annotations.Table(appliesTo="TJ_NAVI",comment="系统菜单表")
public class Navi implements Serializable{
	private static final long serialVersionUID = -5924696043849534600L;
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length=40)
	private Integer id;
	@Column(length=20)
	@FieldDesc(comment="菜单名称")
	private String name;
	@Column(name="page_url",length=200)
	@FieldDesc(comment="后台资源地址")//后台管理
	private String pageUrl;
	@Column(name="ui_url",length=200)
	@FieldDesc(comment="前台资源地址")//后台管理
	private String uiUrl;
	@FieldDesc(comment="排序")
	private Integer orderno;
	@FieldDesc(comment="层级")
	private Integer tier;
	@Column(length=8,name="obj_type")
	@FieldDesc(comment="资源类型",dic="0101")
	private String objType; 
	@Column(name="img_path",length=100)
	@FieldDesc(comment="图片地址")
	private String imgPath;
	@Column(name="descs",length=100)
	@FieldDesc(comment="备注")
	private String descs;
	@Column(name="parent_id",length=40)
	@FieldDesc(comment="父节点ID")
	private Integer parentId;
	@JSONField(serialize=false)
	@Transient
	private String naviPath;//当前位置
	@Transient
	private List<Navi> children;
	public Navi() {
		super();
	}
	
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
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	
	public String getUiUrl() {
		return uiUrl;
	}
	public void setUiUrl(String uiUrl) {
		this.uiUrl = uiUrl;
	}
	public Integer getOrderno() {
		return orderno;
	}
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	public Integer getTier() {
		return tier;
	}
	public void setTier(Integer tier) {
		this.tier = tier;
	}
	
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNaviPath() {
		return naviPath;
	}
	public void setNaviPath(String naviPath) {
		this.naviPath = naviPath;
	}
	
	public List<Navi> getChildren() {
		return children;
	}
	public void setChildren(List<Navi> children) {
		this.children = children;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Navi other = (Navi) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
