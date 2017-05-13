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
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
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
    	  {title:"售票员",type:"select",name:"createUserId",map:userMap},
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
    function queryData(e,queryForm){
    	var table = $('#list table');
    	table.find("tr:gt(0)").remove();
    	
    	//查询条件
    	var param = {};
    	var createUserId = queryForm.find(":input[name=createUserId]").val();
    	
    	if(createUserId!=''){
    		alert(createUserId);
    		param.createUserId = createUserId;
    	} 
    	
    	var showNumberId = queryForm.find(":input[name=showNumberId]").val();
    	if(showNumberId!=''){
    		alert(showNumberId);
    		param.showNumberId = showNumberId;
    	} 
    	
    	var startCreateTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var endCreateTime = queryForm.find(":input[name=createTime]:eq(1)").val();
    	
    	if(startCreateTime!= '' && endCreateTime != ''){
    		param.startCreateTime = startCreateTime;
    		param.endCreateTime = endCreateTime;
    	} 
    	
    	$.ajax({
    		url:"<%=path%>/public/stat/createUserStat.htmls",
    		type:"POST",
    		dataType:"json",
    		async:false,
    		data:{paramString:JSON.stringify(param)},
    		success:function(ret){
    			for(var i=0;i<ret.length;i++){
    				var objs = ret[i];
    				var tr = $("<tr>").appendTo(table);
    				for(var j=0;j<objs.length;j++){
    					var td = $("<td align='center'>").html(objs[j]).appendTo(tr);
    				}
    				
    			}
    			var showNumbers = _.map(ret,_.first);
    			showNumbers = _.uniq(showNumbers);
    			for(var i=0;i<showNumbers.length;i++){
    				var trs = table.find('tr:contains('+showNumbers[i]+')');
    				var stys = _.filter(ret,function(num){//过滤门票策略的行数
    					return _.first(num) === showNumbers[i];
    				});
    				var uniqStrategys = _.map(stys,function(num){
    					return num[1];
    				});
    				uniqStrategys = _.uniq(uniqStrategys);
    				for(var j=0;j<uniqStrategys.length;j++){
    					var rows2=_.filter(stys,function(num){
    						return num[1]===uniqStrategys[j];
    					})
    					trs2 = trs.filter(function(){
    						return $(this).find("td:eq(1)").html() === uniqStrategys[j];
    					});
    					trs2.each(function(k,tr){
    						if(k==0){
    							$(tr).find("td:eq(1)").attr("rowspan",trs2.length);
    						}else{
    							$(tr).find("td:eq(1)").remove();
    						}
    					})
    				}
    				trs.each(function(i,tr){
    					if(i===0){
    						$(tr).find("td:eq(0)").attr('rowspan',trs.length);
    					}else{
    						$(tr).find("td:eq(0)").remove();
    					}
    					
    				})
    				
    			}
    			
    		}
    	})
    }
	var form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	queryData(event,$('#formview form'));
		ajustHeight();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div id="formview" class="rbox1"></div>
<div id="list" class="rbox1">
 <div class="rcon2">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mtable">
<tr>
<th>售票员</th><th>销售策略</th><th>票价</th><th>实际票价</th><th>售票张数</th><th>应收金额</th><th>实收金额</th>
</tr>
</table>
</div>
</div>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>