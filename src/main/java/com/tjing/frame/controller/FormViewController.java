package com.tjing.frame.controller;

import java.lang.reflect.InvocationTargetException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tjing.frame.object.FormSetting;


@Controller
@RequestMapping(value="/public/frame")
public class FormViewController {
	@ResponseBody 
	@RequestMapping(value="/formview")   
	public String formview(FormSetting formSetting) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
	    return "112"; 
	}
}
