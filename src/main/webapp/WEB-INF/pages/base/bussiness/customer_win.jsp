<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css?1" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0202','0206']);
	var gridSetting = {
			id:'customerGrid',
			po:"Customer",
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 6,//auto表示根据容器高度自动判断
				orderby : "spell,code"
			},
			fields:[
			   {	name:'id',
					hide:true
			   }, {	name:'shortname',
					hide:true
			   },{
				   name: 'spell',
				   descs:"字母检索",
				   align:'center',
				   width:50
			   },{
				   name: 'name',
				   descs:"客户全称",
				   align:'center',
				   width:130
			   },{
				   name: 'cheapPrice',
				   descs:"优惠金额",
				   align:'center',
				   width:50
			   }  ,{
				   name: 'balance',
				   descs:"账户余额",
				   align:'center',
				   width:50
			   }  
			]
		};
	var grid;
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		$(document).keydown(function(e){
			var keycode = e.keyCode;
			e.preventDefault();
			if(keycode<65||keycode>=90){
				if(keycode!=8)
				return;
			}
			
			var letter = String.fromCharCode(keycode);
			
			var spell = $('#letter').val();
			if(keycode==8){
				if(spell.length>0)
					spell = spell.substring(0,spell.length-1);
			}else{
				spell = spell + letter;
			}
			$("#letter").val(spell);
			if(spell.length>0){
				grid.load({data:{r:"like",name:"spell",value:spell+"%"}});
			}else{
				grid.load();
			}
			
		})
    });
   
    //-->
</SCRIPT>
</head>

<body>
<div class="rcon" style="height: 350px">
	<input type="text" id="letter"/>
<div id="list"></div>
</div>

<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
</body>
</html>