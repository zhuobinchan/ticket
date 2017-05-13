package com.tjing.frame.util;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.object.PageResultBean;
import com.tjing.frame.services.DataCache;

@Repository
public class SimpleDao {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DbAid dbAid;
	@Autowired
	private DataCache dataCache;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public <T> void update(T obj) {
		getSession().update(obj);
	}

	// 更新后使对象处于游离状态
	public <T> void updatev(T obj) {
		Session ss = getSession();
		ss.update(obj);
		ss.flush();
		evict(obj);
	}

	public <T> void merge(T obj) {
		getSession().merge(obj);
	}

	public <T> void saveOrUpdate(T obj) {
		getSession().saveOrUpdate(obj);
	}

	public <T> void persist(T obj) {
		getSession().persist(obj);
	}

	public int deleteByHql(String hql, Integer[] values) {
		Query query = getSession().createQuery(hql);
		query.setParameterList("ids", values);
		int num = query.executeUpdate();
		return num;
	}

	public int deleteByIds(String hql, List<Integer> values) {
		Query query = getSession().createQuery(hql);
		query.setParameterList("ids", values);
		int num = query.executeUpdate();
		return num;
	}

	public int executeHql(String hql, Map<Integer, Object> paramMap) {
		Query query = getSession().createQuery(hql);
		dbAid.addParams(query, paramMap);
		int num = query.executeUpdate();
		return num;
	}

	public int executeSql(String sql, Map<Integer, Object> paramMap) {
		Query query = getSession().createSQLQuery(sql);
		dbAid.addParams(query, paramMap);
		int num = query.executeUpdate();
		return num;
	}

	@SuppressWarnings("rawtypes")
	public List executeSqlQuery(String sql, Map<Integer, Object> paramMap) {
		Query query = getSession().createSQLQuery(sql);
		dbAid.addParams(query, paramMap);
		List list = query.list();
		return list;
	}

	// 通过id
	@SuppressWarnings("unchecked")
	public <T> T getEntityByHql(Class<T> clazz, String hql) {
		T obj = ((T) getSession().createQuery(hql).uniqueResult());
		return obj;
	}

