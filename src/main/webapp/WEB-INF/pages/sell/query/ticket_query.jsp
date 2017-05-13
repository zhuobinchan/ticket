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
    var userMap = genMaps("from User where id in(select userId from UserRole where roleName='saller') order by name,id");
    var formSetting = {
    		po:'TicketSale',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"售票员",type:"select",name:"createUserId",map:userMap},{title:"售票日期",type:"datescale",name:"createTime"},
    	  {title:"演出日期",type:"datescale",name:"playDate"}
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
		                 			    			 form.reset();
		                 			    		 }
		                 			    	 }
		                 			     }/* , {
		                 			    	 id:'reset',
		                 			    	 title:'打印',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			print();
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'reset',
		                 			    	 title:'导出',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			 form.reset();
		                 			    		 }
		                 			    	 }
		                 			     } */
		                 			 ]}	
    }
	var gridSetting = {
			id:'ticketGrid',
			po:"Ticket",
			title:'门票列表',
			height:'auto',
			fixWidth : true,
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
				   descs:"二维码",
				   align:'center',
				   width:120
			   },{
				   name: 'createUserId',
				   descs:"售票员",
				   type:"select",
				   align:'center',
				   width:90
			   },{
				   name: 'createTime',
				   descs:"售票时间",
				   align:'center',
				   width:160
			   },{
				   name: 'price',
				   descs:"票价",
				   align:'center',
				   width:100
			   } ,{
				   name: 'realPrice',
				   descs:"实际价格",
				   align:'center',
				   width:100
			   } ,{
				   name: 'areaName',
				   descs:"片区",
				   type:'text',
				   align:'center',
				   width:150
			   } ,{
				   name: 'totalNum',
				   descs:"总人数",
				   align:'center',
				   width:80
			   } ,{
				   name: 'allowNum',
				   descs:"剩余人数",
				   align:'center',
				   width:80
			   }  
			   ,{
				   name: 'status',
				   descs:"状态",
				   type:'select',
				   align:'center',
				   width:70
			   } ,{
				   name: 'strategyName',
				   descs:"售票方式",
				   align:'center',
				   formatter:"getStrategyNameByTicketId",
				   width:100
			   } ,{
				   name: 'seatName',
				   descs:"座位",
				   align:'center',
				   width:100
			   }  ,{
				   name: 'playDate',
				   descs:"演出日期",
				   align:'center',
				   width:90
			   }
			]
		};
    var roleString = '${sessionScope.roleString}';
	var userId = '${sessionScope.userInfo.id}';
    function queryData(e,queryForm){
    	var param = [{name:"saleId",r:"!=",value:""}];
    	var playDate = queryForm.find(":input[name=playDate]:eq(0)").val();
    	var playDate2 = queryForm.find(":input[name=playDate]:eq(1)").val();
    	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
    	var createUserId = queryForm.find(":input[name=createUserId]").val();
    	if(playDate===''&&createTime===""){
    		layer.msg("请限定售票日期或者演出日期")
    		return;
    	}
    	if(roleString.indexOf("financial")==-1&&roleString.indexOf("saller_charge")==-1){
    		param.push({createUserId: userId});
    	}else{
    		var createUserId = queryForm.find(":input[name=createUserId]").val();
    		if(createUserId!=''){
        		param.push({createUserId:createUserId});
        	} 
    	}
    	if(createTime!=''){
    		param.push({r:"between",name:'createTime',value:[createTime,createTime2]});
    	}
    	grid.load({data: param});
    	ajustHeight();
    }
	var grid,grid2,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		queryData(event,$('#formview form'));
		$('#list').width(top.rightWidth);
		
    });
    //-->
</SCRIPT>
<style type="text/css">
#list td,th{white-space: nowrap;}
	#list td,th{white-space: nowrap;}
</style>
</head>

<body>
<div class="mainline"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox1" style="overflow:scroll;width:1030px;border:0px solid"></div>
<div class="rbox2">
<div class="rcon">
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
</div>

</div>

<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>