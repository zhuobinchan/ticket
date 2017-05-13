package com.tjing.frame.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import net.sf.ehcache.Cache;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjing.bussiness.model.Area;
import com.tjing.bussiness.model.Seat;
import com.tjing.bussiness.object.AreaSeat;
import com.tjing.frame.controller.CommonController;
import com.tjing.frame.model.Dic;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.RequestObject;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.EncryptUtils;
import com.tjing.frame.util.SimpleDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

@Service
public class DataPerfect {
	@Autowired
	private Cache cache;
	@Autowired
	private SimpleDao simpleDao;
	//根据父ID，设置节点的层级
	public void setTier(RequestObject requestObject){
		//增加一级菜单或更新菜单
		for(JSONObject jobj : requestObject.getData()){
			if(!jobj.containsKey("id")){//根菜单
				if(jobj.containsKey("parentId")){
					Integer parentId = jobj.getInteger("parentId");
					ClassInfo classInfo = CodeHelper.getClassInfo(requestObject.getPoName());
					try {
						Class<?> clazz = Class.forName(classInfo.getFullName());
						Object obj = simpleDao.getEntity(clazz, parentId);
						Integer tier = (Integer) MethodUtils.invokeMethod(obj, "getTier");
						jobj.put("tier", tier+1);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					jobj.put("tier", 1);
				}
				
				
			}
		}
		requestObject.setPromise(true);
	}
	//设置用户数据
	public void setUserPassword(RequestObject requestObject){
		for(JSONObject jobj : requestObject.getData()){
			if(!jobj.containsKey("id"))
				jobj.put("password", EncryptUtils.encrypt("888888"));
		}
		cache.remove("com.tjing.frame.services.DataCache.getUserTopOrgIdFromCache");
		requestObject.setPromise(true);
	}
	public void setcreateTime(ClassInfo classInfo,JSONObject jobj){
		jobj.put("createTime", new Date());
	}
	//部门下面是否存在用户
	public boolean notExistsOrgUser(ClassInfo classInfo,String id){
		String hql = "select count(*) from User where orgId='" + id + "'";
		Integer num = simpleDao.getIntNumByHql(hql);
		return num==0;
	}
	//删除用户关联的数据
	public boolean deleteUserRelated(ClassInfo classInfo,String id){
		return true;
	}
	//删除区域关联数据
	public void beforeDeleteArea(RequestObject requestObject){
		try {
			List<String> arr = requestObject.getArr();
			List<Integer> arr2 = Lists.newArrayList();
			for(String id : arr){
				arr2.add(Integer.valueOf(id));
			}
			String hql = "delete from Seat where areaId in :ids";
			simpleDao.deleteByIds(hql, arr2);
			requestObject.setPromise(true);
		} catch (NumberFormatException e) {
			requestObject.setPromise(true);
			e.printStackTrace();
		}
	}
	//修改区域后刷新缓存
	public void refreshAreaCache(RequestObject requestObject){
		for(JSONObject jobj : requestObject.getData()){
			Area area = simpleDao.getEntity(Area.class, Integer.valueOf(jobj.getIntValue("id")));
			if(area!=null){
				DbServices.cache.invalidate(area.getId());
				//DbServices.cache2.invalidate(area.getCode());
				DbServices.tableHtmlCache.invalidate(area.getBelongto()+"");
			}
				
		}
		requestObject.setPromise(true);
	}
	//修改座位后刷新缓存
	public void modifySeat(RequestObject requestObject){
		for(JSONObject jobj : requestObject.getData()){
			Seat seat = simpleDao.getEntity(Seat.class, Integer.valueOf(jobj.getIntValue("id")));
			Area area = simpleDao.getEntity(Area.class, seat.getAreaId());
			DbServices.cache.invalidate(area.getId());
			DbServices.tableHtmlCache.invalidate(area.getBelongto()+"");
		}
		requestObject.setPromise(true);
	}
	//修改数据字典后刷新缓存
	public void removeDicCache(RequestObject requestObject){
		setTier(requestObject);
		cache.remove("com.tjing.frame.services.DataCache.getDicsFromCache");
	}
	//删除菜单前判断
	public void beforeRemoveNavis(RequestObject requestObject){
		try {
			List<String> arr = requestObject.getArr();
			List<Integer> arr2 = Lists.newArrayList();
			for(String id : arr){
				arr2.add(Integer.valueOf(id));
			}
			String hql = "delete from RoleNavi where naviId in :ids";
			simpleDao.deleteByIds(hql, arr2);
			requestObject.setPromise(true);
		} catch (NumberFormatException e) {
			requestObject.setPromise(true);
			e.printStackTrace();
		}
	}
	//删除角色时，同时删除角色菜单表相关记录
	public void deleteMappedRoleNavis(RequestObject requestObject){
		try {
			List<String> arr = requestObject.getArr();
			List<Integer> arr2 = Lists.newArrayList();
			for(String id : arr){
				arr2.add(Integer.valueOf(id));
			}
			String hql = "delete from RoleNavi where roleId in :ids";
			simpleDao.deleteByIds(hql, arr2);
			requestObject.setPromise(true);
		} catch (NumberFormatException e) {
			requestObject.setPromise(false);
		}
	}
}
