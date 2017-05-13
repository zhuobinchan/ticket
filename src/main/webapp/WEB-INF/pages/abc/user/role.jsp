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
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/DwrTool.js'></script>
<SCRIPT type="text/javascript">
    <!--
	var gridSetting = {
			id:'roleGrid',
			po:"Role",
			title:'角色列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "name,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   align:'center',
				   descs:"角色名称",
				   width:200
			   }      
			   ,{
				   name: 'descs',
				   align:'center',
				   descs:"角色描述",
				   width:260
			   }  
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'ok',
			    	 title:'确定',
			    	 handlers:{
			    		 click: function(){
			    			 var rows = grid.getSelectedRows();
			    			 if(rows.length==0){
			    				 layer.alert("请选择角色");
			    			 }else{
			    				 var roleId = $(rows[0]).attr('recordId');
				    			 var userId = $(parent.grid.getSelectedRows()[0]).attr('recordId');
				    			 DwrTool.mapUserRole(userId,roleId,function(ret){
				    				 layer.msg(ret,function(){
				    					 grid.reload();
				    					 parent.grid2.reload();
				    				 });
				    			 });
			    			 }
			    		 }
			    	 }
			     }, {
			    	 id:'close',
			    	 title:'取消',
			    	 handlers:{
			    		 click: function(){
			    			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			    			 parent.layer.close(index);
			    		 }
			    	 }
			     }
			     
			 ]}
		};
	var grid;
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		var userId = $(parent.grid.getSelectedRows()[0]).attr('recordId');
		grid.load({data:{r:'not in',name:'id',value:{poName:'UserRole',aidField:'roleId',data:{userId:userId}}}});
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rcon">
<div id="list"></div>
        </div>
</body>
</html>