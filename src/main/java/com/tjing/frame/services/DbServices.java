package com.tjing.frame.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.directwebremoting.WebContext;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.tjing.bussiness.model.Area;
import com.tjing.bussiness.model.Floor;
import com.tjing.bussiness.model.Seat;
import com.tjing.bussiness.model.TicketSale;
import com.tjing.bussiness.object.AreaSeat;
import com.tjing.frame.model.Dic;
import com.tjing.frame.model.FieldLog;
import com.tjing.frame.model.OperateLog;
import com.tjing.frame.model.User;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.FieldInfo;
import com.tjing.frame.object.TreeData;
import com.tjing.frame.util.BeanAid;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.SimpleDao;

@Service
public class DbServices {
	public static WebContext wctx = null;
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbAid dbAid;
	@Autowired
	private DataPerfect dataPerfect;

	public int deleteByIds(String poName, List<Integer> ids) {
		String hql = "delete from " + poName + " where id in :ids";
		int num = simpleDao.deleteByIds(hql, ids);
		return num;
	}

	public <T> void deleteEntity(T entity) {
		simpleDao.deleteEntity(entity);
	}

	public <T> void flush() {
		simpleDao.flush();
	}

	public void executeBySql(String sql, Map<Integer, Object> paramMap) {
		simpleDao.executeSql(sql, paramMap);
	}

	public void executeByHql(String hql, Map<Integer, Object> paramMap) {
		simpleDao.executeHql(hql, paramMap);
	}

	public String saveOrupdateRecords(String poName, List<JSONObject> data,
			String methodName) {
		User user = CodeHelper.getCurrentUser();
		try {
			ClassInfo classInfo = CodeHelper.getClassInfo(poName);
			Map<String, FieldInfo> fields = classInfo.getFileds();
			Class<?> clazz = CodeHelper.getClass(classInfo);
			Object classObj = null;
			Object id = "";
			FieldInfo idField = fields.get("id");
			for (JSONObject jobj : data) {
				if (!jobj.containsKey("id")
						|| StringUtils.isEmpty(jobj.getString("id"))) {
					classObj = Class.forName(classInfo.getFullName())
							.newInstance();
					Set<String> keyset = jobj.keySet();
					OperateLog ol = addOperateLog(user, classInfo, "添加记录");
					for (String key : keyset) {
						addFieldLogs(jobj, null, ol, key);
						CodeHelper.setMethodTypeValue(classObj, fields, key,
								jobj.getString(key));
					}
					if (idField.getType().equals("String")) {
						id = CodeHelper.uuid();
						MethodUtils.invokeMethod(classObj, "setId", id);
					}
				} else {
					id = classInfo.getIdType().equals("Integer") ? jobj
							.getInteger("id") : jobj.getString("id");
					classObj = simpleDao.getEntity(clazz, id);
					if (classObj == null) {
						classObj = Class.forName(classInfo.getFullName())
								.newInstance();
						Set<String> keyset = jobj.keySet();
						OperateLog ol = addOperateLog(user, classInfo, "添加记录");
						for (String key : keyset) {
							addFieldLogs(jobj, null, ol, key);
							CodeHelper.setMethodTypeValue(classObj, fields,
									key, jobj.getString(key));
						}
					} else {
						Set<String> keyset = jobj.keySet();
						JSONObject oldJobj = JSON.parseObject(JSON
								.toJSONString(classObj));
						OperateLog ol = addOperateLog(user, classInfo, "修改记录");
						for (String key : keyset) {
							addFieldLogs(jobj, oldJobj, ol, key);
							CodeHelper.setMethodTypeValue(classObj, fields,
									key, jobj.getString(key));
						}
					}
				}
				simpleDao.saveOrUpdate(classObj);
			}
			Object newId = MethodUtils.invokeMethod(classObj, "getId");
			return newId.toString();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new NumberFormatException("数字格式不正确");
		}
		return "";
	}

