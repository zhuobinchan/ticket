package com.tjing.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.tjing.frame.object.Constant;
import com.tjing.frame.services.DataCache;
import com.tjing.frame.util.BeanAid;
public class AppServletContextListener implements ServletContextListener {
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		Constant.FULLPATH = sc.getRealPath("");
		DataCache dataCache = BeanAid.getBean("dataCache");
	//	dataCache.getFloorNumFromCache();
		dataCache.getUserTopOrgIdFromCache();
		System.out.println("context初始化");
	}
}
