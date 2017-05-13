package com.tjing.frame.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tjing.frame.model.Organization;
import com.tjing.frame.object.TreeData;
import com.tjing.frame.services.DbServices;

@Controller
@RequestMapping(value="/public/org")
public class OrganizationController {
	@Autowired
	private DbServices dbServices;
	@ResponseBody 
	@RequestMapping(value="/asTree")   
	public List<TreeData> asTree(@RequestParam(value="pid",required=false) Integer pid) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		List<TreeData> list = new ArrayList<TreeData>();
		dbServices.joinTreeString(list,"Organization",pid," tier,orderno,id",new String[]{"name"});
	    return list; 
	}
	@ResponseBody 
	@RequestMapping(value="/moveNode")   
	public Integer moveNode(Integer sourceId,Integer destId){
		Organization org = dbServices.getEntity(Organization.class, sourceId);
		org.setParentId(destId);
		dbServices.update(org);
		return 1;
	}
}
