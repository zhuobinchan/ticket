package com.tjing.frame.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tjing.frame.model.Navi;
import com.tjing.frame.model.Role;
import com.tjing.frame.model.RoleNavi;
import com.tjing.frame.model.User;
import com.tjing.frame.model.UserRole;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.TreeData;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.SimpleDao;

@Service
public class DwrTool {
	@Autowired
	private DbAid dbAid;
	@Autowired
	private SimpleDao simpleDao;
	public String mapUserRole(Integer userId,Integer roleId){
		try {
			UserRole userRole = new UserRole();
			Role role = simpleDao.getEntity(Role.class, roleId);
			User user = simpleDao.getEntity(User.class, userId);
			userRole.setRoleId(roleId);
			userRole.setUserId(userId);
			userRole.setUserName(user.getName());
			userRole.setRoleName(role.getName());
			userRole.setRoleDesc(role.getDescs());
			simpleDao.persist(userRole);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "分配角色成功";
	}
	public List<TreeData> getRoleNaviList(Integer roleId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		List<TreeData> list = new ArrayList<TreeData>();
		joinTreeString(list,"Navi",null," tier,orderno,id",new String[]{"name"});
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		paramMap.put(0, roleId);
		List<RoleNavi> roleNavis = simpleDao.getListByHql("from RoleNavi where roleId=?", paramMap);
		List<Integer> naviIds = Lists.newArrayList();
		for(int i=0;i<roleNavis.size();i++){
			naviIds.add(roleNavis.get(i).getNaviId());
		}
		setNaviChecked(list, naviIds);
		return list;
	}
	public void setNaviChecked(List<TreeData> list,List<Integer> naviIds){
		String str = StringUtils.join(naviIds, ",") + ",";
		if(list.size()>0){
			for(TreeData tr : list){
				if(str.indexOf(tr.getId()+",")>-1){
					tr.setChecked(true);
					setNaviChecked(tr.getChildren(), naviIds);
				}
			}
		}
	}
	public void joinTreeString(List<TreeData> list,String poName,Integer parentId,String orderbyString,String[] fieldNames) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		ClassInfo classInfo = CodeHelper.getClassInfo(poName);
		String queryString = "{parentId:''}";
		if(parentId!=null){
			queryString = "{parentId:" + parentId + "}";
		}
		List<Object> objs = simpleDao.findList(poName, queryString, orderbyString);
		String textName = StringUtils.capitalize(fieldNames[0]);
		for(Object obj : objs){
			Object id = (Object) MethodUtils.invokeMethod(obj, "getId");
			Object pid = (Object) MethodUtils.invokeMethod(obj, "getParentId");
			String text = (String) MethodUtils.invokeMethod(obj, "get"+textName);
			TreeData treeData = null;
			if(classInfo.getFileds().get("code")==null){
				treeData = new TreeData(id.toString(),pid,text);
			}else{
				String code = (String) MethodUtils.invokeMethod(obj, "getCode");
				treeData = new TreeData(id.toString(),pid,text,code);
			}
			if(pid==null){
				treeData.setOpen(true);
			}
			List<TreeData> list2 = new ArrayList<TreeData>();
			treeData.setChildren(list2);
			list.add(treeData);
			joinTreeString(list2,poName,Integer.valueOf(id.toString()),orderbyString,fieldNames);
		}
	}
	public String mapRoleNavi(Integer roleId,Integer[] naviIds){
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		paramMap.put(0, roleId);
		simpleDao.executeHql("delete RoleNavi where roleId=?", paramMap);
		Role role = simpleDao.getEntity(Role.class, roleId);
		for(Integer id : naviIds){
			RoleNavi roleNavi = new RoleNavi();
			Navi navi = simpleDao.getEntity(Navi.class, id);
			roleNavi.setNaviId(id);
			roleNavi.setRoleId(roleId);
			roleNavi.setNaviName(navi.getName());
			roleNavi.setRoleDesc(role.getDescs());
			roleNavi.setRoleName(role.getName());
			simpleDao.persist(roleNavi);
		}
		return "保存菜单权限成功";
	}
	//pid:目标id
	public int moveNaviNode(int pid,int id){
		HashMap<Integer,Object> paramMap = Maps.newHashMap(); 
		paramMap.put(0, pid);
		paramMap.put(1, id);
		simpleDao.executeHql("update Navi set parentId=? where id=?", paramMap);
		return 1;
	}
}
