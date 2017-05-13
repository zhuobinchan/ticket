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
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/uniform.css" />
<link rel="stylesheet" href="<%=path %>/static/special/css/matrix-style.css" />
<script type='text/javascript' src='<%=path %>/static/js/date.js'></script>
<SCRIPT type="text/javascript">
    <!--
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = genMaps("from Area order by code");
    var index = parent.layer.getFrameIndex(window.name);
    var playDate = $(parent.document).find('#playDate').val();
    var closeWin = function(){
	    parent.layer.close(index);
    }
    var grid2
	var gridSetting2 = {
			id:'ticketGrid',
			po:"Ticket",
			title:"门票列表",
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
				   width:200
			   },{
				   name: 'seatName',
				   descs:"座位",
				   align:'center',
				   width:120
			   } ,{
				   name: 'strategyName',
				   descs:"门票策略",
				   align:'center',
				   width:130
			   },{
				   name: 'price',
				   descs:"票价",
				   align:'center',
				   width:100
			   },{
				   name: 'realPrice',
				   descs:"实际票价",
				   align:'center',
				   width:100
			   }   
			   ,{
				   name: 'status',
				   descs:"状态",
				   type:'select',
				   align:'center'
			   }  
			],
			toolbar:{align:'right',buttons:[
				  {
				          id:'delete',
				          title:'改签',
				          handlers:{
				               click: function(){
								   var rows = grid2.getSelectedRows();
								   if(rows.length==0){
									   layer.msg("请选择要改签的记录");
								   }else{
									   if(rows.find("td[name=status]").attr('value')!=="0203001"){
										   layer.msg("只能改签状态为【售出】的记录");
									   }else{
										   layer.confirm("确定改签吗？",{btn:["确定","取消"]},function(index){
											   var arr = [];
											   rows.each(function(i,v){
												   arr.push($(v).attr("recordId"));
											   })
											   BaseDwr.changeDate(arr,function(areaIds){
												   layer.msg("改签成功");
												   grid2.reload(function(){
													   parent.synccolor(playDate,areaIds);
													   layer.close(index);
												   });
											   });  
										   });
									   }
								   }
				            	  	 
				                }
				          }
				      }, {
				          id:'qtuiding',
				          title:'全部改签',
				          handlers:{
				               click: function(){
				            	   layer.confirm("确定全部改签吗？",{btn:["确定","取消"]},function(index){
									   BaseDwr.changeDateAll(saleId,function(areaIds){
										   parent.synccolor(playDate,areaIds);
										   layer.close(index);
										   closeWin();
									   });  
								   });
				                }
				          }
				      },{
				          id:'back',
				          title:'退票',
				          handlers:{
				               click: function(){
								   var rows = grid2.getSelectedRows();
								   if(rows.length==0){
									   layer.msg("请选择要退票的记录");
								   }else{
									   if(rows.find("td[name=status]").attr('value')!=="0203001"){
										   layer.msg("只能退掉状态为【售出】的记录");
									   }else{
										   layer.confirm("确定退票吗？",{btn:["确定","取消"]},function(index){
											   var arr = [];
											   rows.each(function(i,v){
												   arr.push($(v).attr("recordId"));
											   })
											   BaseDwr.cancelTickets(arr,function(areaIds){
												   layer.msg("退票成功");
												   grid2.reload(function(){
													   parent.synccolor(playDate,areaIds);
													   layer.close(index);
													  /*  if(ret==0){
															closeWin();   
													   } */
												   });
											   });  
										   });
									   }
								   }
				            	  	 
				                }
				          }
				      }, {
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
    var saleId = ""; 
    $(function(){
    	var table = $('#itemTable');
    	$.ajax({
    		url:'<%=path%>/public/tool/getTicketSaleByTicketId.htmls',
    		type:"POST",
    		dataType:"json",
    		async:false,
    		data:{id:'${param.ticketId}'},
    		success:function(ret){
    			saleId = ret.id;
    			table.append('<tr name="itemtr"><td align="center">'+ret.saleNo+'</td><td align="center">'+(ret.descs===null?"":ret.descs)+
    					'</td><td style="text-align:center" name="sallerCode">'+(ret.sallerCode===null?"":ret.sallerCode)
        		+'</td><td align="center" name="memberNo">'+(ret.memberNo)+'</td><td style="text-align:center" name="size">'+ret.ticketNum+'</td><td style="text-align:center" width="70">'
        		+ret.createUserName+'</td><td style="text-align:center" name="createTime">'+new Date(ret.createTime).format('yyyy-MM-dd HH:mm')+'</td></tr>');
    		}
    	})
    	
    	grid2 = new GridView("list",gridSetting2,"list");
		grid2.init();
		grid2.load({data:{saleId: saleId}});
    })
   
    //-->
</SCRIPT>
<style type="text/css">
.widget-content td{
	text-align: center;
}
table#bussiness td{
	padding: 0;
}
.span11{
	padding: 0;margin: 2px!important;
}
</style>
</head>

<body style="margin:0;padding: 0">
<div id="content">
<div class="container-fluid" style="padding: 0;">
  <div class="row-fluid" style="margin: 0">
<div class="widget-box" style="width:97%;padding: 10px">
            <table class="table table-bordered table-striped" id="itemTable">
              <thead>
                <tr>
                  	<th width="100">销售单号</th>
                  	<th width="200">备注信息</th>
					<th width="100">营销编号</th>
					<th width="100">会员卡号</th>
					<th width="90">售票票数</th>
					<th width="90">售票员</th>
					<th>售票时间</th>
                </tr>
              </thead>
            </table>
        </div>
<div class="widget-box" id="list" style="width:97%;padding:10px"> </div>
  </div>
</div></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script src="<%=path %>/static/special/js/jquery.min.js"></script> 
<script src="<%=path %>/static/special/js/bootstrap.min.js"></script> 
<script src="<%=path %>/static/special/js/jquery.uniform.js"></script> 
</body>
</html>