	public Integer getIntNumByHql(String hql) {
		Query query = getSession().createQuery(hql);
		List<?> list = query.list();
		if (list.size() > 0) {
			if (list.get(0) != null) {
				return Integer.parseInt(list.get(0).toString());
			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public <T> T getUniqueByHql(String hql, Map<Integer, Object> paramMap) {
		Session hs = getSession();
		Query query = hs.createQuery(hql);
		if (paramMap != null)
			dbAid.addParams(query, paramMap);
		T objs = (T) query.uniqueResult();
		return objs;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getListByHql(String hql, Map<Integer, Object> paramMap) {
		Session hs = getSession();
		Query query = hs.createQuery(hql);
		if (paramMap != null)
			dbAid.addParams(query, paramMap);
		List<T> list = query.list();
		return list;
	}

	// 查询后使结果集游离
	@SuppressWarnings("unchecked")
	public <T> List<T> getListByHqlv(String hql, Map<Integer, Object> paramMap) {
		Session hs = getSession();
		Query query = hs.createQuery(hql);
		dbAid.addParams(query, paramMap);
		List<T> list = query.list();
		hs.clear();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getListByHql(int showPageNum, int records, String hql,
			Map<Integer, Object> paramMap) {
		Query query = getSession().createQuery(hql);
		dbAid.addParams(query, paramMap);
		query.setFirstResult(showPageNum);
		query.setMaxResults(records);
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getListByHqlWithStrParams(int showPageNum, int records,
			String hql, Map<String, Object> paramMap) {
		Query query = getSession().createQuery(hql);
		dbAid.addNamedParams(query, paramMap);
		query.setFirstResult(showPageNum);
		query.setMaxResults(records);
		List<T> list = query.list();
		return list;
	}

	public <T> List<T> getListByHqlWithStrParams(String hql,
			Map<String, Object> paramMap) {
		Query query = getSession().createQuery(hql);
		dbAid.addNamedParams(query, paramMap);
		@SuppressWarnings("unchecked")
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> getListBySql(int showPageNum, int records, String sql,
			Map<Integer, Object> paramMap, Class clazz) {
		Query query = getSession().createSQLQuery(sql).addEntity(clazz);
		dbAid.addParams(query, paramMap);
		query.setFirstResult(showPageNum);
		query.setMaxResults(records);
		List<T> list = query.list();
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> getListBySql(String sql, Map<Integer, Object> paramMap,
			Class clazz) {
		Query query = getSession().createSQLQuery(sql).addEntity(clazz);
		dbAid.addParams(query, paramMap);
		List<T> list = query.list();
		return list;
	}

	public <T> List<T> getListBySql(String sql, Map<Integer, Object> paramMap) {
		Query query = getSession().createSQLQuery(sql);
		dbAid.addParams(query, paramMap);
		@SuppressWarnings("unchecked")
		List<T> list = query.list();
		return list;
	}

	public Object getUniqueBySql(String sql, Map<Integer, Object> paramMap) {
		Query query = getSession().createSQLQuery(sql);
		dbAid.addParams(query, paramMap);
		Object obj = query.uniqueResult();
		return obj;
	}

	/**
	 * TODO 通过ID查询其他需要的字段信息，比如通过部门ID查询部门名称
	 * 
	 * @param sql
	 * @param id
	 * @return
	 */
	public JSONArray getEntityDicValue(String sql, String id) {
		Query query = getSession().createSQLQuery(sql).setParameter(0, id);
		Object obj = query.uniqueResult();
		JSONArray arr = (JSONArray) JSONArray.toJSON(obj);
		return arr;
	}

	/**
	 * @MethodName countRecordNum
	 * @Description 根据hql语句查询记录数
	 * @author 曾平
	 * @param hql
	 * @return Long
	 */
	public Long countRecords(String hql, Map<Integer, Object> paramMap) {
		Query query = this.getSession().createQuery(hql);
		dbAid.addParams(query, paramMap);
		Long num = (Long) query.iterate().next();
		return num;
	}

	public Long countRecords(Criteria criteria) {
		criteria.setProjection(Projections.rowCount());
		Long num = Long.parseLong(criteria.uniqueResult().toString());
		return num;
	}

	public Long countRecords(Class<?> clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setProjection(Projections.rowCount());
		Long num = Long.parseLong(criteria.uniqueResult().toString());
		return num;
	}

	public Long countRecords(Class<?> clazz, String queryString) {
		Criteria criteria = getSession().createCriteria(clazz);
		dbAid.handleCriteria(criteria, queryString, clazz.getSimpleName());
		criteria.setProjection(Projections.rowCount());
		Long num = Long.parseLong(criteria.uniqueResult().toString());
		return num;
	}

	public Long countRecords(Class<?> clazz, JSONObject json) {
		Criteria criteria = getSession().createCriteria(clazz);
		dbAid.handleCriteria(criteria, json, clazz.getSimpleName());
		criteria.setProjection(Projections.rowCount());
		Long num = Long.parseLong(criteria.uniqueResult().toString());
		return num;
	}

	public Long countRecordsWithStrParams(String hql,
			Map<String, Object> paramMap) {
		Query query = this.getSession().createQuery(hql);
		dbAid.addNamedParams(query, paramMap);
		Long num = (Long) query.iterate().next();
		return num;
	}

	@SuppressWarnings("rawtypes")
	public Long countRecordsWithStrParams(String hql,
			Map<String, Object> paramMap, Class clazz) {
		Query query = this.getSession().createQuery(hql);
		dbAid.addNamedParams(query, paramMap, clazz);
		Long num = (Long) query.iterate().next();
		return num;
	}

	/**
	 * @MethodName countRecordNumBySql
	 * @Description 根据sql语句查询记录数
	 * @author 曾平
	 * @param sql
	 * @return Long
	 */
	public Long countRecordsBySql(String sql, Map<Integer, Object> paramMap) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		dbAid.addParams(query, paramMap);
		Long num = ((BigInteger) query.list().get(0)).longValue();
		return num;
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> clazz, Object id) {
		T entity = null;
		try {
			entity = (T) getSession().get(clazz, (Serializable) id);
		} catch (IllegalArgumentException e) {
			return null;
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntityv(Class<T> clazz, String id) {
		T entity = null;
		try {
			entity = (T) getSession().get(clazz, id);
		} catch (IllegalArgumentException e) {
			return null;
		}
		getSession().evict(entity);
		return entity;
	}

	/**
	 * TODO 查询分页结果
	 * 
	 * @param gridSetting
	 * @return
	 * @throws ClassNotFoundException
	 */
	public PageResultBean findPageResult(GridSetting gridSetting)
			throws ClassNotFoundException {
		Criteria criteria = createCriteria(gridSetting);
		dbAid.handleCriteria(criteria, gridSetting);
		Long num = dbAid.countRecords(criteria);
		criteria.setProjection(null);
		dbAid.order(criteria, gridSetting);
		List<?> list = dbAid.cutList(criteria, gridSetting);
		PageResultBean prb = new PageResultBean();
		prb.setTotalRecordNum(num);
		prb.setList(list);
		return prb;
	}

	public List<?> findList(Criteria criteria, String orderbyString)
			throws ClassNotFoundException {
		dbAid.order(criteria, orderbyString);
		List<?> list = criteria.list();
		return list;
	}

	public List<?> findList(Criteria criteria, String queryString,
			String orderbyString, String poName) throws ClassNotFoundException {
		dbAid.handleCriteria(criteria, queryString, poName);
		dbAid.order(criteria, orderbyString);
		List<?> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findList(String poName, String queryString,
			String orderbyString) {
		Criteria criteria = createCriteria(poName);
		dbAid.handleCriteria(criteria, queryString, poName);
		dbAid.order(criteria, orderbyString);
		List<T> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findList(int beginNum, int records, String poName,
			String queryString, String orderbyString) {
		Criteria criteria = createCriteria(poName);
		dbAid.handleCriteria(beginNum, records, criteria, queryString, poName);
		dbAid.order(criteria, orderbyString);
		List<T> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findPageList(int showPageNum, int records,
			String poName, String queryString, String orderbyString) {
		Criteria criteria = createCriteria(poName);
		int beginNum = dbAid.calcuBeginRecordNum(showPageNum, records);
		dbAid.handleCriteria(beginNum, records, criteria, queryString, poName);
		dbAid.order(criteria, orderbyString);
		List<T> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findList(int beginNum, int records, Criteria criteria) {
		List<T> list = criteria.list();
		return list;
	}

	public String findMaxValue(String poName, String fieldName,
			String queryString) {
		Criteria criteria = createCriteria(poName);
		dbAid.handleCriteria(criteria, queryString, poName);
		criteria.setProjection(Projections.projectionList().add(
				Projections.max(fieldName)));
		@SuppressWarnings("rawtypes")
		List list = criteria.list();
		if (list.get(0) == null) {
			return "";
		}
		return list.get(0).toString();
	}

	public Criteria createCriteria(String poName) {
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		Criteria criteria = getSession().createCriteria(
				CodeHelper.getClass(classInfo));
		return criteria;
	}

	public Criteria createCriteria(Class<?> clazz) {
		getSession().clear();
		Criteria criteria = getSession().createCriteria(clazz);
		return criteria;
	}

	public Criteria createCriteria(GridSetting gridSetting) {
		ClassInfo classInfo = CodeHelper.getClassInfo(gridSetting);
		Criteria criteria = createCriteria(CodeHelper.getClass(classInfo));
		return criteria;
	}

	public <T> void deleteEntity(T obj) {
		getSession().delete(obj);
	}

	public <T> void evict(T obj) {
		getSession().evict(obj);
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}
}