	public void addFieldLogs(JSONObject jobj, JSONObject oldJobj,
			OperateLog ol, String key) {
		FieldLog fl = new FieldLog();
		fl.setId(CodeHelper.uuid());
		fl.setFieldName(key);
		fl.setOid(ol.getId());
		String v = "";
		if (jobj != null) {
			v = jobj.getString(key);
		}
		fl.setNewValue(v);
		if (oldJobj != null)
			fl.setOldValue(oldJobj.getString(key));
		if (StringUtils.isEmpty(fl.getOldValue())
				|| fl.getOldValue().length() <= 200) {
			if (StringUtils.isEmpty(fl.getNewValue())
					|| fl.getNewValue().length() <= 200) {
				persist(fl);
			}
		}
	}

	public void addFieldLogs(JSONObject jobj, JSONObject oldJobj, OperateLog ol) {
		Set<String> ks = null;
		if (jobj != null) {
			ks = jobj.keySet();
		} else {
			ks = oldJobj.keySet();
		}
		for (String key : ks) {
			addFieldLogs(jobj, oldJobj, ol, key);
		}
		;
	}

	public OperateLog addOperateLog(User user, ClassInfo classInfo, String descs) {
		OperateLog ol = new OperateLog();
		ol.setId(CodeHelper.uuid());
		ol.setRecordDate(new Date());
		ol.setTableName(classInfo.getTableName());
		ol.setUserId(user.getId());
		ol.setUserCode(user.getUserCode());
		ol.setUserName(user.getName());
		ol.setDescs(descs);
		persist(ol);
		return ol;
	}

