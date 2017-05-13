<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>票务管理</title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="static/css/login.css" />
<script type="text/javascript" src="static/js/jquery.js"></script>
<script type="text/javascript" src="static/js/encrypt.js"></script>
</head>
<body>

<div class="login_tit">延边阿里郎剧院主办</div>
<form action="${pageContext.request.contextPath }/em.htmls" method="post">
<input type="hidden" value="${param.pccode }" id="pccode" name="pccode"/>
<input type="hidden" id="password" name="password"/>
<div class="login_box">
  <div class="login_boxs">
    <div class="login_boxl"><div>${theaterName}</div>售票系统</div>
    <div class="login_boxm">
      <ul>
        <li><input id="username" name="username" type="text" class="input1" tabindex="1" autofocus/></li>
        <li><input type='password' id="password1" name="password1" value="" class="input2" autocomplete="off" tabindex="2"/></li>
        <li><input name="captcha" type="text" class="input3" /><img id='captchaIMG' src="${pageContext.request.contextPath }/public/captcha" width="56" height="25" /><a id='linkcaptcha' href="http://www.163.com">看不清？刷新</a></li>
      </ul>
    </div>
    <div class="login_boxr"><input id="loginBTN" type="button" class="btn" tabindex="3"/></div>
  </div>
  <div id='error' class="login_boxs" style="color:#9AFF9A;width:250px;text-align: center;font-size: 11pt">${error }</div>
</div>

</form>
<div class="login_foot">&nbsp;</div>
<script type="text/javascript">
	var login = function(){
	var userName = $('#username').val();
	if(userName===''){
		$('#error').html('用户名不能为空');
		return;
	}
	var password = $('#password1').val();
	
	if(password===''){
		$('#error').html('密码不能为空');
		return;
	}
	var pwd = encrypt(password);
	$('#password').val(pwd);
	$('form').submit();
}
$(function(){
	var pccode = $('#pccode').val();
	if(!pccode){
		$('#pccode').val('${applicationScope.pccode }');
	}
	if(top!==self){
		top.location.assign('${pageContext.request.contextPath}/index.htmls?pccode='+$('#pccode').val());
	}
	$('#loginBTN').click(function(){
		login();
	})
	$('#linkcaptcha').click(function(e){
		e.preventDefault();
		$('#captchaIMG').attr('src',"${pageContext.request.contextPath }/public/captcha?"+new Date());
	});
	$('#username').focus()
	document.onkeydown = function(e){
		e = e ? e : event;
		if(e.keyCode===13){
			login();
		}
	}
})
</script>
</body>
</html>