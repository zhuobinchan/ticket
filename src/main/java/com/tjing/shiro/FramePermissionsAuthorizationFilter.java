package com.tjing.shiro;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.stereotype.Component;


@Component("frameperms")
public class FramePermissionsAuthorizationFilter extends
		PermissionsAuthorizationFilter {
	@Override
	public boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		Subject subject = getSubject(request, response);
		String uri = req.getRequestURI();
		if(uri!=null&&(uri.equals(req.getContextPath())||uri.equals(req.getContextPath()+"/"))){
			subject.getSession().stop();
		}
		if(StringUtils.isNotBlank(req.getQueryString())){
			uri += "?"+req.getQueryString();
			uri = StringUtils.substringBefore(uri, "&pageNum");
		}
		String contextPath = req.getContextPath();
		if(uri.startsWith(contextPath)){
			uri = StringUtils.substringAfter(uri, contextPath+"/");
		}
		boolean permitted = true;
		if(StringUtils.isNotEmpty(uri)){
			permitted= subject.isPermitted(uri);
		}
		return permitted;

	}
}
