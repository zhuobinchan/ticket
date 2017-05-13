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
<style type="text/css">
	#list td,th{white-space: nowrap;padding-left: 5px;padding-right: 5px}
	#list2 td,th{white-space: nowrap;}
</style>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0204']);
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
		                 			     }
		                 			 ]}	
    }
	var gridSetting = {
			id:'ticketSaleGrid',
			po:"TicketSale",
			title:'销售记录',
			fixWidth : true,
			height:'auto',
			className:"mtable",
			service:"uiServices!listSimple",
			pageInfo: {
				records: 11,//auto表示根据容器高度自动判断
				orderby : "createTime desc,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'saleNo',
				   descs:"销售单号",
				   align:'center',
				   width:110
			   } ,{
				   name: 'createTime',
				   descs:"售票时间",
				   align:'center',
				   width:180
			   } ,{
				   name: 'playDate',
				   descs:"演出日期",
				   align:'center',
				   width:110
			   },{
				   name: 'createUserId',
				   descs:"售票人",
				   type:'select',
				   align:'center',
				   width:80
			   } ,{
				   name: 'payType',
				   descs:"支付方式",
				   type:"select",
				   align:'center',
				   width:80
			   }  ,{
				   name: 'strategyName',
				   descs:"门票策略",
				   align:'center',
				   width:100
			   }  
			    ,{
				   name: 'ticketNum',
				   descs:"售票数量",
				   type:'int',
				   align:'center',
				   width:70
			   },{
				   name: 'cheapPrice',
				   descs:"每张立减",
				   type:'int',
				   align:'center',
				   width:70
			   },{
				   name: 'offsetPrice',
				   descs:"原票抵扣",
				   type:'int',
				   align:'center',
				   width:70
			   },{
				   name: 'discount',
				   descs:"折扣比例",
				   type:'int',
				   align:'center',
				   width:70
			   } ,{
				   name: 'showAmount',
				   descs:"票面总额",
				   align:'center',
				   formatter:"sumSaleShowAmount",
				   width:100
			   },{
				   name: 'realAmount',
				   descs:"实收总额",
				   align:'center',
				   formatter:"sumSaleRealAmount",
				   width:100
			   } ,{
				   name: 'sallerCode',
				   descs:"营销编号",
				   width:120,
				   align:'center'
			   } ,{
				   name: 'memberNo',
				   descs:"会员卡号",
				   align:'center',
				   width:120
			   }     ,{
				   name: 'printNum',
				   descs:"用票数",
				   align:'center',
				   width:60
			   }   
			]
		};
	var gridSetting2 = {
			id:'ticketGrid',
			po:"Ticket",
			title:'门票列表',
			height:'auto',
			fixWidth : true,
			className:"mtable",
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
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
				   width:200
			   }  ,{
				   name: 'seatName',
				   descs:"座位",
				   align:'center',
				   width:100
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
				   width:100
			   } 
			]
		};
    var roleString = '${sessionScope.roleString}';
	var userId = '${sessionScope.userInfo.id}';
	var belongto = '${sessionScope.userInfo.theaterSn}';

    function queryData(e,queryForm){
    	var playDate = queryForm.find(":input[name=playDate]:eq(0)").val();
    	var playDate2 = queryForm.find(":input[name=playDate]:eq(1)").val();
    	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
    	var createUserId = queryForm.find(":input[name=createUserId]").val();
    	if(playDate===''&&createTime===""){
    		layer.msg("请限定售票日期或者演出日期")
    		return;
    	}
    	var data = [];
    	if(belongto){
    		data.push({theaterSn: belongto});
    	}
    	if(roleString.indexOf("financial")==-1&&roleString.indexOf("saller_charge")==-1){
    		data.push({createUserId: userId});
    	}else{
    		var createUserId = queryForm.find(":input[name=createUserId]").val();
    		if(createUserId!=''){
        		data.push({createUserId: createUserId});
        	} 
    	}
    	if(createTime2!==""){
    		data.push({r:"between",name:'createTime',value:[createTime,createTime2]});
    	}
    	if(playDate2!==""){
    		data.push({r:"between",name:'playDate',value:[playDate,playDate2]});
    	}
    	grid.load({data:data});
    	grid2.removeAll();
    	ajustHeight();
    }
    function pickedFunc(){
    	$dp.$('formview_playDate_2').value=$dp.$('formview_playDate').value;
    }
	var grid,grid2,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
    	grid2 = new GridView("list2",gridSetting2,"list2");
		grid2.init();
		grid.rowClick(function(id){
			grid2.load({data:{saleId:id}});
			ajustHeight();
		});
		queryData(event,$('#formview form'));
		$('#list').width(top.rightWidth);
		$('#formview_playDate').attr("onclick","WdatePicker({onpicked:pickedFunc})")
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox1" style="overflow:scroll;width:1050px;border:0px solid"></div>
<div id="list2" class="rbox2"></div>
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