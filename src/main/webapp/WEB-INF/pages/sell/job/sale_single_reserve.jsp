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
			title:"预订门票列表",
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
			   },{	name:'areaId',
					hide:true
			   },{
				   name: 'areaName',
				   descs:"片区",
				   align:'center',
				   width:210
			   },{
				   name: 'seatName',
				   descs:"座位",
				   align:'center',
				   width:200
			   } ,{
				   name: 'price',
				   descs:"票价",
				   align:'center',
				   width:160
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
											    area: ['900px', '520px'], //宽高
											    content: ['<%=path%>/public/common/r2.htmls?page=sell/job/pay_reserve_win&all=2',"no"],
											    end:function(ret){
											    	if(parent.ticketArr!=null){
											    		parent.printTicket(parent.ticketArr,function(){
											    			var arr = [];
											    			rows.each(function(i,v){
											    				var j = $.parseJSON($(v).attr('hidevalues'));
											    				arr.push(j.areaId);
											    			})
								    		    			var areaIds = _.uniq(arr);
								    						parent.synccolor(parent.$('#playDate').val(),areaIds); 
								    		    		});
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
				            	 layer.open({
							    		title:false,
									    type: 2,
									    skin: 'layui-layer-rim', //加上边框
									    area: ['900px', '520px'], //宽高
									    content: ['<%=path%>/public/common/r2.htmls?page=sell/job/pay_reserve_win&all=1',"no"],
									    end:function(ret){
									    	if(parent.ticketArr!=null){
									    		parent.printTicket(parent.ticketArr,function(){
										    		var arr = [];
										    		rows = grid2.getAllRows();
										    		rows.each(function(i,v){
										    			var j = $.parseJSON($(v).attr('hidevalues'));
										    			arr.push(j.areaId);
										    		})
							    		    		var areaIds = _.uniq(arr);
							    					parent.synccolor(parent.$('#playDate').val(),areaIds); 
						    		    		});
									    		closeWin();
									    	}
									    }
									});
				             }
				         }
				      }, {
				          id:'delete',
				          title:'退订',
				          handlers:{
				               click: function(){
								   var rows = grid2.getSelectedRows();
								   if(rows.length==0){
									   layer.msg("请选择要取消预订的记录");
								   }else{
									   if(rows.find("td[name=status]").attr('value')!=="0203002"){
										   layer.msg("只能退订状态为【预订】的记录");
									   }else{
										   layer.confirm("确定取消预订吗？",{btn:["确定","取消"]},function(index){
											   var arr = [];
											   rows.each(function(i,v){
												   arr.push($(v).attr("recordId"));
											   })
											   BaseDwr.unreserve(arr,function(ret){
												   layer.msg("退订成功");
												   afterChange(rows);
												   grid2.reload(function(){
													   
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
				            	   layer.confirm("确定全部退订吗？",{btn:["确定","取消"]},function(index){
									   BaseDwr.unreserveAll(reserveId,function(ret){
										   afterChange(grid2.getAllRows());
										   grid2.reload();
										   layer.close(index);
										   closeWin();
									   });  
								   });
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
    var reserveId = ""; 
    $(function(){
    	var table = $('#itemTable');
    	$.ajax({
    		url:'<%=path%>/public/tool/getTicketReserveByTicketId.htmls',
    		type:"POST",
    		dataType:"json",
    		async:false,
    		data:{id:'${param.ticketId}'},
    		success:function(ret){
    			reserveId = ret.id;
    			table.append('<tr name="itemtr"><td align="center">'+(ret.descs===null?"":ret.descs)+
    					'</td><td style="text-align:center" name="sallerCode">'+(ret.sallerCode===null?"":ret.sallerCode)
        		+'</td><td style="text-align:center" name="memberNo">'+(ret.memberNo===null?"":ret.memberNo)+'</td><td style="text-align:center" name="size">'+ret.ticketNum+'</td><td style="text-align:center">'
        		+ret.createUserName+'</td><td style="text-align:center" name="realPrice">'+new Date(ret.createTime).format("HH:mm:ss")+
        		'</td></tr>');
    		}
    	})
    	grid2 = new GridView("list",gridSetting2,"list");
		grid2.init();
		grid2.load({data:{reserveId:reserveId}});
    })
   function afterChange(rows){
    	var arr = [];
		rows.each(function(i,v){
			var j = $.parseJSON($(v).attr('hidevalues'));
			arr.push(j.areaId);
		})
		var areaIds = _.uniq(arr);
		parent.synccolor(parent.$('#playDate').val(),areaIds); 
    }
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
<div id="content" style="height: 90%">
<div class="container-fluid" style="padding-left: 20;">
<div class="widget-box" style="width:97%;padding: 20px">
          <table class="table table-bordered table-striped" id="itemTable">
              <thead>
                <tr>
                  	<th width="300">备注信息</th>
					<th width="100">营销编号</th>
					<th width="100">会员卡号</th>
					<th width="100">预订票数</th>
					<th width="100">售票员</th>
					<th width="100">预订时间</th>
                </tr>
              </thead>
            </table>
        </div>
<div class="widget-box" id="list" style="width:97%;padding: 20px"> </div>
</div>
</div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script src="<%=path %>/static/special/js/jquery.min.js"></script> 
<script src="<%=path %>/static/special/js/bootstrap.min.js"></script> 
<script src="<%=path %>/static/special/js/jquery.uniform.js"></script> 
</body>
</html>
