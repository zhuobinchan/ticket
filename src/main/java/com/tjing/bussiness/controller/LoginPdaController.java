package com.tjing.bussiness.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.tjing.frame.model.Role;
import com.tjing.frame.model.User;
import com.tjing.frame.object.Constant;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.support.CaptchaException;
import com.tjing.frame.support.UsernamePasswordCaptchaToken;
import com.tjing.frame.util.CodeHelper;

@Controller
public class LoginPdaController {
	@Autowired
	private DbServices dbServices;
	@RequestMapping(value = "/loginpda")
	public String index() throws Exception {

		return "/abc/home/login_pda";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/empda",method=RequestMethod.POST)
	public ModelAndView login(String username,String password,String captcha, HttpServletRequest request) throws IOException {
		//常量赋值
		DateTime dt = new DateTime();
		Constant.CURRENT_YEARE = dt.getYear()+"";
		ModelAndView mav = new ModelAndView();
		mav.addObject("password",password);
		mav.addObject("userCode",username);
		mav.setViewName("forward:/loginpda.htmls");
		String host = request.getRemoteHost();
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(username, password.toCharArray(), true, host, captcha);
	    token.setRememberMe(false);
	    try {
		    currentUser.login( token );
		    User loginUser = ((User)currentUser.getPrincipal());
		    HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		    paramMap.put(0, new Date());
		    paramMap.put(1, loginUser.getId());
		    dbServices.executeByHql("update User set errLoginCount=0,lastLoginTime=? where id=?", paramMap);
		    loginUser.setIp(CodeHelper.getIpAddr(request));
		    paramMap = new HashMap<Integer,Object>();
		    paramMap.put(0, loginUser.getId());
		    List<Role> roles = dbServices.findListBySql("select r.* from tj_role r join tj_user_role ur on r.id=ur.role_id where ur.user_id=?", paramMap,Role.class);
		    String roleString = "";
		    for(Role r : roles){
		    	roleString += r.getName() + ",";
		    }
		    HttpSession session = request.getSession();
		    session.setAttribute("roleString", roleString);
		    mav.addObject("userName",loginUser.getName());
		    session.setAttribute("userInfo", loginUser);
			mav.setViewName("abc/home/pda");
		} catch ( UnknownAccountException uae ) {
			System.out.println("用户名错误");
			mav.addObject("error","用户信息输入不正确！");
		} catch ( IncorrectCredentialsException ice ) {
			System.out.println("密码错误");
			List<User> users = dbServices.findList("User", "{userCode:'"+ username + "'}", "");
			if(users.size()>0){
				User user = users.get(0);
				int vc = user.getErrLoginCount()==null?0:user.getErrLoginCount();
				user.setErrLoginCount(vc+1);
				dbServices.update(user);
			}
			mav.addObject("error","登陆失败，用户信息输入不正确！");
		} catch ( LockedAccountException lae ) {
		   System.out.println("帐号被锁定，请稍后重试");
		   mav.addObject("error","帐号被锁定，请稍后重试！");
		} catch(CaptchaException ce){
			System.out.println("验证码错误");
			mav.addObject("error","登陆失败，验证码错误！");
		}catch ( AuthenticationException ae ) {
		    System.out.println("其他错误");
		    ae.printStackTrace();
		    mav.addObject("error","登陆失败，未知错误！");
		}catch(Exception e){
			 mav.addObject("error","登陆失败，未知错误！");
			 e.printStackTrace();
		}
		return mav;
	}
}
