package com.tjing.bussiness.support;

import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.WebContextFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tjing.frame.util.CodeHelper;
@Component
public class MessagePush {
	public void onPageLoad(Integer userId) {
		if(userId!=null){
			ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
            scriptSession.setAttribute("userId", userId);
		}
		DwrScriptSessionManagerUtil dwrScriptSessionManagerUtil = new DwrScriptSessionManagerUtil();
		try {
			dwrScriptSessionManagerUtil.init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageAuto(String userid, String message) {
		final String userId = userid;
		final String autoMessage = message;
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				if ("any" == userId){
					return true;
				}else{
					return false;
				}
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();
			public void run() {
				script.appendCall("showMessage", autoMessage);
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					if (scriptSession.getAttribute("userId").equals("any")) {
				       scriptSession.addScript(script);
				    }
				}
			}
		});
	}
	public void sendMessage(Integer userid, Integer[] theSeatIds,String playDate,Integer showNumberId,int type,final HttpServletRequest request,final HttpServletResponse response) {
		
		final Integer userId = userid ;
		Integer userId2 = CodeHelper.getCurrentUser().getId();
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				if (session.getAttribute("userId") == null){
					return false;
				}else{
					boolean show = (userId2.intValue()==userId.intValue());
					return show;
				}
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();
			public void run() {
				script.appendCall("showMessage", theSeatIds,playDate,showNumberId,type);
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					if (!scriptSession.getAttribute("userId").equals(userId2)) {
			           scriptSession.addScript(script);
			        }
				}
			}
		});
	}
	//专门针对预订类型，快速预订和预订需要标识出来
	public void sendMessage2(JSONObject attObj, JSONArray arr,final HttpServletRequest request,final HttpServletResponse response) {
		final Integer userId = attObj.getInteger("createUserId") ;
		Integer userId2 = CodeHelper.getCurrentUser().getId();
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				if (session.getAttribute("userId") == null){
					return false;
				}else{
					boolean show = (userId2.intValue()==userId.intValue());
					return show;
				}
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();
			public void run() {
				script.appendCall("showMessage2", arr,attObj);
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					if (!scriptSession.getAttribute("userId").equals(userId2)) {
						scriptSession.addScript(script);
					}
				}
			}
		});
	}

}
