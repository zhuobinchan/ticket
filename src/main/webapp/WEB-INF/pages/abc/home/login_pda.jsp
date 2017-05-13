<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>信息管理平台</title>
<meta name="keywords" content="关键词">
<meta name="description" content="描述">
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="static/css/login.css1" />
<script type="text/javascript" src="static/js/jquery.js"></script>
<script type="text/javascript" src="static/js/encrypt.js"></script>
</head>
<body>

<form action="${pageContext.request.contextPath }/empda.htmls" method="post">
<input type="hidden" id="password" name="password"/>
<table align="center">
	<tr>
	<td>
	用户名
	</td>
	<td>
	<input id="username" name="username" type="text" class="input1" tabindex="1" autofocus/>
	</td>
	</tr>
	<tr>
	<td>
	密&nbsp;码
	</td>
	<td>
	<input type='password' id="password1" name="password1" value="" class="input2" autocomplete="off" tabindex="2"/>
	</td>
	</tr>
	<tr>
	<td align="center" colspan="2">
	<input id="loginBTN" type="button" value="登录" tabindex="3"/>
	</td>
	</tr>
	<tr>
	<td align="center" colspan="2">
	 <div id='error' class="login_boxs" style="color:#9AFF9A;width:250px;text-align: center;font-size: 11pt">${error }</div>
	</td>
	</tr>
</table>

</form>
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
	if(top!==self){
		top.location.assign('${pageContext.request.contextPath}');
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