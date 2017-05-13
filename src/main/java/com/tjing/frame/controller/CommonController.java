package com.tjing.frame.controller;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.tjing.frame.model.Navi;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;


@Controller
@RequestMapping(value="/public/common")
public class CommonController {
	@Autowired
	private DbServices dbservices;
	@RequestMapping("/r")
	public ModelAndView r(@RequestParam String page,@RequestParam String naviId,HttpServletRequest request){
		List<Navi> allButtons = dbservices.findList("Navi", "{parentId:"+naviId+"}", "");
		String sql = "select n.* from ((tj_navi n join tj_role_navi rn on n.id=rn.navi_id) join tj_role r on r.id=rn.role_id) join tj_user_role ur on ur.role_id=r.id where n.parent_id=? and ur.user_id=? group by n.id order by n.orderno ,n.id";
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, naviId);
		paramMap.put(1, CodeHelper.getCurrentUser().getId());
		List<Navi> buttons = dbservices.findListBySql(sql, paramMap,Navi.class);
		//按钮权限规则：如果在菜单下添加了按钮记录，但是没有出现在权限表中，则隐藏
		JSONArray forbitBtns = new JSONArray();
		for(Navi navi : allButtons){
			if(!buttons.contains(navi)){
				forbitBtns.add(navi.getPageUrl());
			}
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("forbitBtns",forbitBtns);
		mav.setViewName(page);
		return mav;
	}
	@RequestMapping("/r2")
	public ModelAndView r2(@RequestParam String page,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName(page);
		return mav;
	}
	@RequestMapping("/readySalePrintCommon")
	public ModelAndView readySalePrintCommon(HttpServletRequest request) throws ExecutionException{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sell/job/theatre_print");
		return mav;
	}
	@RequestMapping("/readySalePrintCommon2")
	public ModelAndView readySalePrintCommon2(HttpServletRequest request) throws ExecutionException{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sell/job/theatre_print2");
		return mav;
	}

	@RequestMapping("/readySalePrintCommon4CZ")
	public ModelAndView readySalePrintCommon4CZ(HttpServletRequest request) throws ExecutionException{
		String id = request.getParameter("id");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("sell/job/theatre_print_cz");
		mav.addObject("theaterId", id);
		return mav;
	}

}
