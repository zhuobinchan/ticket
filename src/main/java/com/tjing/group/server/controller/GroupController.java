package com.tjing.group.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjing.group.server.services.GroupService;

@RestController
@RequestMapping("/outside/group")
public class GroupController {
	private Logger log = Logger.getLogger(GroupController.class);

	@Autowired
	private GroupService service;

	@RequestMapping("/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		return service.handleText(request, response);
	}
}
