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
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<SCRIPT type="text/javascript">
    <!--
    var data = {};
	var index = parent.layer.getFrameIndex(window.name);
    var closeWin = function(){
	    parent.layer.close(index);
    }
	var gridSetting = {
			id:'ticketSaleGrid',
			po:"TicketReserve",
			height:'auto',
			className:"mtable",
			noPaddingRight:true,
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "createTime,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{	name:'playDate',
					hide:true
			   },{	name:'showNumberId',
					hide:true
			   },{
				   name: 'descs',
				   descs:"备注信息",
				   align:'center',
				   width:120
			   } ,{
				   name: 'sallerCode',
				   descs:"营销编号",
				   align:'center',
				   width:100
			   }   
			    ,{
				   name: 'ticketNum',
				   descs:"预订数量",
				   type:'int',
				   align:'center'
			   }  
			]
		};
	var gridSetting2 = {
			id:'ticketGrid',
			po:"Ticket",
			title:"预订门票列表1",
			height:'auto',
			service:"uiServices!listSimple",
			noPaddingRight:true,
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "number,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'areaName',
				   descs:"片区",
				   align:'center',
				   width:100
			   },{
				   name: 'seatName',
				   descs:"座位",
				   align:'center',
				   width:100
			   } ,{
				   name: 'price',
				   descs:"票价",
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
			],
			toolbar:{align:'right',buttons:[
				   {
				         id:'add',
				         title:'出票',
				         handlers:{
				             click: function(){
				            	 var rows = grid2.getSelectedRows();
								 if(rows.length===0){
									 layer.msg("请选择要出票的记录");
								 }else{
									 var ok = true;
									 rows.each(function(i,tr){
										 if($(tr).find("td[name=status]").attr("value")!=="0203002"){
												ok = false;
												return;
										 }
									  })
									  if(ok){
										   layer.open({
									    		title:false,
											    type: 2,
											    skin: 'layui-layer-rim', //加上边框
											    area: ['1000px', '520px'], //宽高
											    content: ['<%=path%>/public/common/r2.htmls?page=sell/job/pay_reserve_win&all=2',"no"],
											    end:function(ret){
											    	if(parent.ticketArr!=null&&parent.ticketArr.length!=0){
											    		closeWin();
											    	}
											    }
											});
									   }else{
										   layer.msg("已售出的记录不能继续出票");
									   }
									  
								   }
				             }
				          }
				    }, {
				         id:'save',
				         title:'全部出票',
				         handlers:{
				             click: function(){
				            	 var rows = grid.getSelectedRows();
								   if(rows.length!=1){
									   layer.msg("请选择要出票的销售记录");
								   }else{
									   layer.open({
								    		title:false,
										    type: 2,
										    skin: 'layui-layer-rim', //加上边框
										    area: ['1000px', '520px'], //宽高
										    content: ['<%=path%>/public/common/r2.htmls?page=sell/job/pay_reserve_win&all=1',"no"],
										    end:function(ret){
										    	if(parent.ticketArr!=null&&parent.ticketArr.length!=0){
										    		closeWin();
										    	}
										    }
										});
								   }
				             }
				         }
				      }, {
				          id:'delete',
				          title:'退订',
				          handlers:{
				               click: function(){
								   var rows = grid2.getSelectedRows();
								   if(rows.length==0){
									   layer.msg("请选择要退订的记录");
								   }else{
									   var flag = true;
									   rows.each(function(i,v){
										   if($(v).find("td[name=status]").attr('value')!=="0203002"){
											   flag = false;
											   return false;
										   }
									   })
									   if(!flag){
										   layer.msg("只能退订状态为【预订】的记录");
									   }else{
										   layer.confirm("确定取消预订吗？",{btn:["确定","取消"]},function(index){
											   var arr = [];
											   rows.each(function(i,v){
												   arr.push($(v).attr("recordId"));
											   })
											   BaseDwr.unreserve(JSON.stringify(arr),function(ret){
												   layer.msg("退订成功");
												   grid2.reload(function(){
													   parent.setSeatStatus($(parent.document).find('#playDate').val());
													   layer.close(index);
													   if(ret==0){
															closeWin();   
													   }
												   });
											   });  
										   });
									   }
								   }
				            	  	 
				                }
				          }
				      }, {
				          id:'qtuiding',
				          title:'全部退订',
				          handlers:{
				               click: function(){
				            	   var rows = grid.getSelectedRows();
								   if(rows.length!=1){
									   layer.msg("请选择要退订的预订记录");
								   }else{
									   layer.confirm("确定全部退订吗？",{btn:["确定","取消"]},function(index){
										   BaseDwr.unreserveAll(rows.attr("recordId"),function(ret){
											   grid2.reload();
											   parent.setSeatStatus($(parent.document).find('#playDate').val());
											   layer.close(index);
											   closeWin();
										   });  
									   });
								   }
				            	   
				                }
				          }
				      },{
				          id:'close',
				          title:'关闭窗口',
				          handlers:{
				               click: function(){
								   closeWin();
				            	  	 
				                }
				          }
				      }
			]}	
		};
	var grid,grid2;
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		var data1 = [{
				showNumberId:$(parent.document).find("#showNumber").val()},
				{"id!!in":{pojo:'Ticket',fieldName:'reserveId',where:[{status:'0203002'}]}}];
		data1.push({name:"playDate",value:"today",r:">="});
		grid.load({data:data1});
    	grid2 = new GridView("list2",gridSetting2,"list2");
		grid2.init();
		grid.rowClick(function(id){
			grid2.load({data:{reserveId:id}});
		});
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rcon" style="height: 510px;">
<table style="width:100%" border="0" cellpadding="0" cellspacing="0">
	<tr valign="top">
	<td width="340"><div id="list"></div></td>
	<td><div id="list2" class="rcon" style="height:480px;overflow: auto;"></div></td>
	</tr>
</table>
</div>


<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>