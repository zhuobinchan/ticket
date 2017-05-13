package com.tjing.frame.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.object.PageInfo;
import com.tjing.frame.object.PageResultBean;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.SimpleDao;

@Service 
public class UiServices {
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbAid dbAid;
	public JSONArray queryEntityDicValue(String sql,String id){
		JSONArray v = simpleDao.getEntityDicValue(sql,id);
		return v;
	}
	public PageResultBean listSimple(GridSetting gridSetting) throws ClassNotFoundException{
		PageResultBean rb = simpleDao.findPageResult(gridSetting);
		return rb;
	}
	public PageResultBean listBySql(GridSetting gridSetting) throws ClassNotFoundException{
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		PageResultBean rb = new PageResultBean();
		String queryString = gridSetting.getQueryParams();
		JSONObject queryJson = JSON.parseObject(queryString);
		Set<String> keySet = queryJson.keySet();
		PageInfo pageInfo = gridSetting.getPageInfo();
		String poName = gridSetting.getModelName();
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		int index = 0;
		String sql = "select * from " + classInfo.getTableName() + " t where 1=1";
		for(String key : keySet){
			sql += " and " +  key + "=?";
			paramMap.put(index++, queryJson.get(key));
		}
		pageInfo.setOrderby(pageInfo.getOrderby().replace("xuanze ", "select ").replace(" cong ", " from "));
		sql += " order by "+pageInfo.getOrderby();
		Class<?> clazz = Class.forName(classInfo.getFullName());
		Long totalCount = simpleDao.countRecords(clazz, queryString);
		int beginNum = dbAid.calcuBeginRecordNum(gridSetting);
		List<Object> list = simpleDao.getListBySql(beginNum,pageInfo.getRecords(),sql, paramMap,clazz);
		rb.setList(list);
		rb.setTotalRecordNum(totalCount);
		return rb;
	}
}
