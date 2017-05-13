package com.tjing.group.client.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.tjing.bussiness.support.BusinessException;
import com.tjing.frame.model.Parameter;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.SimpleDao;
import com.tjing.group.client.model.ISendTable;
import com.tjing.group.client.model.TicketSaleSend;

/**
 * 定时器接口类
 * 
 * @author Random
 *
 */
public class InterfaceJob {
	@Autowired
	private DbServices db;

	@Autowired
	private SimpleDao dao;

	@Autowired
	private RestTemplate rt;

	private String b2bConsumeUrl;
	private String serectKey = "9bc8aa75-ea70-40c6-b4d5-d58c31909faf";
	private String machingNo = "TS10001";
	private String appId = "TS10001";

	private String groupUrl;
	private String[] groupTables;

	private Logger log = Logger.getLogger(InterfaceJob.class);

	public void work() {
		try {
			consumeRemote();
		} catch (HibernateException e) {
			log.error("异常：" + e.getMessage(), e);
		}
	}

	/**
	 * 获取集团接口url
	 * 
	 * @return
	 */
	private synchronized String getGroupUrl() throws BusinessException {
		if (groupUrl == null) {
			Parameter para = dao.getEntityByHql(Parameter.class,
					"from Parameter where type = 'groupInterface' and value = 'url' ");
			if (para != null) {
				groupUrl = para.getConfig1();
			}
		}
		if (groupUrl == null) {
			throw new BusinessException("没有配置集团接口url");
		}
		return groupUrl;
	}

	/**
	 * 获取同步到集团接口的数据表
	 * 
	 * @return
	 */
	private synchronized String[] getGroupTables() {
		if (groupTables == null) {
			Parameter para = dao.getEntityByHql(Parameter.class,
					"from Parameter where type = 'groupInterface' and value = 'tables' ");
			if (para != null) {
				if (para.getConfig1() != null) {
					groupTables = para.getConfig1().split(",");
				}
			}
		}
		if (groupTables == null) {
			groupTables = new String[]{};
		}
		return groupTables;
	}

	/**
	 * 获取未发送的销售数据
	 * 
	 * @return
	 */
	private List<?> getUnsendData(Class<?> cls) {
		String name = cls.getName();
		int index = name.lastIndexOf(".");
		name = name.substring(index + 1);
		return db.findListByHql(0, 10,
				"from " + name + " where (status = '0211002' or status = '0227003') and "
						+ "(sendStatus is null or sendStatus <> '0234003') and "
						+ "(sendCount is null or sendCount < 10)",
				new HashMap<Integer, Object>());
	}

	public void consumeRemote() {
		log.info("处理");
		try {
			String[] tables = getGroupTables();
			for (int k = 0; k < tables.length; k++) {
				String table = tables[k];
				Class<?> cls = Class.forName(table);

				List<?> list = getUnsendData(cls);
//				while (!list.isEmpty()) {
				sendData(cls, list.iterator());
//					// 再次获取未发送的数据
//					//list = getUnsendData(cls);
//				}
			}
		} catch (RestClientException e) {
			log.error(e.getMessage(), e);
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void sendData(Class<?> cls, Iterator<?> it) {
		while (it.hasNext()) {
			final MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			Object item = it.next();
			Method[] methods = cls.getMethods();
			Integer id = null;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().indexOf("get") == 0) {
					try {
						Object value = methods[i].invoke(item, new Object[]{});
						params.add(methods[i].getName().substring(3), value);
						if (methods[i].getName().equals("getId")) {
							id = (Integer) value;
						}
					} catch (IllegalAccessException e) {
						log.error(e.getMessage(), e);
					} catch (IllegalArgumentException e) {
						log.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			ISendTable send = dao.getEntityByHql(ISendTable.class,
					"from TicketSaleSend where sourceId = " + id);
			if (send == null) {
				send = new TicketSaleSend();
				send.setSourceId(id);
				send.setSendCount(0);
			}
			send.setSendStatus("0234002");
			send.setSendTime(new Date());
			send.setSendCount(send.getSendCount() == null ? 1 : send.getSendCount() + 1);
			dao.persist(send);
			String result = rt.postForObject(getGroupUrl(), params, String.class);
			log.info("send sale id : " + id);
			if ("0000".equals(result)) {
				send.setSendStatus("0234003");
				dao.persist(send);
			}
		}
	}

	public String getB2bConsumeUrl() {
		return b2bConsumeUrl;
	}

	public void setB2bConsumeUrl(String b2bConsumeUrl) {
		this.b2bConsumeUrl = b2bConsumeUrl;
	}
	public String getSerectKey() {
		return serectKey;
	}
	public void setSerectKey(String serectKey) {
		this.serectKey = serectKey;
	}
	public String getMachingNo() {
		return machingNo;
	}
	public void setMachingNo(String machingNo) {
		this.machingNo = machingNo;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
