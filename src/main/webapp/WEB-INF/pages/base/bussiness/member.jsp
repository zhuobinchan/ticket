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
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0202','0206']);
    var userMap = genMaps("from User order by name,id");
    var memberMap = genMaps("from Salesman order by code,id");
    var formSetting = {
    		po:'Member',
    		title:"会员详情",
    		hiddens:["id","balance","useNum"],
    	fields:[
    	     [
	         {title:"会员卡号",type:"text",name:"code"}, {title:"会员名称",type:"text",name:"name"},{title:"手机号码",type:"text",name:"mobileno"}
	     	]
    	,[{title:"记录状态",type:"select",dic:"0202",name:"status"}, {title:"营销人员",type:"autocomplete",name:"salesmanId",map:memberMap},
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
		                 			    			var newRow = grid.newRow();
                 			    					setFormDefault(newRow);
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'save',
		                 			    	 title:'保存',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 if(!form.getValue("code")){
		                 			    				 layer.msg("会员卡号不能为空");return;
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
	var gridSetting = {
			id:'customerGrid',
			po:"Member",
			title:'会员列表',
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
				   name: 'code',
				   descs:"会员卡号",
				   align:'center',
				   width:50
			   },{
				   name: 'name',
				   descs:"会员名称",
				   align:'center',
				   width:110
			   },{
				   name: 'mobileno',
				   descs:"手机号码",
				   align:'center',
				   width:80
			   }  ,{
				   name: 'salesmanId',
				   descs:"营销人员",
				   type:'select',
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
    function setFormDefault(newRow){
    	form.reset();
		grid.setCellHtml(newRow,"status","0202001");
		var now = getCurrentTime("yyyy-MM-dd HH:mm:ss");
		grid.setCellHtml(newRow,"createTime",now);
		form.setValue("createTime",now);
		form.setValue("status","0202001");
    }
	var grid,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		grid.rowClick(function(id){
			if(id){
				form.loadData(id, "Member");
			}else{
				setFormDefault($(this));
			}
		});
		ajustHeight();
		$.ajax({
        	url:"${pageContext.request.contextPath}/public/tool/getSalesmanArr.htmls",
        	type:"POST",
        	dataType:"json",
        	success:function(ret){
        		$(":input[name=salesmanId_txt]").autocomplete({
        		      minLength: 0,
        		      source: ret,
        		      select: function( event, ui ) {
        		    	  $(":input[name=salesmanId_txt]").val( ui.item.value+ui.item.name );
        		    	  $(":input[name=salesmanId]").val( ui.item.id );
        		        return false;
        		      }
        		})
        	}
        })
    });
    //-->
</SCRIPT>
</head>

<body>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox2"></div>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<link href="<%=path %>/static/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
</body>
</html>