package com.tjing.frame.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tjing.frame.object.TreeData;
import com.tjing.frame.services.DbServices;

@Controller
@RequestMapping(value="/public/navi")
public class NaviController {
	@Autowired
	private DbServices dbServices;
	@ResponseBody 
	@RequestMapping(value="/asTree")   
	public List<TreeData> asTree(@RequestParam(value="pid",required=false) Integer pid) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		List<TreeData> list = new ArrayList<TreeData>();
		dbServices.joinTreeString(list,"Navi",pid," tier,orderno,id",new String[]{"name"});
	    return list; 
	}
}
