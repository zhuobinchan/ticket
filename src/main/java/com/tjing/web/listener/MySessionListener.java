package com.tjing.web.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.joda.time.DateTime;

public class MySessionListener implements SessionListener {

	@Override
	public void onExpiration(Session session) {
		System.out.println("过期");
		System.out.println(session.getId() + "  session过期时间："+new DateTime().toString("HH:mm:ss"));
	}

	@Override
	public void onStart(Session session) {
		System.out.println(session.getId() + "  session创建时间："+new DateTime().toString("HH:mm:ss"));
		
	}

	@Override
	public void onStop(Session session) {
		System.out.println(session.getId() + "  session停止时间："+new DateTime().toString("HH:mm:ss"));
	}
	 
}
