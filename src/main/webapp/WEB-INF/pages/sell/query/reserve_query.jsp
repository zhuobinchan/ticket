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
    var showNumberMap = genMaps("from ShowNumber order by orderno,id");
    var formSetting = {
    		po:'TicketSale',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"演出场次",type:"select",name:"showNumberId",map:showNumberMap},
    	  {title:"售票员",type:"pop",name:"createUserId",map:userMap},
    	  {title:"售票日期",type:"datescale",name:"createTime"}]
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
			id:'ticketGrid',
			po:"Ticket",
			title:'门票列表',
			height:'auto',
			className:"mtable",
			service:"uiServices!listSimple",
			pageInfo: {
				records: 20,//auto表示根据容器高度自动判断
				orderby : "number,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'number',
				   descs:"票号",
				   align:'center',
				   width:150
			   },{
				   name: 'showNumberId',
				   descs:"演出场次",
				   type:'select',
				   align:'center',
				   width:150
			   } ,{
				   name: 'price',
				   descs:"票价",
				   align:'center',
				   width:100
			   } ,{
				   name: 'realPrice',
				   descs:"实际价格",
				   align:'center',
				   width:100
			   }  ,{
				   name: 'seatName',
				   descs:"座位",
				   align:'center',
				   width:100
			   }  
			   ,{
				   name: 'status',
				   descs:"状态",
				   type:'select',
				   align:'center',
				   width:70
			   }  
			]
		};
    function queryData(e,queryForm){
    	var showNumberId = queryForm.find(":input[name=showNumberId]").val();
    	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
    	var createUserId = queryForm.find(":input[name=createUserId]").val();
    	var data = [{name:"reserveId",r:"!=",value:""}];
    	if(showNumberId!=''){
    		data.push({showNumberId:showNumberId});
    	}
    	data.push({r:"between",name:'createTime',value:[createTime,createTime2]});
    	if(createUserId!=''){
    		data.push({createUserId:createUserId});
    	} 
    	grid.load({data:data});
    }
	var grid,grid2,form;
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
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>