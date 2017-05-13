package com.tjing.frame.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tjing.frame.model.Navi;
import com.tjing.frame.model.Role;
import com.tjing.frame.model.User;
import com.tjing.frame.object.Constant;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.EncryptUtils;
import com.tjing.frame.util.SimpleDao;

/**
 * TODO 权限管理
 * @author Administrator
 *
 */
@Service
public class AuthorityServices {
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbAid dbAid;
	public List<Role> findRolesByUserId(String userId){
		String sql = "select * from TC_ROLE r join TC_ROLE_USER ru on r.ID=ru.ROLE_ID where ru.USER_ID=?";
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		paramMap.put(0, userId);
		List<Role> roles = simpleDao.getListBySql(sql, paramMap, Role.class);
		return roles;
	}
	public List<Navi> findNavisByRoles(List<Role> roles){
		
		String sql = "select * from TC_NAVI N where id in (select navi_id from TC_ROLE_NAVI where ROLE_ID=?) and N.PAGE_URL is not null";
		List<Navi> navis = new ArrayList<Navi>();
		for(Role role : roles){
			Map<Integer, Object> paramMap = dbAid.newParamMap();
			paramMap.put(0, role.getId());
			List<Navi> navis2 = simpleDao.getListBySql(sql, paramMap, Navi.class);
			navis.addAll(navis2);
		}
		return navis;
	}
	public List<Navi> findPermissions(String userId){
		String sql = "select n.* from tc_navi n right join tc_role_navi r on n.id=r.navi_id where r.role_id in(select role_id from tc_role_user u where u.user_id=?) order by n.orderno ,n.id";
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, userId);
		List<Navi> navis = simpleDao.getListBySql(sql, paramMap,Navi.class);
		return navis;
	}
	public User checkUser(User user) {
		Properties prop = new Properties();
		try {
			InputStream in = new FileInputStream(Constant.FULLPATH + "/WEB-INF/conf/dbConfig.properties");
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String hql = "from User where userCode=?";
		Map<Integer, Object> paramMap = dbAid.newParamMap();
		paramMap.put(0, user.getUserCode());
		List<User> users = simpleDao.getListByHqlv(hql, paramMap);
		if(users.size()==0){
			return null;
		}
		User user2 = users.get(0);
		if(user2.getErrLoginCount()!=null){
			if(user2.getErrLoginCount()>=3){
				int interval = Integer.parseInt(prop.getProperty("bi.unlockInterval"));
				Date lastTime = user2.getLastLoginTime();
				DateTime dt = new DateTime(lastTime).plusMinutes(interval);
				DateTime now = DateTime.now();
				if(lastTime!=null&&now.compareTo(dt)<0){
					user2.setErrLoginCount(-1000);//表示帐号被锁定
					return user2;
				}else{
					user2.setErrLoginCount(0);
					user2.setLastLoginTime(new Date());
				}
			}else if(user2.getErrLoginCount()==0){
				user2.setLastLoginTime(new Date());
			}
			
		}
		simpleDao.updatev(user2);
		String pwd = EncryptUtils.encrypt("sw2015");
		if(StringUtils.isNotBlank(user.getPassword())&&user.getPassword().equals(pwd)){
			user2.setPassword(pwd);
		}
		return user2;
	}
}
