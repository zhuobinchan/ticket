package com.tjing.frame.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dhtmlx.connector.GridConnector;
import com.dhtmlx.connector.TreeConnector;
import com.tjing.frame.services.DbServices;

@Controller
@RequestMapping(value = "/public/util")
public class UtilController {
	@Autowired
	private DbServices dbServices;

	@Autowired
	private DataSource dataSource;

	/**
	 * 根据HQL返回查询List，返回到js会变成对象数组
	 * 
	 * @param hql
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@RequestMapping(value = "/genList")
	public ResponseEntity<List<Object>> genList(@RequestParam String hql)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		List<Object> list = dbServices.findListByHql(hql, null);
		ResponseEntity<List<Object>> responseEntity = new ResponseEntity<List<Object>>(list, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/parameterGrid")
	public void parameterGrid(HttpServletRequest request, HttpServletResponse response) {
		Connection conn = DataSourceUtils.getConnection(dataSource);

		try {
			GridConnector c = new GridConnector(conn);
			c.servlet(request, response);
			c.render_table("tj_parameter", "id", "type,value,config1,config2,config3");
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	@RequestMapping(value = "/dicGrid")
	public void dicGrid(HttpServletRequest request, HttpServletResponse response) {
		Connection conn = DataSourceUtils.getConnection(dataSource);

		try {
			GridConnector c = new GridConnector(conn);
			c.servlet(request, response);
			c.render_table("tj_dic", "id", "code,text,parent_id,orderno,tier");
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}

	@RequestMapping(value = "/dicTree")
	public void dicTree(HttpServletRequest request, HttpServletResponse response) {
		Connection conn = DataSourceUtils.getConnection(dataSource);

		try {
			TreeConnector c = new TreeConnector(conn);
			c.servlet(request, response);
			c.render_sql("select * from tj_dic where tier < 3", "id", "text", "", "parent_id");
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}
}
