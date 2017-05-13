package com.tjing.frame.object;

import java.util.List;

public class TreeData {
	private String id;
	private Object pid;
	private String code;
	private String name;
	private Integer type;
	private boolean checked;
	private boolean open;
	private List<TreeData> children;
	public TreeData(){
		super();
	}
	public TreeData(String name){
		this.name = name;
	}
	public TreeData(String id,Object pid,String name){
		this(name);
		this.id = id;
		this.pid = pid;
	}
	public TreeData(String id,Object pid,String name,String code){
		this(id,pid,name);
		this.code = code;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Object getPid() {
		return pid;
	}
	public void setPid(Object pid) {
		this.pid = pid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public List<TreeData> getChildren() {
		return children;
	}
	public void setChildren(List<TreeData> children) {
		this.children = children;
	}
	

}
