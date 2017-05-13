<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css?v=<%=Math.random()%>" />
 <link rel="stylesheet" href="<%=path %>/static/farbtastic/farbtastic.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=<%=Math.random()%>"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>

<SCRIPT type="text/javascript">
    <!--
    $.ajax({
    	url:'http://120.25.209.173:9090/mgr/outside/lockSeats.htmls',
    	type:'post',
    //	data:{"seat_map['Y01'][0]":'3,36',"seat_map['Y01'][1]":'3,37',theater_sn:'JY0001',play_date:'2016-06-27',"price_map['Y01']":120,shownumber_id:1,mobileno:1123},
    	data:{theater_sn:'JY0001',play_date:'2016-06-28',shownumber_id:1,mobileno:1123
    		,'area_codes':"Y01",'col_nos':34,'row_nos':1
    		},
    	dataType:'json',
    	//contentType:"application/json",
    	success:function(ret){
    		alert(JSON.stringify(ret))
    	}
    }) 
    genDicJs(['0230']);
	var gridSetting = {
			id:'areaGrid',
			po:"Area",
			title:'<lable style="font-size:10px">标识排号：0 ： 左右加数字标识；1：右边加n排标识；2：左边加n排标识；3：在左右两旁添加n排的标识</label>',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 10,//auto表示根据容器高度自动判断
				orderby : "orderno"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'code',
				   descs:"编号",
				   align:'center',
				   editable: true,
				   width:60
			   } ,{
				   name: 'name',
				   descs:"区域名称",
				   align:'center',
				   editable: true,
				   width:150
			   } ,{
				   name: 'descs',
				   descs:"区域说明",
				   align:'center',
				   editable: true,
				   width:150
			   } ,{
				   name: 'floorNum',
				   descs:"楼层",
				   align:'center',
				   editable: true,
				   width:70
			   }     
			   ,{
				   name: 'totalRowNum',
				   descs:"行数",
				   align:'center',
				   editable: true,
				   width:70
			   } ,{
				   name: 'totalColumnNum',
				   descs:"列数",
				   align:'center',
				   editable: true,
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
				   length:4,
				   editable: true,
				   width:70
			   },{
				   name: 'mark',
				   descs:"标识排号",
				   align:'center',
				   length:4,
				   editable: true,
				   width:70
			   },{
				   name: 'defaultNum',
				   descs:"默认人数",
				   align:'center',
				   editable: true,
				   width:70
			   } ,{
				   name: 'blockId',
				   descs:"分块",
				   align:'center',
				   editable: true,
				   width:70
			   },{
				   name: 'orders',
				   descs:"块内排序",
				   align:'center',
				   editable: true,
				   width:70
			   },{
				   name: 'backgroundColor',
				   descs:"背景颜色",
				   align:'center',
				   editable: true,
			//	   init: bindColorpicker,
				   width:70
			   } ,{
				   name: 'orderno',
				   descs:"排序",
				   align:'center',
				   editable: true,
				   width:70
			   }   
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'add',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var newRow = grid.newRow();
			    			 grid2.addExData(newRow,{belongto:belongto});
			    		 }
			    	 }
			     }, {
			    	 id:'save',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 grid.save("",'refreshAreaCache',function(ret) {
			    				if (ret == -1) {
			    					layer.alert("数据保存失败，请重试！");
			    				} else {
			    					layer.msg("数据已保存！");
			    					grid.reload(ret.id);
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
			    			 grid.deletes("beforeDeleteArea",function(ret){
			    				 if(ret==-1){
			    					 layer.msg("删除失败，请重试！");
			    				 }else{
			    					 layer.msg("删除" + ret.recordNum + "条记录！",function(ret){
			    						 grid.reload();
			    					 });
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }, {
			    	 id:'generate',
			    	 title:'生成座位',
			    	 handlers:{
			    		 click: function(){
			    			 var rows = grid.getSelectedRows();
			    			 if(rows.length==0){
			    				 alert("请选择区域记录");
			    			 }else{
			    				 if(confirm('生成新座位前将删除旧座位，是否继续？')){
				    				 var id = rows.attr("recordId");
				    				 BaseDwr.genSeats(id,function(ret){
				    					 alert(ret);
				    					 grid2.reload();
				    					 ajustHeight();
				    				 });
				    			 }
			    			 }
			    		 }
			    	 }
			     }, {
			    	 id:'sync',
			    	 title:'同步座位名称',
			    	 width:120,
			    	 handlers:{
			    		 click: function(){
			    			 var rows = grid.getSelectedRows();
			    			 if(rows.length==0){
			    				 if(confirm('是否确定修改所有座位的名称？')){
				    				 BaseDwr.syncAllSeatName('${sessionScope.theater.id}',function(ret){
				    					 alert("修改完成")
				    					 ajustHeight();
				    				 });
				    			 }
			    			 }else{
			    				 if(confirm('是否确定修改该区域的座位名称？')){
				    				 var id = rows.attr("recordId");
				    				 BaseDwr.syncSeatName(id,function(ret){
				    					 grid2.reload();
				    					 ajustHeight();
				    				 });
				    			 }
			    			 }
			    		 }
			    	 }
			     }
			 ]}
		};
	var gridSetting2 = {
			id:'seatGrid',
			po:"Seat",
			title:'座位列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 20,//auto表示根据容器高度自动判断
				orderby : "rowNum,columnNum"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"座位名称",
				   align:'center',
				   editable: true,
				   width:150
			   },{
				   name: 'mappedName',
				   descs:"显示名称",
				   align:'center',
				   editable: true,
				   width:150
			   }      
			   ,{
				   name: 'rowNum',
				   descs:"行数",
				   align:'center',
				   editable: true,
				   width:100
			   } ,{
				   name: 'columnNum',
				   descs:"列数",
				   align:'center',
				   editable: true,
				   width:100
			   },{
				   name: 'price',
				   descs:"价格",
				   align:'center',
				   editable: true,
				   width:100
			   },{
				   name: 'type',
				   descs:"类别",
				   align:'center',
				   editable: true,
				   type : 'select',
				   width:100
			   } ,{
				   name: 'style',
				   descs:"样式",
				   align:'center',
				   editable: true,
				   width:100
			   }   
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'add2',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var rows = grid.getSelectedRows();
			    			 if(rows.length==0){
			    				 layer.msg("请选择区域");
			    			 }else{
			    				 var newRow = grid2.newRow();
			    				 grid2.addExData(newRow,{areaId:$(rows[0]).attr('recordId')});
			    			 }
			    			 
			    		 }
			    	 }
			     }, {
			    	 id:'save2',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 grid2.save("","modifySeat",function(ret) {
			    				if (ret == -1) {
			    					layer.alert("数据保存失败，请重试！");
			    				} else {
			    					layer.msg("数据已保存！");
			    					grid2.reload(ret.id);
			    				}

			    			});
			    		 }
			    	 }
			     }
			     , {
			    	 id:'delete2',
			    	 title:'删除',
			    	 handlers:{
			    		 click: function(){
			    			 grid2.deletes(function(ret){
			    				 if(ret==-1){
			    					 layer.msg("删除失败，请重试！");
			    				 }else{
			    					 layer.msg("删除" + ret.recordNum + "条记录！",function(ret){
			    						 grid2.reload();
			    					 });
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }
			 ]}
		};
	var grid,grid2;
	var belongto = '${sessionScope.theater.id}';
	
	function bindColorpicker(obj){
		
		$('#picker').farbtastic(function(c){
			layer.alert(obj.attr("name"))
			obj.val(c);
		});
	}
	var f;
    $(document).ready(function(){
    	//f = $.farbtastic('#picker');
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load({data:{belongto:belongto}});
    	grid2 = new GridView("list2",gridSetting2,"list2");
		grid2.init();
		grid.rowClick(function(id){
			grid2.load({data:{areaId:id}});
			$('div#list2 div.rcon2').css('height', '');
			ajustHeight2();
		});
		
		ajustHeight();
		
    });
    //-->
</SCRIPT>
</head>

<body>
<div id="picker" style="float: right;"></div>
<div class="rtit">区域设置</div>
<div id="list" class="rbox1"></div>
<div id="list2" class="rbox2"></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
 <script type="text/javascript" src="<%=path %>/static/farbtastic/farbtastic.js"></script>

</body>
</html>
