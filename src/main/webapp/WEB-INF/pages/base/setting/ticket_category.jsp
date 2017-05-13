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
    genDicJs(['0202','0208','0212']);
	var gridSetting = {
			id:'strategyGrid',
			po:"TicketStrategy",
			title:'门票策略列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "orderno,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"策略名称",
				   align:'center',
				   editable: true,
				   width:100
			   }  ,{
				   name: 'price',
				   descs:"指定价格",
				   align:'center',
				   editable: true,
				   width:70
			   }    
			   ,{
				   name: 'cheapPrice',
				   descs:"优惠金额",
				   align:'center',
				   editable: true,
				   width:70
			   } ,{
				   name: 'offsetPrice',
				   descs:"抵扣金额",
				   align:'center',
				   editable: true,
				   width:70
			   },{
				   name: 'discount',
				   descs:"折扣比例%",
				   align:'center',
				   editable: true,
				   width:70
			   },{
				   name: 'algorithm',
				   descs:"算法",
				   align:'center',
				   editable: true,
				   width:100
			   },{
				   name: 'valueType',
				   descs:"计价方式",
				   type:'select',
				   align:'center',
				   editable: true,
				   width:70
			   }  ,{
				   name: 'mustPrint',
				   descs:"必须打印",
				   type:'select',
				   align:'center',
				   editable: true,
				   width:90
			   }  ,{
				   name: 'status',
				   descs:"记录状态",
				   type:'select',
				   align:'center',
				   editable: true,
				   width:70
			   }  ,{
				   name: 'orderno',
				   descs:"排序",
				   align:'center',
				   type:'int',
				   editable: true,
				   width:50
			   }   
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'add',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var newRow = grid.newRow();
			    			 grid.setCellValue(newRow,"status","020201");
			    		 }
			    	 }
			     }, {
			    	 id:'save',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 grid.save(function(ret) {
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
		};
	var grid;
    $(document).ready(function(){
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div class="rtit">门票策略设置</div>
<div id="list" class="rbox1"></div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>