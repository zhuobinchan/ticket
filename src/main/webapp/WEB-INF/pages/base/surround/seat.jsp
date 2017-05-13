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
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<style type="text/css">
.row{
	background-color: #836FFF;color: white;font-weight: bold;
}
.cell{
	background-color: #CDBE70;color: white;font-weight: bold;
}
.all{
	background-color: #8B8682;;color: white;font-weight: bold;
}
</style>
<SCRIPT type="text/javascript">
    <!--
	var gridSetting = {
			id:'areaGrid',
			po:"Area",
			title:'区域选择',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "name,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'code',
				   descs:"区域编号",
				   align:'center',
				   width:70
			   },{
				   name: 'name',
				   descs:"区域名称",
				   align:'center',
				   width:150
			   }      
			   ,{
				   name: 'totalRowNum',
				   descs:"行数",
				   align:'center',
				   width:70
			   } ,{
				   name: 'totalColumnNum',
				   descs:"列数",
				   align:'center',
				   width:70
			   },{
				   name: 'fromRowNum',
				   descs:"起始行",
				   align:'center',
				   editable: true,
				   width:70
			   } ,{
				   name: 'fromColumnNum',
				   descs:"起始列",
				   align:'center',
				   editable: true,
				   width:70
			   } ,{
				   name: 'price',
				   descs:"价格",
				   align:'center',
				   editable: true,
				   width:70
			   }    
			]
		};
	var drawSeatGraph=function(list){
		var maxRow = _.max(list,function(seat){
			return seat.rowNum;
		});
		var maxCell = _.max(list,function(seat){
			return seat.columnNum;
		});
		var minRow = _.min(list,function(seat){
			return seat.rowNum;
		});
		var minCell = _.min(list,function(seat){
			return seat.columnNum;
		});
		var seatTable = $('#seats').html("");
		seatTable.append($("<tr><td class='all' align='center' id='_all'></td></tr>"));
		$('#_all').html(maxRow.rowNum+"行"+maxCell.columnNum+"列");
		for(var i=minRow.rowNum;i<=maxRow.rowNum;i++){
			var seat = _.find(list,function(r){
				return r.rowNum == i;
			});
			var tr = $("<tr>").append($("<td class='row' align='center'>").html("行"+i)).appendTo(seatTable);
			for(var j=minCell.columnNum;j<=maxCell.columnNum;j++){
				var seat2 = _.find(list,function(r){
					return r.rowNum == i&&r.columnNum==j;
				});
				tr.append($("<td align='center'>").html(seat2.name));
			}
		}
		var tr = $("#seats tr:first");
		for(var i=minCell.columnNum;i<=maxCell.columnNum;i++){
			$("<td class='cell' align='center'>").html("列"+i).appendTo(tr);
		}
	}
	var belongto = '${param.belongto}';
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load({data:{belongto:belongto}});
		grid.rowClick(function(id){
			BaseDwr.queryAreaSeats(id,function(list){
				drawSeatGraph(list);
				ajustHeight();
			});
		});
		ajustHeight();
    });
    
    //-->
</SCRIPT>
</head>

<body>
<div class="rtit">座位设置</div>
<div id="list" class="rbox1"></div>
<div class="rbox2">
<div class="rcon2">
<table id="seats" width="100%" class="mtable"></table></div></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>