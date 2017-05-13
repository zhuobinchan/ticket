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
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0204']);
    var userMap = genMaps("from User order by name,id");
    var formSetting = {
    		po:'Play',
    		title:"演出剧目",
    		hiddens:["id"],
    	fields:[[{
    	   title:"剧目名称",type:"text",name:"name",colspan:5
    	}],[{title:"演出日期",type:"date",name:"playDate"},{title:"开演时",type:"int",name:"hh"},{title:"开演分",type:"int",name:"mi"}]
    	,[{title:"剧目状态",type:"select",dic:"0204",name:"status"},
    	  {title:"创建人",type:"popText",name:"createUserId",map:userMap},
    	  {title:"创建时间",type:"text",name:"createTime"}]
    	],
		toolbar:{align:'right',buttons:[
		                 			   {
		                 			    	 id:'add',
		                 			    	 title:'增加',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 var name = '${sessionScope.userInfo.name}';
		                 			    			 var userId = '${sessionScope.userInfo.id}';
		                 			    			 var newRow = grid.newRow();
		                 			    			 grid.setCellHtml(newRow,"status","0204001");
		                 			    			 grid.setCellHtml(newRow,"createUserName",name);
		                 			    			 var now = getCurrentTime("yyyy-MM-dd HH:mm:ss");
		                 			    			 grid.setCellHtml(newRow,"createTime",now);
		                 			    			 form.setValue("createUserId",userId);
		                 			    			 form.setValue("createTime",now);
		                 			    			 form.setValue("status","0204001");
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'save',
		                 			    	 title:'保存',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 form.save(function(ret) {
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
		                 			    			 grid.deletes("deleteMappedRoleNavis",function(ret){
		                 			    				 if(ret==-1){
		                 			    					 layer.msg("删除失败，请重试！");
		                 			    				 }else{
		                 			    					 layer.msg("删除" + ret.recordNum + "条记录！",function(ret){
		                 			    						 grid.reload();
		                 			    						 DwrTool.getRoleNaviList(-1,function(zNodes){
		                 			    							$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		                 			    						});
		                 			    					 });
		                 			    				 }
		                 			    			 });
		                 			    			 
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			 ]}	
    }
	var gridSetting = {
			id:'playGrid',
			po:"Play",
			title:'演出剧目列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 12,//auto表示根据容器高度自动判断
				orderby : "createTime,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"剧目名称",
				   align:'center',
				   width:150
			   }      
			   ,{
				   name: 'playDate',
				   descs:"演出日期",
				   align:'center',
				   width:70
			   } ,{
				   name: 'status',
				   descs:"记录状态",
				   type:'select',
				   align:'center',
				   width:70
			   }  ,{
				   name: 'createUserId',
				   descs:"创建人",
				   type:"select",
				   align:'center',
				   width:70
			   }   
			]
		};
	var grid,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		grid.rowClick(function(id){
			form.loadData(id, "Play")
		});
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox2"></div>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>