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
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=<%=new java.util.Date().getTime() %>"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js?v=<%=new java.util.Date().getTime() %>"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0103']);
    genDicJs(['0302']);
	var gridSetting = {
			id:'TheaterGrid',
			po:"Theater",
			height:'auto',
			title:'内部编号只能是从1开始的连续正整数',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"剧场名称",
				   align:'center',
				   editable: true,
				   width:500
			   } ,{
				   name: 'sn',
				   descs:"剧场编号",
				   align:'center',
				   editable: true,
				   width:200
			   } ,{
				   name: 'stagePosition',
				   descs:"舞台位置",
				   align:'center',
				   editable: true,
				   type:'select',
				   width:200
			   }  ,{
				   name: 'remark',
				   descs:"备注",
				   align:'center',
				   editable: true
			   }    
			     
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'add',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var newRow = grid.newRow();
			    			 grid2.addExData(newRow,{belongto:belongto});
			    		 }
			    	 }
			     }, {
			    	 id:'save',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 grid.save("",'refreshAreaCache',function(ret) {
			    				if (ret == -1) {
			    					layer.alert("数据保存失败，请重试！");
			    				} else {
			    					layer.msg("数据已保存！");
			    					grid.reload(ret.id);
			    				}

			    			});
			    		 }
			    	 }
			     }
			     , {
			    	 id:'delete',
			    	 title:'删除',
			    	 handlers:{
			    		 click: function(){
			    			 grid.deletes(function(ret){
			    				 if(ret==-1){
			    					 layer.msg("删除失败，请重试！");
			    				 }else{
			    					 layer.msg("删除" + ret.recordNum + "条记录！",function(ret){
			    						 grid.reload();
			    					 });
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }
			 ]}
		};
	var gridSetting2 = {
			id:'floorGrid',
			po:"Floor",
			title:'楼层列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 20,//auto表示根据容器高度自动判断
				orderby : "orders"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"楼层名称",
				   align:'center',
				   editable: true,
				   width:150
			   }     
			   ,{
				   name: 'orders',
				   descs:"排序",
				   align:'center',
				   editable: true,
				   width:100
			   } ,{
				   name: 'defaults',
				   descs:"默认显示",
				   align:'center',
				   type:'select',
				   defaultValue:'0103002',
				   editable: true,
				   width:100
			   },{
				   name: 'remark',
				   descs:"备注",
				   align:'center',
				   editable: true,
				   width:100
			   }
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'add2',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var rows = grid.getSelectedRows();
			    			 if(rows.length==0){
			    				 layer.msg("请选择剧院");
			    			 }else{
			    				 var newRow = grid2.newRow();
			    				 grid2.addExData(newRow,{theaterId:$(rows[0]).attr('recordId')});
			    			 }
			    			 
			    		 }
			    	 }
			     }, {
			    	 id:'save2',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 grid2.save(function(ret) {
			    				if (ret == -1) {
			    					layer.alert("数据保存失败，请重试！");
			    				} else {
			    					layer.msg("数据已保存！");
			    					grid2.reload(ret.id);
			    				}

			    			});
			    		 }
			    	 }
			     }
			     , {
			    	 id:'delete2',
			    	 title:'删除',
			    	 handlers:{
			    		 click: function(){
			    			 grid2.deletes(function(ret){
			    				 if(ret==-1){
			    					 layer.msg("删除失败，请重试！");
			    				 }else{
			    					 layer.msg("删除" + ret.recordNum + "条记录！",function(ret){
			    						 grid2.reload();
			    					 });
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }
			 ]}
		};
	var grid,grid2;
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
    	grid2 = new GridView("list2",gridSetting2,"list2");
		grid2.init();
		grid.rowClick(function(id){
			grid2.load({data:{theaterId: id}});
			ajustHeight2();
		});
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rtit">剧院设置</div>
<div id="list" class="rbox1"></div>
<div id="list2" class="rbox2"></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>
