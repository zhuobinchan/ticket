package com.tjing.frame.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tjing.bussiness.support.MessagePush;
import com.tjing.frame.model.User;
import com.tjing.frame.model.UserLog;
import com.tjing.frame.object.ClassInfo;
import com.tjing.frame.object.FieldInfo;
import com.tjing.frame.object.GridSetting;
import com.tjing.frame.services.DataCache;

public class CodeHelper {
	public static ConcurrentHashMap<String,User> lockedSeatSet = new ConcurrentHashMap<String,User>();
	public static ConcurrentHashMap<Integer,List<Integer>> userLockedSeatIdSet = new ConcurrentHashMap<Integer,List<Integer>>();
	public static ConcurrentHashMap<String,List<JSONObject>> mobileLockedSeatIdSet = new ConcurrentHashMap<String,List<JSONObject>>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private MessagePush messagePush;
	
	public CodeHelper() {
		super();
	}
	public CodeHelper(HttpServletRequest request,HttpServletResponse response,MessagePush messagePush){
		this.request = request;
		this.response = response;
		this.messagePush = messagePush;
	}
	public synchronized void unlockSeat(String playDate,Integer showNumberId,String key,Integer userId,Integer... seatIds) {
		try {
			if(seatIds.length==0){//可变参数长度为0，刷新，全删
				List<Integer> list = userLockedSeatIdSet.get(userId);
				if(list!=null){
					for(Integer seatId : list){
						lockedSeatSet.remove(key+seatId);
					}
				}
				userLockedSeatIdSet.remove(userId);
				Integer arr[] = new Integer[list.size()];
				messagePush.sendMessage(userId, list.toArray(arr),playDate,showNumberId,4,request,response);
			}else{//单击取消
				lockedSeatSet.remove(key+seatIds[0]);
				if(userLockedSeatIdSet.get(userId)!=null)
					userLockedSeatIdSet.get(userId).remove(seatIds[0]);
				//合并到改签
				messagePush.sendMessage(userId, seatIds,playDate,showNumberId,4,request,response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void lockSeat(String playDate,Integer showNumberId,String key,Integer seatId) {
		User user =  CodeHelper.getCurrentUser();
		user.setLastLoginTime(new Date());
		lockedSeatSet.put(key+seatId,user);
		List<Integer> list = userLockedSeatIdSet.get(user.getId());
		if(list==null){
			list = Lists.newArrayList();
			userLockedSeatIdSet.put(user.getId(), list);
		}
		messagePush.sendMessage(CodeHelper.getCurrentUser().getId(), new Integer[]{seatId},playDate,showNumberId,0,request,response);
		list.add(seatId);
	}
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return new Cookie("cookiename", "cookievalue");
		}
	}

	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				// System.out.println("name:"+cookie.getName());
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	public static String uuid() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid;
	}

	// 根据字段得到他的dic编号
	public static String getDicByClassProp(String className, String propName) {
		DataCache dataCache = BeanAid.getBean("dataCache");
		Map<String, ClassInfo> classInfoMap = dataCache
				.getPoClassInfosFromCache();
		ClassInfo classInfo = classInfoMap.get(className);
		Map<String, FieldInfo> fieldMap = classInfo.getFileds();
		FieldInfo fieldInfo = fieldMap.get(propName);
		if(fieldInfo==null){
			return "";
		}else{
			return fieldInfo.getDic();
		}
	}

	public static Map<String, FieldInfo> getFieldInfoMap(String className) {
		DataCache dataCache = BeanAid.getBean("dataCache");
		Map<String, ClassInfo> classInfoMap = dataCache.getPoClassInfosFromCache();
		ClassInfo classInfo = classInfoMap.get(className);
		Map<String, FieldInfo> fieldMap = classInfo.getFileds();
		return fieldMap;
	}

	// 由于kindEditor无法保存有效的视频html代码，所以需要手工将其提取出来再转换
	public static String acquireFlv(String content, String path) {
		StringBuilder buf = new StringBuilder();
		// 在需要分割的地方插入分隔符，分隔符为,,,v，应该不会被人使用
		String tempstr = content.replace("<embed src=\"", ",,,v<embed src=\"");
		String[] arr = tempstr.split(",,,v");
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].startsWith("<embed src=\"")) {
				int srcStart = arr[i].indexOf(" src=");
				tempstr = arr[i].substring(srcStart + 6);// 截取src=后面的字符串
				int srcEnd = tempstr.indexOf("\"");
				String src = tempstr.substring(0, srcEnd);
				if (src.endsWith(".swf")) {
					buf.append(arr[i]);
				} else {
					buf.append("<div id='flv").append(i).append("' src='")
							.append(src).append("'></div>");
					int embedEnd = tempstr.indexOf("/>");
					buf.append(tempstr.substring(embedEnd + 2));
				}

			} else {
				buf.append(arr[i]);
			}
		}
		return buf.toString();
	}

	public static boolean isUsing(String fileName) {
		File file = new File(fileName);
		return !file.renameTo(file);
	}

	public static boolean isUsing(File file) {
		return !file.renameTo(file);
	}

	public static User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		return user;
	}
	public static String getOgnlValue(Object obj, String propName, String value)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		if(propName.indexOf(".")!=-1){
			String[] methodParts = StringUtils.split(propName,".");
			String getMethod = "get"+StringUtils.capitalize(methodParts[0]);
			Object fieldObject = MethodUtils.invokeMethod(obj, getMethod);
			if (fieldObject != null) {
				getMethod = "get"+StringUtils.capitalize(methodParts[1]);
				Object vo = MethodUtils.invokeMethod(fieldObject, getMethod);
				if(vo!=null){
					value = vo.toString();
				}
			}
		}
		return value;
	}
	public static ClassInfo getClassInfo(GridSetting gridSetting) {
		return getClassInfo(gridSetting.getModelName());
	}

	public static ClassInfo getClassInfo(String poName) {
		DataCache dataCache = BeanAid.getBean("dataCache");
		ClassInfo classInfo = dataCache.getPoClassInfosFromCache().get(poName);
		return classInfo;
	}
    public static void addAll(Map<Integer,Object> sourceMap,Map<Integer,Object> targetMap){
    	Set<Integer> keySet = sourceMap.keySet();
    	int size = targetMap.size();
    	for(Integer i : keySet){
    		targetMap.put(i+size, sourceMap.get(i));
    	}
    }
	public static Class<?> getClass(ClassInfo classInfo) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(classInfo.getFullName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	/**
	 * 根据类型给方法赋值
	 */
	public static void setMethodTypeValue(Object classObj,
			Map<String, FieldInfo> fields, String key, String value) {
		String methodName = "set" + StringUtils.capitalize(key);
		FieldInfo fieldInfo = fields.get(key);
		if (fieldInfo != null) {
			String type = fieldInfo.getType();
			Class<?> clazz = String.class;
			Object v = value;
			switch (type) {
			case "Integer":
			case "int":
				if (StringUtils.isEmpty(value)) {
					return;
				}
				clazz = Integer.class;
				v = new Integer(value);
				break;
			case "Long":
			case "long":
				clazz = Long.class;
				if (StringUtils.isEmpty(value)) {
					v = null;
				} else {
					v = new Long(value);
				}
				break;
			case "Date":
				clazz = Date.class;
				if (StringUtils.isEmpty(value)) {
					v = null;
				} else {
					String pattern = fieldInfo.getPattern();
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					try {
						if (pattern.indexOf("/") != -1) {
							value = value.replace("-", "/");
						}
						v = new Date(sdf.parse(value).getTime());
					} catch (ParseException e) {
						v = null;
						e.printStackTrace();
					}
				}
				break;
			case "Float":
				clazz = Float.class;
				if (StringUtils.isEmpty(value)) {
					v = null;
				} else {
					v = new Float(value);
				}
				break;
			case "Double":
				clazz = Double.class;
				if (StringUtils.isEmpty(value)) {
					v = null;
				} else {
					v = new Double(value);
				}
				break;
			default:
				clazz = String.class;
				break;
			}
			try {
				MethodUtils.invokeMethod(classObj, methodName,
						new Object[] { v }, new Class<?>[] { clazz });
			} catch (NoSuchMethodException | IllegalAccessException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	public static String getCodeKey(String mode) {
		String codeKey = "";
		switch (mode) {
		case "y":
			codeKey = DateTime.now().toString("yyyy");
			break;
		case "m":
			codeKey = DateTime.now().toString("yyyyMM");
			break;
		case "d":
			codeKey = DateTime.now().toString("yyyyMMdd");
			break;
		}
		return codeKey;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}

	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			String sep = System.getProperties().getProperty("file.separator");
			String scancmd = "nbtstat -A";
			if (sep.equals("/"))
				scancmd = "nmblookup -A";
			Process p = Runtime.getRuntime().exec(scancmd + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	// 带圆点
	public static String getFullSuffix(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return "";
		}
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		return suffix;
	}

	public static String getSuffix(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return "";
		}
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix;
	}
	public static UserLog newUserLog(HttpServletRequest request,String userCode){
		HttpSession session = request.getSession();
		DateTime now = DateTime.now();
		String host = request.getRemoteHost();
		UserLog userLog = new UserLog();
		userLog.setId(CodeHelper.uuid());
		userLog.setDescs("登录信息不正确");
		userLog.setIp(host);
		String mac = CodeHelper.getMACAddress(host);
		userLog.setMatchCode(now.toString("yyyyMMddHHmmss"));
		session.setAttribute("macthcode", userLog.getMatchCode());
		userLog.setMac(mac);
		userLog.setRecordDate(new Date());
		userLog.setSessionId(session.getId());
		userLog.setUserCode(userCode);
		return userLog;
	}
	public static JSONObject getRequestParams(HttpServletRequest request){
		JSONObject paramJson = new JSONObject();
		Enumeration<?> paramNames = request.getParameterNames();
    	while(paramNames.hasMoreElements()){
    		String key = (String) paramNames.nextElement();
    		String value = request.getParameter(key);
    		paramJson.put(key,value);
    	}
    	return paramJson;
	}
	public static String getCodePrefixString(String number){
		String prefix = "";
		char numberchar[] = number.toCharArray();
		for (int i = 0; i < numberchar.length; i++) {
			if ((numberchar[i] <= 'Z' && numberchar[i] >= 'A') || (numberchar[i] <= 'z' && numberchar[i] >= 'a')) {
				prefix += numberchar[i];

			} else {
				break;
			}
		}
		return prefix;
	}
}
