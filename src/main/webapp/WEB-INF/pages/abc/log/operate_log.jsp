<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/backbone-min.js?v=1"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0102']);
	var gridSetting = {
			id:'userGrid',
			po:"OperateLog",
			title:'操作日志列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 40,//auto表示根据容器高度自动判断
				orderby : "recordDate desc"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'userCode',
				   descs:"登录账号",
				   align:'center',
				   width:80
			   }      
			   ,{
				   name: 'userName',
				   descs:"用户名",
				   align:'center',
				   width:80
			   }  
			   ,{
				   name: 'descs',
				   descs:"操作描述",
				   align:"center",
				   width:120
			   }  
			   ,{
				   name: 'recordDate',
				   descs:"操作时间",
				   align:"center",
				   width:120
			   }    
			]
		};
	var grid;
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rtit">操作日志</div>
<div id="list" class="rbox1"></div>
</body>
</html>