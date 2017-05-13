package com.tjing.frame.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tjing.bussiness.model.Theater;
import com.tjing.frame.model.Navi;
import com.tjing.frame.model.Role;
import com.tjing.frame.model.User;
import com.tjing.frame.object.Constant;
import com.tjing.frame.services.DataCache;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.support.CaptchaException;
import com.tjing.frame.support.UsernamePasswordCaptchaToken;
import com.tjing.frame.util.CodeHelper;

@Controller
public class LoginController {
	@Autowired
	private DbServices dbServices;
	@RequestMapping(value = "/unauth",method=RequestMethod.GET)
	public String unauth(Model model,HttpServletRequest request) throws Exception {
		System.out.println("error/unauth");
		return "/error/unauth";
	}
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request) throws Exception {
		String name = dbServices.getDicTextByCode("0104001");
		if (name == null || "".equals(name.trim())) {
			name = "剧院";
		}
		request.setAttribute("theaterName", name);
		return "/abc/home/login";
	}
	@RequestMapping(value="/em",method=RequestMethod.POST)
	public ModelAndView login(String username,String password,String pccode,String captcha, HttpServletRequest request) throws IOException {
		//常量赋值
		DateTime dt = new DateTime();
		Constant.CURRENT_YEARE = dt.getYear()+"";
		/**/
		ModelAndView mav = new ModelAndView();
		mav.addObject("password",password);
		mav.addObject("userCode",username);
		mav.setViewName("forward:/index.htmls");
		HttpSession session = request.getSession();
    	
		String host = request.getRemoteHost();
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(username, password.toCharArray(), true, host, captcha);
	    token.setRememberMe(false);
	    try {
		    currentUser.login( token );
		    User loginUser = ((User)currentUser.getPrincipal());
		    if(StringUtils.isEmpty(loginUser.getFetureLetter())){
		    	 pccode = pccode==null?"1":pccode;
			     pccode = StringUtils.isEmpty(pccode)?"1":pccode;
			     session.getServletContext().setAttribute("pccode", pccode);
		    }else{
		    	pccode = null;
		    }
		    HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		    paramMap.put(0, new Date());
		    paramMap.put(1, loginUser.getId());
		    dbServices.executeByHql("update User set errLoginCount=0,lastLoginTime=? where id=?", paramMap);
		    loginUser.setIp(CodeHelper.getIpAddr(request));
		    List<Navi> apps = findApps(loginUser.getId());
		    paramMap = new HashMap<Integer,Object>();
		    paramMap.put(0, loginUser.getId());
		    List<Role> roles = dbServices.findListBySql("select r.* from tj_role r join tj_user_role ur on r.id=ur.role_id where ur.user_id=?", paramMap,Role.class);
		    String roleString = "";
		    for(Role r : roles){
		    	roleString += r.getName() + ",";
		    }
		    session.setAttribute("roleString", roleString);
		    mav.addObject("apps",apps);
		    mav.addObject("userName",loginUser.getName());
		    session.setAttribute("userInfo", loginUser);
			//Map<Integer, Integer> userTopOrgMap = dataCache.getUserTopOrgIdFromCache();
			loginUser.setPccode(pccode);
			
			String theatersn = loginUser.getTheaterSn();
			if(StringUtils.isNotEmpty(theatersn)){
				paramMap = new HashMap<Integer,Object>();
				paramMap.put(0, theatersn);
				List<Theater> theaters = dbServices.findListByHql("from Theater where sn=?", paramMap);
				Theater theater = theaters.get(0);
				session.setAttribute("theater", theater);
			}
			
			dbServices.fetchSeatList(mav,1);
			mav.setViewName("abc/home/index");
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
	@SuppressWarnings("unused")
	private void setTimeout(Session session) {
		session.setTimeout(1000*30*60);
	}
	//如果直接输入登录地址，则使用此方法判断
	@RequestMapping(value="/em",method=RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) throws ExecutionException {
		System.out.println("get");
		Subject currentUser = SecurityUtils.getSubject();
		ModelAndView mav = new ModelAndView();
		if(currentUser.isAuthenticated()){
			HttpSession session = request.getSession();
			Theater theater = (Theater) session.getAttribute("theater");
			User loginUser = ((User)currentUser.getPrincipal());
			mav.addObject("username",loginUser.getUserCode());
			mav.setViewName("abc/home/index");
		    List<Navi> apps = findApps(loginUser.getId());
		    HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
			paramMap.put(0, 1);
			session.setAttribute("theater", theater);
			dbServices.fetchSeatList(mav,1);
		    mav.addObject("apps",apps);
		}else{
			mav.setViewName("forward:/login.jsp");
		}
		return mav;
	}
	@RequestMapping(value="/public/logout",method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		request.getSession().setAttribute("LogDescs", "注销系统");
		SecurityUtils.getSubject().logout();
		mav.setViewName("redirect:/index.htmls");
		return mav;
	}
	@RequestMapping(value="/public/refreshOrClosePage")
	public ModelAndView logoffOr(HttpServletRequest request,HttpServletResponse response){
		System.out.println("40秒后过期");
		ModelAndView mav = new ModelAndView();
		Session session = SecurityUtils.getSubject().getSession();
		//System.out.println("刷新或者关闭页面");
		if(session!=null){
			session.setTimeout(1000*40);//40秒钟后过期
		//	System.out.println(session.getId());
			session.setAttribute("LogDescs", "关闭页面");
		}
		mav.setViewName("redirect:/em.html");
		return mav;
	}
	@RequestMapping(value="/public/logoutSys")
	public ModelAndView logoutSys(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		request.getSession().setAttribute("LogDescs", "注销系统");
		mav.setViewName("redirect:/index.htmls");
		return mav;
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/public/findLeftNavis")
	public ResponseEntity<List<Navi>> findLeftNavis(Integer id){
		String sql = "select n.* from ((tj_navi n join tj_role_navi rn on n.id=rn.navi_id) join tj_role r on r.id=rn.role_id) join tj_user_role ur on ur.role_id=r.id where n.parent_id=? and ur.user_id=? group by n.id order by n.orderno ,n.id";
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, id);
		paramMap.put(1, CodeHelper.getCurrentUser().getId());
		List<Navi> navis = dbServices.findListBySql(sql, paramMap,Navi.class);
		sql = "select n.* from ((tj_navi n join tj_role_navi rn on n.id=rn.navi_id) join tj_role r on r.id=rn.role_id) join tj_user_role ur on ur.role_id=r.id where n.parent_id=? and ur.user_id=? group by n.id order by n.orderno ,n.id";
		for(Navi navi : navis){
			paramMap.put(0, navi.getId());
			List<Navi> navis2 = dbServices.findListBySql(sql, paramMap,Navi.class);
			navi.setChildren(navis2);
		}
		ResponseEntity<List<Navi>> res = new ResponseEntity<List<Navi>>(navis,HttpStatus.OK);
		return res;
	}
	private List<Navi> findApps(Integer userId){
		String sql = "select n.* from ((tj_navi n join tj_role_navi rn on n.id=rn.navi_id) join tj_role r on r.id=rn.role_id) join tj_user_role ur on ur.role_id=r.id where n.tier=1 and ur.user_id=? group by n.id order by n.orderno ,n.id";
		HashMap<Integer, Object> paramMap = new HashMap<Integer,Object>();
		paramMap.put(0, userId);
		@SuppressWarnings("unchecked")
		List<Navi> apps = dbServices.findListBySql(sql, paramMap,Navi.class);
		return apps;
	}
	@RequestMapping(value="/public/modPassword",produces = {"application/json;charset=UTF-8"})
	public ResponseEntity<String> modPassword(@RequestParam String pwd,String pwd2,HttpServletRequest request){
		ResponseEntity<String> res = null;
		Integer userId = CodeHelper.getCurrentUser().getId();
		User user = dbServices.getEntity(User.class, userId);
		if(user.getPassword().equals(pwd)){
			user.setPassword(pwd2);
			dbServices.update(user);
			res = new ResponseEntity<String>("密码修改成功",HttpStatus.OK);
		}else{
			res = new ResponseEntity<String>("旧密码输入不正确",HttpStatus.OK);
		}
		return res;
	}
}
