package com.tjing.shiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tjing.frame.model.User;
import com.tjing.frame.services.AuthorityServices;
import com.tjing.frame.support.CaptchaException;
import com.tjing.frame.support.UsernamePasswordCaptchaToken;
import com.tjing.web.servlet.CaptchaServlet;
import com.alibaba.fastjson.JSON;


//认证数据库存储
@Component("defaultShiroRealm")
public class DefaultShiroRealm extends AuthorizingRealm {
	@Autowired
	private AuthorityServices authorityServices;
	public DefaultShiroRealm() {
		// 认证
		// super.setAuthenticationCacheName(GlobalStatic.authenticationCacheName);
		//super.setAuthenticationCachingEnabled(false);
		// 授权
		//super.setAuthorizationCacheName("shiro-authorizationCacheName");
	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		SimpleAuthorizationInfo sazi = new SimpleAuthorizationInfo();
		return sazi;
	}

	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordCaptchaToken upToken = (UsernamePasswordCaptchaToken) authcToken;
		String username = upToken.getUsername();
		if (username == null) {
			 throw new AccountException("用户信息不正确。");
		}
		
		String captcha = upToken.getCaptcha();
		String exitCode = (String) SecurityUtils.getSubject().getSession().getAttribute(CaptchaServlet.KEY_CAPTCHA);
		if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
			throw new CaptchaException("验证码错误");
		}
		
		String pwd = new String(upToken.getPassword());
		User user = new User();
		user.setUserCode(username);
		user.setPassword(pwd);
		user = authorityServices.checkUser(user);
		if(user==null){
			throw new UnknownAccountException("用户信息不正确");
		}else{
			if(user.getErrLoginCount()!=null&&user.getErrLoginCount()==-1000){
				throw new LockedAccountException();
			}
			AuthenticationInfo authinfo = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
			return authinfo;
		}
	}
	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
}
