package com.tjing.frame.aop;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

public class MethodCacheInterceptor implements MethodInterceptor,InitializingBean {
	private Cache cache;

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public void afterPropertiesSet() throws Exception {

	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String targetName = invocation.getThis().getClass().getName();
		String methodName = invocation.getMethod().getName();
		Object[] arguments = invocation.getArguments();
		Object result;
		String cacheKey = getCacheKey(targetName, methodName, arguments);
		Element element = null;
		synchronized (this) {
			element = cache.get(cacheKey);
			//System.out.println(cacheKey+element);
			if (element == null) {
				// 调用实际的方法
				result = invocation.proceed();
				element = new Element(cacheKey, (Serializable) result);
				cache.put(element);
			}
		}
		return element.getValue();
	}

	private String getCacheKey(String targetName, String methodName,Object[] arguments) {
		StringBuffer sb = new StringBuffer();
		sb.append(targetName).append(".").append(methodName);
		if ((arguments != null) && (arguments.length != 0)) {
			for (int i = 0; i < arguments.length; i++) {
				sb.append(".").append(arguments[i]);
			}
		}
		return sb.toString();
	}
}