	public void joinTreeString(List<TreeData> list, String poName,
			Integer parentId, String orderbyString, String[] fieldNames)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		String queryString = "{parentId:''}";
		if (parentId != null) {
			queryString = "{parentId:" + parentId + "}";
		}
		List<Object> objs = simpleDao.findList(poName, queryString,
				orderbyString);
		String textName = StringUtils.capitalize(fieldNames[0]);
		for (Object obj : objs) {
			Object id = (Object) MethodUtils.invokeMethod(obj, "getId");
			Object pid = (Object) MethodUtils.invokeMethod(obj, "getParentId");
			String text = (String) MethodUtils.invokeMethod(obj, "get"
					+ textName);
			TreeData treeData = null;
			if (classInfo.getFileds().get("code") == null) {
				treeData = new TreeData(id.toString(), pid, text);
			} else {
				String code = (String) MethodUtils.invokeMethod(obj, "getCode");
				treeData = new TreeData(id.toString(), pid, text, code);
			}
			/*
			 * if(pid==null){ treeData.setOpen(true); }
			 */
			List<TreeData> list2 = new ArrayList<TreeData>();
			treeData.setChildren(list2);
			list.add(treeData);
			joinTreeString(list2, poName, Integer.valueOf(id.toString()),
					orderbyString, fieldNames);
		}
	}

	public JSONObject findEntity(String poName, String id) {
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		Class<?> clazz = CodeHelper.getClass(classInfo);
		Object obj = simpleDao.getEntity(clazz, id);
		JSONObject jobj = JSON.parseObject(JSONObject.toJSONString(obj));
		return jobj;
	}

	public <T> List<T> getListByHqlWithStrParams(int showPageNum, int records,
			String hql, Map<String, Object> paramMap) {
		List<T> list = simpleDao.getListByHqlWithStrParams(showPageNum,
				records, hql, paramMap);
		return list;
	}

	public <T> List<T> getListByHqlWithStrParams(String hql,
			Map<String, Object> paramMap) {
		List<T> list = simpleDao.getListByHqlWithStrParams(hql, paramMap);
		return list;
	}

	public <T> T getEntity(Class<T> clazz, Object id) {
		T obj = simpleDao.getEntity(clazz, id);
		return obj;
	}

	public <T> List<T> findList(String poName, String queryString,
			String orderby) {
		List<T> list = simpleDao.findList(poName, queryString, orderby);
		return list;
	}

	public <T> List<T> findList(int beginNum, int records, String poName,
			String queryString, String orderby) {
		List<T> list = simpleDao.findList(beginNum, records, poName,
				queryString, orderby);
		return list;
	}

	public <T> List<T> findPageList(int showPageNum, int records,
			String poName, String queryString, String orderby) {
		List<T> list = simpleDao.findPageList(showPageNum, records, poName,
				queryString, orderby);
		return list;
	}

	public <T> List<T> findList(int beginNum, int records, Criteria criteria) {
		List<T> list = simpleDao.findList(beginNum, records, criteria);
		return list;
	}

	public String findMaxValue(String poName, String fieldName,
			String queryString) {
		String value = simpleDao.findMaxValue(poName, fieldName, queryString);
		return value;
	}

	public JSONArray queryEntityDicValue(String sql, String id) {
		JSONArray v = simpleDao.getEntityDicValue(sql, id);
		return v;
	}

	public <T> void persist(T obj) {
		simpleDao.persist(obj);
	}

	public <T> void update(T obj) {
		simpleDao.update(obj);
	}

	public <T> void saveOrUpdateEntity(T obj) {
		simpleDao.saveOrUpdate(obj);
	}

	public <T> void saveOrUpdateEntity(T obj, String methodName) {
		ClassInfo classInfo = CodeHelper.getClassInfo(obj.getClass()
				.getSimpleName());
		try {
			JSONObject jobj = JSON.parseObject(JSON.toJSONString(obj));
			MethodUtils.invokeMethod(dataPerfect, methodName, classInfo, jobj);
			// this.saveOrupdateRecords(obj.getClass().getSimpleName(),
			// jobj.toJSONString(), methodName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public <T> List<T> findListByHql(String hql, Map<Integer, Object> paramMap) {
		List<T> list = simpleDao.getListByHql(hql, paramMap);
		return list;
	}

	public <T> T getUniqueByHql(String hql, Map<Integer, Object> paramMap) {
		T objs = simpleDao.getUniqueByHql(hql, paramMap);
		return objs;
	}

	public <T> List<T> findListByHql(int beginNum, int records, String hql,
			Map<Integer, Object> hashMap) {
		List<T> list = simpleDao.getListByHql(beginNum, records, hql, hashMap);
		return list;
	}

	public <T> List<T> findListBySql(String sql, Map<Integer, Object> paramMap) {
		List<T> list = simpleDao.getListBySql(sql, paramMap);
		return list;
	}

	public Object findUniqueBySql(String sql, Map<Integer, Object> paramMap) {
		Object obj = simpleDao.getUniqueBySql(sql, paramMap);
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public <T> List<T> findListBySql(String hql, Map<Integer, Object> paramMap,
			Class clazz) {
		List<T> list = simpleDao.getListBySql(hql, paramMap, clazz);
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List findListBySql(int beginNum, int records, String hql,
			Map<Integer, Object> paramMap, Class clazz) {
		List list = simpleDao.getListBySql(beginNum, records, hql, paramMap,
				clazz);
		return list;
	}

	public Long countRecords(Class<?> clazz, String queryString) {
		return simpleDao.countRecords(clazz, queryString);
	}

	public Long countRecords(String hql, Map<Integer, Object> paramMap) {

		return simpleDao.countRecords(hql, paramMap);
	}

	public Long countRecordsBySql(String sql, Map<Integer, Object> paramMap) {
		return simpleDao.countRecordsBySql(sql, paramMap);
	}

	public String getDicCodeByValue(String prefix, String value) {
		List<Dic> list = simpleDao.findList("Dic", "[{name:'code',value:'"
				+ prefix + "%',r:'like'},{text:'" + value + "'}]", "");
		if (list.size() == 0) {
			return "";
		}
		Dic dic = list.get(0);
		return dic.getCode();
	}

	public String getDicTextByCode(String code) {
		List<Dic> list = simpleDao.findList("Dic", "{code:'" + code + "'}", "");
		if (list.size() == 0) {
			return "";
		}
		Dic dic = list.get(0);
		return dic.getText();
	}

	public <T> Criteria createCriteria(Class<T> clazz) {
		Criteria criteria = simpleDao.createCriteria(clazz);
		return criteria;
	}

	public User getUserById(String id) {
		User user = simpleDao.getEntity(User.class, id);
		return user;
	}

	public String getUserNameById(String id) {
		User user = getUserById(id);
		if (user == null) {
			return "";
		}
		return user.getName();
	}

	public Integer getSectionIdByUserId(String id) {
		User user = simpleDao.getEntity(User.class, id);
		return user.getOrgId();
	}

	public String modPassword(String userId, String pwd, String pwd2) {
		User user = simpleDao.getEntity(User.class, userId);
		if (!user.getPassword().equals(pwd)) {
			return "当前密码不正确，请仔细检查";
		}
		user.setPassword(pwd2);
		return "0";
	}

	@SuppressWarnings({ "unchecked" })
	public List<TicketSale> getListByQueryString(String queryString,
			String orderbyString) {
		Criteria criteria = simpleDao.createCriteria(TicketSale.class);
		ClassInfo classInfo = CodeHelper.getClassInfo("TicketSale");
		JSONArray arr = JSON.parseArray(queryString);
		dbAid.setParams(criteria, arr, classInfo);
		dbAid.order(criteria, orderbyString);
		List<TicketSale> list = criteria.list();
		return list;
	}

	public void fetchSeatList(ModelAndView mav, Integer theaterId)
			throws ExecutionException {
		mav.addObject("areaMap",
				JSON.toJSONString(tableHtmlCache.get(theaterId + "")));
	}

	/*
	 * public void fetchSeatList2(ModelAndView mav) throws ExecutionException {
	 * mav.addObject("areaMap" ,JSON.toJSONString(tableHtmlCache.get("2"))); }
	 */
	public static LoadingCache<String, Map<String, List<Seat>>> tableHtmlCache = CacheBuilder
			.newBuilder().maximumSize(1000) // 最多可以缓存1000个key
			.build(new CacheLoader<String, Map<String, List<Seat>>>() {
				public Map<String, List<Seat>> load(String key)
						throws ExecutionException {
					SimpleDao simpleDao = BeanAid.getBean(SimpleDao.class);
					List<Area> list = simpleDao.findList("Area", "{belongto:"
							+ key + "}", "");
					HashMap<String, List<Seat>> map = Maps.newHashMap();
					AreaSeat as = null;
					for (Area area : list) {
						as = cache.get(area.getId());
						if (as != null) {
							map.put(area.getCode(), as.getSeats());
						}
						;

					}
					return map;
				}
			});
	public static LoadingCache<Integer, AreaSeat> cache = CacheBuilder
			.newBuilder().maximumSize(500)
			.build(new CacheLoader<Integer, AreaSeat>() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public AreaSeat load(Integer areaId) {
					SimpleDao simpleDao = BeanAid.getBean(SimpleDao.class);
					Map paramMap = new HashMap<Integer, Object>();
					paramMap.put(0, areaId);
					List<Area> areas = simpleDao.getListByHql(
							"from Area where id=?", paramMap);
					Area area = areas.get(0);
					List<Seat> list = simpleDao.findList("Seat", "{areaId:"
							+ area.getId() + "}", "rowNum,columnNum");
					AreaSeat as = new AreaSeat(area, list);
					return as;
				}
			});
	public static LoadingCache<Integer, Seat> seatCache = CacheBuilder
			.newBuilder().maximumSize(1)
			.build(new CacheLoader<Integer, Seat>() {
				public Seat load(Integer seatId) {
					SimpleDao simpleDao = BeanAid.getBean(SimpleDao.class);
					Seat seat = simpleDao.getEntity(Seat.class, seatId);
					return seat;
				}
			});
	public static LoadingCache<Integer, List<Floor>> floorCache = CacheBuilder
			.newBuilder().maximumSize(10)
			.build(new CacheLoader<Integer, List<Floor>>() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public List<Floor> load(Integer theaterId) {
					SimpleDao simpleDao = BeanAid.getBean(SimpleDao.class);
					Map paramMap = new HashMap<Integer, Object>();
					paramMap.put(0, theaterId);
					List<Floor> floors = simpleDao.getListByHql(
							"from Floor where theaterId=?", paramMap);
					return floors;
				}
			});

	public Seat getSeat(Integer id) {
		Seat seat = null;
		try {
			seat = seatCache.get(id);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return seat;
	}

	public List<Floor> getFloors(Integer theaterId) {
		List<Floor> floors = null;
		try {
			floors = floorCache.get(theaterId);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return floors;
	}

}
