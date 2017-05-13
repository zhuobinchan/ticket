package com.tjing.frame.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.model.Navi;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.object.PageInfo;
import com.tjing.frame.object.PageResultBean;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.SimpleDao;

@Service
public class NaviServices{
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbAid dbAid;
	/**
	 * to do 菜单维护页面，显示列表
	 * @param gridSetting
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public PageResultBean listNavis(GridSetting gridSetting){
		String hql = "from Navi where 1=1";
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
						Integer id = Integer.valueOf(item.getValue().toString());
						paramMap.put("id", id);
						paramMap.put("parentId", id);
					}
					
				}else{
					hql += " and " + key + " like :"+key;
					paramMap.put(key, "%" +item.getValue() + "%");
				}
			}
		}
		Long totalRecordNum = simpleDao.countRecordsWithStrParams("select count(*) " + hql, paramMap);
		PageInfo pageInfo = gridSetting.getPageInfo();
		if(StringUtils.isNotEmpty(pageInfo.getOrderby())){
			hql += " order by " + pageInfo.getOrderby(); 
		}
		int beginRecordNum = (pageInfo.getShowPageNum()-1)*pageInfo.getRecords();//第一条记录
		List list = simpleDao.getListByHqlWithStrParams(beginRecordNum,pageInfo.getRecords(),hql,paramMap);
		PageResultBean rb = new PageResultBean();
		rb.setList(list);
		rb.setTotalRecordNum(totalRecordNum);
		return rb;
	}
	/**
	 * to do 显示页面头部的导航栏
	 * @return
	 */
	public List<Navi> listNavis(String parentId){
		//User user = CodeHelper.getCurrentUser();
		
		//String hql = "select n.* from tc_navi n right join tc_role_navi r on n.id=r.navi_id where n.parent_id=?  order by n.tier,n.orderno,n.name";
		String hql = "select n.* from tc_navi n where n.parent_id=?  order by n.tier,n.orderno,n.name";
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		paramMap.put(0, parentId);
		List<Navi> list = simpleDao.getListBySql(hql, paramMap,Navi.class);
		return list;
	}
	public List<String> deletes(String data){
		List<String> list = new ArrayList<String>();
		JSONArray arr = JSONArray.parseArray(data);
		String sql = "delete from TC_ROLE_NAVI where NAVI_ID=?";
		String hql = "delete from Navi where id=?";
		for(int i=0;i<arr.size();i++){
			
			Map<Integer, Object> paramMap = dbAid.newParamMap();
			paramMap.put(0, arr.getString(i));
			Long count = simpleDao.countRecords("select count(*) from Navi where parentId=?", paramMap); 
			if(count==0){
				simpleDao.executeSql(sql, paramMap);
				int num = simpleDao.executeHql(hql, paramMap);
				if(num>0){
					list.add(arr.getString(i));
				}
			}
		}
		return list;
	}
	public Navi loadNavi(String id){
		Navi navi = simpleDao.getEntity(Navi.class, id);
		return navi;
	}
	public String findNaviPath(String naviPath ,Integer id){
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		paramMap.put(0, id);
		Navi navi = simpleDao.getEntity(Navi.class, id);
		if(navi.getTier()!=1){
			if(naviPath.length()==0){
				naviPath = navi.getName();
			}else
				naviPath = navi.getName() + ">" + naviPath;
			if(navi.getTier()>1){
				naviPath = findNaviPath(naviPath, navi.getParentId());
			}
		}
		return (naviPath);
	}
}
