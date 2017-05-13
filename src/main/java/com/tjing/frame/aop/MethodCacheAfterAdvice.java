package com.tjing.frame.aop;

import java.lang.reflect.Method;
import java.util.List;


import net.sf.ehcache.Cache;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.InitializingBean;

public class MethodCacheAfterAdvice implements AfterReturningAdvice,InitializingBean {

	private Cache cache;

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public MethodCacheAfterAdvice() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public void afterReturning(Object obj, Method method, Object[] objs,Object obj2) throws Throwable {
		System.out.println("after.......");
		String className = obj2.getClass().getName();
		List list = cache.getKeys();
		for (int i = 0; i < list.size(); i++) {
			String cacheKey = String.valueOf(list.get(i));
			if (cacheKey.startsWith(className)) {
				cache.remove(cacheKey);
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		if (cache == null) {
		}
	}

}