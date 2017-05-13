package com.tjing.frame.services;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.model.Organization;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.object.PageInfo;
import com.tjing.frame.object.PageResultBean;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.SimpleDao;
@Service
public class OrganizationServices {
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbAid dbAid;
	public PageResultBean listOrgs(GridSetting gridSetting){
		String hql = "from Organization where 1=1";
		String queryString = gridSetting.getQueryParams();
		Map<String, Object> paramMap = dbAid.newStringParamMap();
		if(queryString!=null){
			JSONObject whereJson = JSON.parseObject(queryString);
			Set<Entry<String, Object>> keySet = whereJson.entrySet();
			Iterator<Entry<String, Object>> it = keySet.iterator();
			while(it.hasNext()){
				Entry<String, Object> item = it.next();
				String key = item.getKey();
				if(key.equals("id")){
					if(StringUtils.isEmpty(item.getValue().toString())){
						hql += " and ifnull(parentId,'')=''";
					}else{
						hql += " and (id=:id or parentId=:parentId)";
						ClassInfo classInfo = CodeHelper.getClassInfo("Organization");
						if(classInfo.getIdType().equals("Integer")){
							paramMap.put("id", Integer.valueOf(item.getValue().toString()));
							paramMap.put("parentId", Integer.valueOf(item.getValue().toString()));
						}else{
							paramMap.put("id", item.getValue());
							paramMap.put("parentId", item.getValue());
						}
					}
					
				}else{
					hql += " and " + key + " like :"+key;
					paramMap.put(key, "%" +item.getValue() + "%");
				}
			}
		}
		Long totalRecordNum = simpleDao.countRecordsWithStrParams("select count(*) " + hql, paramMap,Organization.class);
		PageInfo pageInfo = gridSetting.getPageInfo();
		if(StringUtils.isNotEmpty(pageInfo.getOrderby())){
			hql += " order by " + pageInfo.getOrderby(); 
		}
		int beginRecordNum = (pageInfo.getShowPageNum()-1)*pageInfo.getRecords();//第一条记录
		@SuppressWarnings("rawtypes")
		List list = simpleDao.getListByHqlWithStrParams(beginRecordNum,pageInfo.getRecords(),hql,paramMap);
		PageResultBean rb = new PageResultBean();
		rb.setList(list);
		rb.setTotalRecordNum(totalRecordNum);
		return rb;
	}
}
