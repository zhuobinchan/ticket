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
    		po:'Recharge',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"",type:"",name:""},{title:"充值日期",type:"datescale",name:"createTime"},{title:"",type:"",name:""}]
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
		                 			    			 form.reset();
		                 			    		 }
		                 			    	 }
		                 			     }
		                 			 ]}	
    }
	var gridSetting = {
			id:'rechargeGrid',
			po:"Recharge",
		//	title:'充值记录',
			height:'auto',
			className:"mtable",
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "createTime desc,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'createTime',
				   descs:"充值时间",
				   align:'center',
				   width:150
			   } ,{
				   name: 'rechargeAmount',
				   descs:"充值金额",
				   align:'center',
				   width:100
			   } ,{
				   name: 'createUserName',
				   descs:"经办人",
				   type:"text",
				   align:'center',
				   width:100
			   } 
			]
		};
    function queryData(e,queryForm){
    	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
    	var data = [{customerId:'${param.id}'}];
    	data.push({r:"between",name:'createTime',value:[createTime,createTime2]});
    	grid.load({data:data});
    }
	var grid,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		queryData(event,$('#formview form'));
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox1"></div>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
</body>
</html>