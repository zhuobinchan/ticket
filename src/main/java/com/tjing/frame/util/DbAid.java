package com.tjing.frame.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.FieldInfo;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.object.PageInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@Component
public class DbAid {
	@Autowired
	private SimpleDao simpleDao;
	public void closeStatement(Statement stmt){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void closeResultSet(ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @to do 设置参数 
	 * @param query
	 * @param paramMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addParams(Query query ,Map<Integer,Object> paramMap){
		if(paramMap!=null){
			Iterator it = paramMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer,Object> entry = (Entry<Integer,Object>) it.next();
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addNamedParams(Query query ,Map<String,Object> paramMap){
		if(paramMap!=null){
			Iterator it = paramMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String,Object> entry = (Entry<String,Object>) it.next();
				if(entry.getKey().endsWith("s")){
					query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
				}else if(entry.getKey().endsWith("List")){
					query.setParameterList(entry.getKey(), (Collection) entry.getValue());
				}else{
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addNamedParams(Query query ,Map<String,Object> paramMap,Class clazz){
		if(paramMap!=null){
			Iterator it = paramMap.entrySet().iterator();
			Map<String, FieldInfo> fieldMap = CodeHelper.getFieldInfoMap(clazz.getSimpleName());
			
			while(it.hasNext()){
				Entry<String,Object> entry = (Entry<String,Object>) it.next();
				FieldInfo fieldInfo = fieldMap.get(entry.getKey());
				if(fieldInfo.getType().equals("Integer")){
					query.setInteger(entry.getKey(),Integer.parseInt(entry.getValue().toString()));
				}else{
					query.setParameter(entry.getKey(), entry.getValue());
				}
				
				
			}
		}
	}
	/**
	 * @to do 创建一个空的map
	 * @return
	 */
	public Map<Integer,Object> newParamMap(){
		Map<Integer,Object> paramMap = new HashMap<Integer,Object>();
		return paramMap;
	}
	public Map<String,Object> newStringParamMap(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		return paramMap;
	}
	public String setOrderby(String hql,String orderby){
		if(StringUtils.isNotEmpty(orderby)){
			hql += " order by " + orderby; 
		}
		return hql;
	}
	public int calcuBeginRecordNum(int showPageNum ,int records){
		int beginRecordNum = (showPageNum-1)*records;//第一条记录
		return beginRecordNum;
	}
	public void handleCriteria(Criteria criteria,String queryString,String poName){//处理条件
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		if(StringUtils.isNotBlank(queryString)){
			queryString = queryString.trim();
			if(queryString.startsWith("{")){
				JSONObject jsonObject = JSON.parseObject(queryString);
				setParams(criteria, jsonObject, classInfo);
			}else if(!StringUtils.isEmpty(queryString)){
				JSONArray arr = JSON.parseArray(queryString);
				setParams(criteria, arr, classInfo);
			}
		}
	}
	public void handleCriteria(int beginNum,int records,Criteria criteria,String queryString,String poName){
		handleCriteria(criteria,queryString,poName);
		criteria.setFirstResult(beginNum);
		criteria.setMaxResults(records);
	}
	public void handleCriteria(Criteria criteria,JSONObject jsonObject,String poName){
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		if(jsonObject!=null){
			setParams(criteria, jsonObject, classInfo);
		}
	}
	
	public void or(Criteria criteria,JSONObject jobj,ClassInfo classInfo){
		JSONArray zArr = jobj.getJSONArray("or");
		List<Criterion> rl = new ArrayList<Criterion>();
		for(int j=0;j<zArr.size();j++){
			JSONObject zJ = zArr.getJSONObject(j);
			addParam(rl, zJ, classInfo);
		}
		criteria.add(Restrictions.or(rl.get(0), rl.get(1)));
	}
	public void between(Criteria criteria,JSONObject jobj,ClassInfo classInfo){
		String key = jobj.getString("name");
		JSONArray valueArr = jobj.getJSONArray("value");
		Object lo = null,hi = null;
		String value1 = valueArr.getString(0);
		String value2 = valueArr.getString(1);
		// between条件可能是date类型，也可能是int类型
		if (value1.length() == 10) {
			lo = new DateTime(value1).toDate();
		} else if (value1.length() > 0) {
			lo = new Integer(value1);
		}
		if(value2.length()==10){
			hi = new DateTime(value2).plusDays(1).minusSeconds(1).toDate();
		} else if (value2.length() > 0) {
			hi = new Integer(value2);
		}
		criteria.add(Restrictions.between(key, lo, hi));
	}
	public void setParams(Criteria criteria,JSONArray arr,ClassInfo classInfo){
		for(int i=0;i<arr.size();i++){
			JSONObject jobj = arr.getJSONObject(i);
			if(jobj.containsKey("or")){
				or(criteria, jobj,classInfo);
			}else if(jobj.containsValue("between")){
				between(criteria, jobj,classInfo);
			}else{
				addParam(criteria, jobj, classInfo);
			}
		}
	}
	public void addParam(Criteria criteria ,JSONObject zJ,ClassInfo classInfo){
		String fieldType = "";
		if(zJ.containsKey("r")){//如果包含关系符，表示为json字符串{r:"like",value:2}
			String key = zJ.getString("name");
			fieldType = classInfo.getFileds().get(key).getType();
			addParam(criteria, fieldType,zJ);
		}else{
			Set<String> keys = zJ.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String r = "=";
				String key = it.next();
				FieldInfo fieldInfo = classInfo.getFileds().get(key);
				JSONObject model = new JSONObject();
				if(fieldInfo==null){
					if(key.endsWith("!=")){
						model.put("value", zJ.getString(key));
						key = StringUtils.substringBefore(key, "!=");
						r = "!=";
					}else if(key.endsWith("!!in")){
						model.put("value", zJ.getString(key));
						key = StringUtils.substringBefore(key, "!!in");
						r = "in";
					}else if(key.endsWith("!in")){
						model.put("value", zJ.getString(key));
						key = StringUtils.substringBefore(key, "!in");
						r = "not in";
					}else if(key.endsWith(">=")){
						model.put("value", zJ.getString(key));
						key = StringUtils.substringBefore(key, ">=");
						r = ">=";
					}else if(key.endsWith("<=")){
						model.put("value", zJ.getString(key));
						key = StringUtils.substringBefore(key, "<=");
						r = "<=";
					}
				}else{
					model.put("value", zJ.getString(key));
				}
				fieldInfo = classInfo.getFileds().get(key);
				fieldType = fieldInfo.getType();
				model.put("name", key);
				model.put("r", r);
				addParam(criteria, fieldType,model);
			}
		}
	}
	//分别设置or的各个条件
	public void addParam(List<Criterion> sep ,JSONObject zJ,ClassInfo classInfo){
		String fieldType;
		if(zJ.containsKey("r")){//如果包含关系符，表示为json字符串{r:"like",value:2}
			String key = zJ.getString("name");
			fieldType = classInfo.getFileds().get(key).getType();
			addParam(sep, fieldType, zJ);
		}else{
			Set<String> keys = zJ.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String key = it.next();
				if(key.endsWith("!!in")){
					DetachedCriteria sub = addSubWhere(zJ.getString(key));
					key = StringUtils.substringBefore(key, "!!in");
					sep.add(Property.forName(key).in(sub));
				}else{
					fieldType = classInfo.getFileds().get(key).getType();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("name", key);
					jsonObject.put("value", zJ.getString(key));
					jsonObject.put("r", "=");
					addParam(sep, fieldType, jsonObject);
				}
				
			}
		}
	}
	public void setParams(Criteria criteria,JSONObject jsonObject,ClassInfo classInfo){
		//
		if(jsonObject.containsKey("or")){
			or(criteria, jsonObject,classInfo);
		}else{
			addParam(criteria, jsonObject, classInfo);
		}
	}
	public void handleCriteria(Criteria criteria,GridSetting gridSetting){
		String queryString = gridSetting.getQueryParams();
		handleCriteria(criteria, queryString,gridSetting.getModelName());
	}
	public void handleAndOrderCriteria(Criteria criteria,GridSetting gridSetting){
		handleCriteria(criteria, gridSetting);
		order(criteria, gridSetting);
	}
	@SuppressWarnings("unchecked")
	public void addParam(Criteria criteria,String fieldType,JSONObject jsonObject){
		String value = jsonObject.getString("value");
		String key = jsonObject.getString("name");
		String r = jsonObject.getString("r");
		if(StringUtils.isEmpty(value)){
			if(r.equals("!=")){
				criteria.add(Restrictions.isNotNull(key));
			}else{
				if(fieldType.equals("Integer")){
					criteria.add(Restrictions.isNull(key));
				}else
					criteria.add(Restrictions.or(Restrictions.isNull(key),Restrictions.eq(key, "")));
			}
		}else{
			if(r.equals("like")){
				criteria.add(Restrictions.like(key, value));
			}else if(r.equals("between")){
				System.out.println("---"+value);
			}else if(r.equals("in")){
				HashSet<String> vs = new HashSet<String>();
				if(value.trim().startsWith("{")){
					DetachedCriteria sub = addSubWhere(value);
		            criteria.add(Property.forName(key).in(sub));
				}else{
					String[] values = StringUtils.split(value,",");
					for(String v : values){
						vs.add(v);
					}
					criteria.add(Restrictions.in(key, vs));
				}
				
			}else if(r.equals("not in")){
				if(value.startsWith("{")){
					JSONObject kidWhereJson = JSON.parseObject(value);
					String poName = kidWhereJson.getString("poName");
					String aidField = kidWhereJson.getString("aidField");
					Criteria kidCriteria = simpleDao.createCriteria(poName);
					handleCriteria(kidCriteria, kidWhereJson.getString("data"),poName);
					List<Object> kidList = kidCriteria.list();
					String[] kidValues = new String[kidList.size()];
					int index = 0;
					for(Object kid : kidList){
						try {
							Object aidValue = MethodUtils.invokeMethod(kid, "get"+StringUtils.capitalize(aidField));
							kidValues[index++] = aidValue.toString();
						} catch (NoSuchMethodException | IllegalAccessException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					value = StringUtils.join(kidValues, ",");
				}
				if(StringUtils.isNotBlank(value)){
					if("Integer".equals(fieldType)){
						HashSet<Integer> vs = new HashSet<Integer>();
						String[] values = StringUtils.split(value,",");
						for(String v : values){
							vs.add(Integer.parseInt(v));
						}
						criteria.add(Restrictions.not(Restrictions.in(key, vs)));
					}else{
						HashSet<String> vs = new HashSet<String>();
						String[] values = StringUtils.split(value,",");
						for(String v : values){
							vs.add(v);
						}
						criteria.add(Restrictions.not(Restrictions.in(key, vs)));
					}
				}
				
				
			}else if(r.equals("!=")){
				switch(fieldType){
					case "int":
					case "Integer":criteria.add(Restrictions.ne(key, new Integer(value)));
						break;
					case "Date" :criteria.add(Restrictions.ne(key, value));
						break;
					default :
						criteria.add(Restrictions.ne(key, value));
						break;
				}
				
			}else if(r.equals(">")){
				switch(fieldType){
					case "int":
					case "Integer":criteria.add(Restrictions.gt(key, new Integer(value)));break;
					case "Date" :
						Date end = null;
						if(value.equals("today")){
							end = DateTime.parse(DateTime.now().toString("yyyy-MM-dd")).toDate();
						}else{
							end = new DateTime(value).toDate();
						}
						criteria.add(Restrictions.gt(key, end));break;
					default :criteria.add(Restrictions.gt(key, value));break;
				}
			}else if(r.equals(">=")){
				switch(fieldType){
					case "int":
					case "Integer":
						criteria.add(Restrictions.ge(key, new Integer(value)));break;
					case "Date" :
						Date end = null;
						if(value.equals("today")){
							end = DateTime.parse(DateTime.now().toString("yyyy-MM-dd")).toDate();
						}else{
							end = new DateTime(value).toDate();
						}
						criteria.add(Restrictions.ge(key, end));break;
					default :criteria.add(Restrictions.ge(key, value));break;
				}
			}else if(r.equals("<")){
				switch(fieldType){
					case "int":
					case "Integer":criteria.add(Restrictions.lt(key, new Integer(value)));break;
					case "Date" :
						criteria.add(Restrictions.lt(key, new DateTime(value).toDate()));break;
					default :criteria.add(Restrictions.lt(key, value));break;
				}
			}else if(r.equals("<=")){
				switch(fieldType){
					case "int":
					case "Integer":criteria.add(Restrictions.le(key, new Integer(value)));break;
					case "Date" :
						Date end = null;
						if(value.equals("today")){
							end = DateTime.parse(DateTime.now().toString("yyyy-MM-dd")).toDate();
						}else{
							end = new DateTime(value).toDate();
						}
						criteria.add(Restrictions.lt(key, end));break;
					default :criteria.add(Restrictions.le(key, value));break;
				}
			}else
				switch(fieldType){
				case "int":
				case "Integer":criteria.add(Restrictions.eq(key, new Integer(value)));break;
				case "Date" :
				DateTime dt = new DateTime(value);
					if(value.length()==10){
						criteria.add(Restrictions.and(Restrictions.ge(key,dt.toDate()),Restrictions.lt(key, dt.plusDays(1).toDate())));
					}else{
						
					}
				
					break;
				default :criteria.add(Restrictions.eq(key, value));break;
			}
		}
	}
	private DetachedCriteria addSubWhere(String value) {
		JSONObject subJson = JSON.parseObject(value);
		String subPojoName = subJson.getString("pojo");
		ClassInfo subClassInfo = CodeHelper.getClassInfo(subPojoName);
		DetachedCriteria sub = null;
		try {
			sub = DetachedCriteria.forClass(Class.forName(subClassInfo.getFullName()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String subFieldName = subJson.getString("fieldName");
		sub.setProjection(Property.forName(subFieldName));
		JSONArray subWhereJson = subJson.getJSONArray("where");
		for(Object whereObj : subWhereJson){
			JSONObject wjo = (JSONObject)whereObj;
			if(wjo.containsKey("r")){
				
			}else{
				Set<String> keySet = wjo.keySet();
				Iterator<String> it = keySet.iterator();
				String whereKey = it.next();
				String subWhere = wjo.getString(whereKey);
				if(subWhere.trim().startsWith("{")){
					wjo.getJSONObject(whereKey).put("wFieldName", whereKey);
					DetachedCriteria sub2 = addSubWhere(wjo.getJSONObject(whereKey).toJSONString());
					String keyField = StringUtils.substringBefore(whereKey, "!!in");
					sub.add(Property.forName(keyField).in(sub2));
				}else{
					sub.add(Restrictions.eq(whereKey, subWhere));
				}
			}
		}
		return sub;
	}
	//用于构造criteria的or函数
	public void addParam(List<Criterion> sel,String fieldType,JSONObject jsonObject){
		String key = jsonObject.getString("name");
		String value = jsonObject.getString("value");
		String r = jsonObject.getString("r");
		if(StringUtils.isEmpty(value)){
			sel.add(Restrictions.isNull(key));
		}else{
			switch(fieldType){
			case "int":
			case "Integer":sel.add(Restrictions.eq(key, new Integer(value)));
			break;
			case "Date" :sel.add(Restrictions.eq(key, value));
			break;
			default : 
				if(r.equals("like")){
					sel.add(Restrictions.like(key, value));
				}else{
					sel.add(Restrictions.eq(key, value));
				}
				break;
			}
		}
		
	}
	public void order(Criteria criteria,GridSetting gridSetting){
		String orderbyString = gridSetting.getPageInfo().getOrderby();
		order(criteria, orderbyString);
	}
	public void order(Criteria criteria,String orderbyString){
		if(!StringUtils.isEmpty(orderbyString)){
			String orderbys[] = orderbyString.split(",");
			for(String orderby : orderbys){
				String propertyName = orderby.trim();
				orderby = StringUtils.trim(orderby);//去掉前后空格
				int a = StringUtils.countMatches(orderby," ");//判断还是否存在空格，有空格表示有desc或者asc
				if(a>0){
					String dir = StringUtils.substringAfterLast(orderby, " ");
					propertyName = StringUtils.substringBefore(orderby, " ");
					if(dir.equals("desc")){
						criteria.addOrder(Order.desc(propertyName));
					}else{
						criteria.addOrder(Order.asc(propertyName));
					}
				}else{
					criteria.addOrder(Order.asc(propertyName));
				}
			}
		}
		
	}
	public Long countRecords(Criteria criteria){
		criteria.setProjection(Projections.rowCount());
		Long num = Long.parseLong(criteria.uniqueResult().toString());
		return num;
	}
	//截取
	public List<?> cutList(Criteria criteria,GridSetting gridSetting){
		criteria.setFirstResult(calcuBeginRecordNum(gridSetting)).setMaxResults(gridSetting.getPageInfo().getRecords());
		List<?> list = criteria.list();
		return list;
	}
	public int calcuBeginRecordNum(GridSetting gridSetting){
		PageInfo pageInfo = gridSetting.getPageInfo();
		int beginRecordNum = (pageInfo.getShowPageNum()-1)*pageInfo.getRecords();//第一条记录
		return beginRecordNum;
	}
	public void setValueObject(String type,Map<Integer,Object> paramMap,JSONObject fieldObj){
		type = convertTypeToMy(type);
		switch(type){
			case "date" :
				paramMap.put(paramMap.size(), fieldObj.getDate("value"));
			break;
			case "int" :
				paramMap.put(paramMap.size(), fieldObj.getInteger("value"));
			break;
			default : 
				paramMap.put(paramMap.size(), fieldObj.getString("value"));
		}
	}
	public void setValueObject(String type,Map<Integer,Object> paramMap,Object value){
		type = convertTypeToMy(type);
		switch(type){
		case "date" :
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				paramMap.put(paramMap.size(), sdf.parse(value.toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case "int" :
			paramMap.put(paramMap.size(), Integer.parseInt(value.toString()));
			break;
		default : 
			paramMap.put(paramMap.size(), value.toString());
		}
	}
	/**
	 * 将java类型转换为ui可以识别的类型
	 * @param type
	 * @return String
	 */
	public String convertTypeToMy(String type){
		switch(type){
			case "int":
			case "Integer" : return "int";
			case "Date" : return "date";
			default : return "string";
		}
		
	}
}
