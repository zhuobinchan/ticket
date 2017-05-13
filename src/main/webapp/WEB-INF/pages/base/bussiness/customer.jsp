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
    genDicJs(['0202','0206']);
    var userMap = genMaps("from User order by name,id");
    var formSetting0 = {
    		po:'Customer',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"客户名称",type:"text",name:"name"},{title:"客户编号",type:"text",name:"code"}
    	  ]
    	],
    	queryRow:{align:'center',buttons:[
		                 			   {
		                 			    	 id:'query',
		                 			    	 title:'查询',
		                 			    	 handlers:{
		                 			    		 click: function(e,queryForm){
		                 			    			 queryData(e,queryForm);
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'reset',
		                 			    	 title:'重置',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 form0.reset();
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			 ]}	
    }
    var formSetting = {
    		po:'Customer',
    		title:"客户资料详情",
    		hiddens:["id","balance","useNum"],
    	fields:[
    	     [
				{title:"客户名称",type:"text",name:"name",colspan:5,width:"97%"}
    		 ],[
	         {title:"客户编号",type:"text",name:"code"}, {title:"客户简称",type:"text",name:"shortname"}, 
	         {title:"字母检索",type:"text",name:"spell"}
	     	],
    	[{title:"立减金额",type:"text",name:"cheapPrice"},{title:"手机号码",type:"text",name:"mobileno"},{title:"座机号码",type:"text",name:"phone"}]
    	,[{title:"记录状态",type:"select",dic:"0202",name:"status"}, {title:"客户类型",type:"select",name:"type",dic:"0206"},
    	   {title:"创建时间",type:"text",name:"createTime"}],
    	   [
	         {title:"联系地址",type:"text",name:"address",colspan:5,width:"97%"}
	]
    	],
		toolbar:{align:'right',buttons:[
		                 			   {
		                 			    	 id:'add',
		                 			    	 title:'增加',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			if($('tr[name=newrow]').length===0){
		                 			    				var newRow = grid.newRow();
			                 			    			form.reset();
	                 			    					form.setValue("cheapPrice",0);
	                 			    					form.setValue("balance",0);
	                 			    					form.setValue("status","0202001");
	                 			    					form.setValue("useNum",0);
	                 			    					form.setValue("createTime",getCurrentTime("yyyy-MM-dd HH:mm:ss"));
	                 			    					grid.setCellHtml(newRow,"status","0202001");
			                 			    			var now = getCurrentTime("yyyy-MM-dd HH:mm:ss");
			                 			    			grid.setCellHtml(newRow,"createTime",now);
			                 			    			form.setValue("createTime",now);
			                 			    			form.setValue("status","0202001");
		                 			    			}
		                 			    			 
		                 			    			<%--  $.ajax({
		                 			    				 url:'<%=path%>/public/tool/genCustomerCode.htmls',
		                 			    				 success:function(ret){
		                 			    					form.reset();
		                 			    					grid.setCellHtml(newRow,"code",ret);
		                 			    					form.setValue("code",ret);
		                 			    					form.setValue("cheapPrice",0);
		                 			    					form.setValue("balance",0);
		                 			    					form.setValue("status","0202001");
		                 			    					form.setValue("useNum",0);
		                 			    					form.setValue("createTime",getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		                 			    				 }
		                 			    			 }) --%>
		                 			    			
		                 			    			
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'save',
		                 			    	 title:'保存',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			if(grid.getSelectedRows().length===0&&$('#list tr[name=newrow]').length!==1){
	                 			    					layer.msg("请选择或者新增记录");
	                 			    					return ;
	                 			    				}
		                 			    			 if(!form.getValue("code")){
		                 			    				 layer.msg("客户编号不能为空");return;
		                 			    			 }
		                 			    			 form.save(function(ret) {
		                 			    				if (ret == -1) {
		                 			    					layer.alert("数据保存失败，请重试！");
		                 			    				} else {
		                 			    					layer.msg("数据已保存！");
		                 			    					grid.reload(ret.id);
		                 			    					ajustHeight();
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
    function queryData(e,queryForm){
    	var code = queryForm.find(":input[name=code]").val();
    	var name = queryForm.find(":input[name=name]").val();
    	var data = [];
    	if(code!==""){
    		data.push({r:"like",name:'code',value:code+"%"});
    	}
    	if(name!==""){
    		data.push({or:[{r:"like",name:'name',value:name+"%"},{r:"like",name:'shortname',value:name+"%"}]});
    	}
    	grid.load({data:data});
    	ajustHeight();
    }
	var gridSetting = {
			id:'customerGrid',
			po:"Customer",
			title:'客户资料列表',
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
				   name: 'spell',
				   descs:"字母检索",
				   align:'center',
				   width:50
			   },{
				   name: 'code',
				   descs:"客户编号",
				   align:'center',
				   width:70
			   },{
				   name: 'shortname',
				   descs:"客户简称",
				   align:'center',
				   width:150
			   },{
				   name: 'mobileno',
				   descs:"手机号码",
				   align:'center',
				   width:80
			   }  ,{
				   name: 'phone',
				   descs:"座机号码",
				   align:'center',
				   width:80
			   }     
			  ,{
				   name: 'status',
				   descs:"记录状态",
				   type:'select',
				   align:'center',
				   width:50
			   }  
			]
		};
	var grid,form,form0;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	form0 = new FormView("formview0",formSetting0);
    	form0.init();
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		grid.rowClick(function(id){
			form.loadData(id, "Customer");
		});
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div id="formview0" class="rbox1"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox2"></div>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>