package com.tjing.group.server.services;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjing.frame.util.SimpleDao;
import com.tjing.group.server.model.GroupTicketSale;

@Service
public class GroupService {
	private Logger log = Logger.getLogger(GroupService.class);

	@Autowired
	private SimpleDao dao;

	public String handleText(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("Id");
		String theaterSn = request.getParameter("TheaterSn");
		GroupTicketSale ts = dao.getEntityByHql(GroupTicketSale.class,
				"from GroupTicketSale where sourceId = " + id + " and theaterSn = '" + theaterSn + "'");
		if (ts == null) {
			ts = new GroupTicketSale();
		}

		Method[] methods = GroupTicketSale.class.getMethods();

		@SuppressWarnings("unchecked")
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			try {
				String key = e.nextElement();
				String pv = request.getParameter(key);
				Object value = null;
				if ("Id".equals(key)) {
					key = "SourceId";
				}
				for (int i = 0; i < methods.length; i++) {
					Method method = methods[i];
					if (method.getName().equals("set" + key)) {
						Class<?>[] classes = method.getParameterTypes();
						if (classes.length == 1) {
							Class<?> c = classes[0];
							if (pv != null) {
								switch (c.getName()) {
									case "java.lang.Integer" :
										value = new Integer(pv);
										break;

									case "java.lang.Float" :
										value = new Float(pv);
										break;

									case "java.util.Date" :
										value = new Date(new Long(pv));
										break;
									default :
										value = pv;
										break;
								}
							}
							method.invoke(ts, value);
							log.info(key + ": " + pv);
							break;
						}
					}
				}
			} catch (Exception e1) {
				log.error(e1.getMessage(), e1);
				return "1001";
			}
		}
		dao.persist(ts);
		return "0000";
	}
}
