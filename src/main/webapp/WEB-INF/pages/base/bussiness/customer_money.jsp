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
<script type="text/javascript" src="<%=path%>/static/layer/extend/layer.ext.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<SCRIPT type="text/javascript">
    <!--
    genDicJs(['0202','0206']);
    var userMap = genMaps("from User order by name,id");
    var formSetting = {
    		po:'Customer',
    		type:"query",
    		hiddens:["id"],
    	fields:[[{title:"客户简称",type:"text",name:"shortname"},
    	  {title:"字母检索",type:"text",name:"spell"},
    	  {title:"客户类型",type:"select",name:"type",dic:"0206"}]
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
			id:'customerGrid',
			po:"Customer",
			title:'客户资金列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: 12,//auto表示根据容器高度自动判断
				orderby : "createTime,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'code',
				   descs:"客户编号",
				   align:'center',
				   width:50
			   },{
				   name: 'shortname',
				   descs:"客户简称",
				   align:'center',
				   width:150
			   },{
				   name: 'rechargeAmount',
				   descs:"最近充值金额",
				   align:'center',
				   width:80
			   } ,{
				   name: 'balance',
				   descs:"账户余额",
				   align:'center',
				   width:80
			   }     ,{
				   name: 'type',
				   descs:"客户类型",
				   type:'select',
				   align:'center',
				   width:70
			   }  
			  ,{
				   name: 'rechargeTime',
				   descs:"最近充值时间",
				   type:'text',
				   align:'center',
				   width:100
			   }  
			],
			toolbar:{align:'right',buttons:[
			                 			   {
			                 			    	 id:'add',
			                 			    	 title:'充值',
			                 			    	 handlers:{
			                 			    		 click: function(){
			                 			    			var selects = grid.getSelectedRows();
			                 			    			if(selects.length!=1){
			                 			    				layer.alert("请选择客户记录",{title:"提示信息"});
			                 			    				return;
			                 			    			}
			                 			    			layer.prompt({
			                 			    			    title: '输入充值金额',
			                 			    			    formType: 0 //prompt风格，支持0-2
			                 			    			}, function(money){
			                 			    			   var id = selects.attr("recordId");
			                 			    			   BaseDwr.recharge(money,id,function(ret){
			                 			    				   grid.reload(id);
			                 			    				   layer.msg("充值"+money+"元完成");
			                 			    			   });
			                 			    			});

			                 			    		 }
			                 			    	 }
			                 			     }, {
			                 			    	 id:'save',
			                 			    	 title:'查看充值历史',
			                 			    	 width:100,
			                 			    	 handlers:{
			                 			    		 click: function(){
			                 			    			var selects = grid.getSelectedRows();
			                 			    			if(selects.length!=1){
			                 			    				layer.alert("请选择客户记录",{title:"提示信息"});
			                 			    				return;
			                 			    			}
			                 			    			layer.open({
			                 			    	    		title:'客户充值历史',
			                 			    	    		id:'rechargewin',
			                 			    			    type: 2,
			                 			    			    btn :["关闭"],
			                 			    			    skin: 'layui-layer-rim', //加上边框
			                 			    			    area: ['800px', '400px'], //宽高
			                 			    			    content: '<%=path%>/public/common/r2.htmls?page=base/bussiness/recharge_win&id='+selects.attr("recordId"),
			                 			    			    end:function(ret){
			                 			    			    	
			                 			    			    }
			                 			    			});
			                 			    		 }
			                 			    	 }
			                 			     }
			                 			     
			                 			 ]}
		};
	var grid,form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	grid = new GridView("list",gridSetting,"list");
		grid.init();
		grid.load();
		grid.rowClick(function(id){
			
		});
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox2"></div>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>