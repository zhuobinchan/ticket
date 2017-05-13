package com.tjing.frame.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.sql.DataSource;
import net.sf.ehcache.Ehcache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.annotation.JSONField;
import com.tjing.frame.annotation.FieldDesc;
import com.tjing.frame.model.Dic;
import com.tjing.frame.model.Organization;
import com.tjing.frame.model.User;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.FieldInfo;
import com.tjing.frame.util.DbAid;
import com.tjing.frame.util.SimpleDao;
@Component
public class DataCache {
	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private Ehcache ehCache;
	@Autowired
	private DbAid dbAid;
	@Autowired
	private DataSource dataSource;
	public Map<String,ClassInfo> getPoClassInfosFromCache(){
		Map<String,ClassInfo> poMap = new HashMap<String,ClassInfo>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "com/tjing/**/model" + "/" + this.resourcePattern;
		ResourcePatternResolver rp = new PathMatchingResourcePatternResolver();
		Resource[] ress;
		try {
			ress = rp.getResources(packageSearchPath);
			URL rootClassPath = DataCache.class.getResource("/");
			for(Resource res : ress){
				String classPath = res.getURL().getPath().replace(rootClassPath.getPath(), "");
				classPath = classPath.replace("/", ".");
				classPath = StringUtils.remove(classPath, ".class");
				try {
					Class<?> classObject = getClass().getClassLoader().loadClass(classPath);
					if(classObject.isAnnotationPresent(Entity.class)){
						String tableName = classObject.getSimpleName();
						if(classObject.isAnnotationPresent(Table.class)){
							if(StringUtils.isNotEmpty(classObject.getAnnotation(Table.class).name())){
								tableName = classObject.getAnnotation(Table.class).name();
							}
						}
						ClassInfo classInfo = new ClassInfo(classObject.getName(),tableName);
						classInfo.setName(classObject.getSimpleName());
						Field[] classFields = classObject.getDeclaredFields();
						Class<?> supperClass = classObject.getSuperclass();
						
						Map<String,FieldInfo> fields = new HashMap<String,FieldInfo>();
						
						handleFields(classFields, fields);
						if(!supperClass.getSimpleName().equals("Object")){
							handleFields(supperClass.getDeclaredFields(), fields);
						}
						classInfo.setFileds(fields);
						classInfo.setIdType(fields.get("id").getType());
						poMap.put(classObject.getSimpleName(), classInfo);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return poMap;
	}
	private void handleFields(Field[] classFields, Map<String, FieldInfo> fields) {
		for(Field field : classFields){
			int length = 0;
			String dbFieldName = field.getName();
			if(field.isAnnotationPresent(Column.class)){
				Column columnAnn = field.getAnnotation(Column.class);
				if(StringUtils.isNotEmpty(columnAnn.name())){
					dbFieldName = columnAnn.name();
				}
				length = columnAnn.length();
			}else if(field.isAnnotationPresent(JoinColumn.class)){
				if(StringUtils.isNotEmpty(field.getAnnotation(JoinColumn.class).name()))
					dbFieldName = field.getAnnotation(JoinColumn.class).name();
			}
			FieldInfo fieldInfo = new FieldInfo(field.getName(),dbFieldName,field.getType().getSimpleName());
			fieldInfo.setLength(length);
			if(field.isAnnotationPresent(FieldDesc.class)){
				FieldDesc fieldInfoAnn = field.getAnnotation(FieldDesc.class);
				String dic = fieldInfoAnn.dic();
				fieldInfo.setDic(dic);
				String comment = fieldInfoAnn.comment();
				fieldInfo.setComment(comment);
			}else{
				if(!field.getName().equals("id"))
					continue;
			}
			if(field.isAnnotationPresent(JSONField.class)){
				JSONField fieldInfoAnn = field.getAnnotation(JSONField.class);
				String pattern = fieldInfoAnn.format();
				fieldInfo.setPattern(pattern);
			}
			fields.put(field.getName(), fieldInfo);
		}
	}
	public Map<String,Dic> getDicsFromCache(){
		LinkedHashMap<String,Dic> dics = new LinkedHashMap<String,Dic>();
		List<Dic> list = simpleDao.findList("Dic", "","orderno,code");
		for(Dic dic : list){
			if(StringUtils.isNotEmpty(dic.getCode())){
				dics.put(dic.getCode(), dic);
				List<Dic> list2 = simpleDao.findList("Dic", "{parentId:'"+dic.getId()+"'}","orderno,code");
				for(Dic dic2 : list2){
					if(StringUtils.isNotEmpty(dic2.getCode()))
						dic.getDicMap().put(dic2.getCode(), dic2);
				}
			}
		}
		return dics;
	}
	/**
	 * @todo 取得用户ID和单位的对应关系
	 * @return
	 */
	public Map<Integer,Integer> getUserTopOrgIdFromCache(){
		LinkedHashMap<Integer,Integer> userMap = new LinkedHashMap<Integer,Integer>();
		List<User> list = simpleDao.findList("User", "","");
		for(User user : list){
			Integer orgId = user.getOrgId();
			Organization org = simpleDao.getEntity(Organization.class, orgId);
			if(org==null){
				continue;
			}
			if(org.getParentId()==null){
				userMap.put(user.getId(), org.getId());
			}else{
				org = simpleDao.getEntity(Organization.class, org.getParentId());
				if(org.getParentId()==null){
					userMap.put(user.getId(), org.getId());
				}else{
					org = simpleDao.getEntity(Organization.class, org.getParentId());
					if(org.getParentId()==null){
						userMap.put(user.getId(), org.getId());
					}else{
						org = simpleDao.getEntity(Organization.class, org.getParentId());
					}
				}
			}
		}
		return userMap;
	}
	public long getFloorNumFromCache(){
		long count = simpleDao.countRecords("select count(distinct floorNum) from Area", null);
		return count;
	}
}
