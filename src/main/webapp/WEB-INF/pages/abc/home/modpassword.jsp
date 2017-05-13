<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
	<script type="text/javascript" src="<c:url value="/static/js/jquery.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/encrypt.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/formview.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/css.css"/>"  />
	<script type="text/javascript" src="<c:url value="/static/layer/layer.js"/>"></script>
	<SCRIPT type="text/javascript">
		var closeWin = function(){
	    	var index = parent.layer.getFrameIndex(window.name);
		    parent.layer.close(index);
	    }
		function intitToolbarEvent(){
	  		$("#update").click(function(){
	  			save();
	  		})
	  		$("#close").click(function(){
	  			closeWin();
	  		})
	  	}
		function save(){
			var pwd = $("#password").val();
			var pwd1 = $("#password1").val();
			var pwd2 = $("#password2").val();
			if(pwd==""){
				layer.alert("请输入旧密码");
				return;
			}
			if(pwd1==""){
				layer.alert("请输入新密码");
				return;
			}
			if(pwd1!=pwd2){
				layer.alert("两次输入密码不一致");
				return;
			}
			/* if(pwd1.length<8){
				layer.alert("密码长度不能小于8");
				return;
			} */
			var isAllNum = true;
  			var numstr = "0123456789";
  			for(var i=0;i<pwd2.length;i++){
  				if(numstr.indexOf(pwd2.charAt(i))==-1)
  					isAllNum = false;
  			}
  			/* if(isAllNum){
  				layer.alert("为了您的密码安全，请不要输入纯数字");
  				return false;
  			} */
  			/* isAllNum = true;
  			var letter = "abcdefghijklmnopqrstuvwxyz";
  			for(var i=0;i<pwd2.length;i++){
  				if(letter.indexOf(pwd2.charAt(i))==-1)
  					isAllNum = false;
  			}
  			if(isAllNum){
  				layer.alert("为了您的密码安全，请不要输入纯字母");
  				return false;
  			} */
			pwd = encrypt(pwd);
			pwd2 = encrypt(pwd2);
			$.ajax({
				url:'<c:url value="/public/modPassword.htmls"/>',
				type:'post',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",  
				dataType:'html',
				data:{pwd:pwd,pwd2:pwd2},
				success:function(ret){
					layer.alert(ret)
				}
			})
		}
		$(document).ready(function(){
			intitToolbarEvent();
		});
		
		//-->
	</SCRIPT>  
  </head>
  <body>
  <div class="rcon" style="height: 300px">
<div id="passwordForm" style="border:1px solid">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ctable">
  <tr>
    <td width="130" align="right">输入旧密码:</td>
    <td><input id="password" type="password" class="iw190"/></td>
  </tr>
  <tr>
    <td width="130" align="right">输入新密码:</td>
    <td><input id="password1" type="password" class="iw190" /></td>
  </tr>
  <tr>
    <td width="130" align="right">确认新密码:</td>
    <td><input id="password2" type="password" class="iw230" /></td>
  </tr>
  <tr>
    <td align="center" colspan="2">
    <a class="btn2" id="update">修改</a>
    <a class="btn2" id="close">取消</a>
    </td>
  </tr>
  </table>

</div>
<br/>
<fieldset>
密码长度不能小于8位，且必须是字母和数字的组合
</fieldset>
	</div>
  </body>
</html>
