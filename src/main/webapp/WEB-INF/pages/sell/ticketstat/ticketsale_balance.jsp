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
<style type="text/css">
td{
	text-align: center
}
</style>
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
		                 			     }
		                 			 ]}	
    }
    var roleString = '${sessionScope.roleString}';
	var userId = '${sessionScope.userInfo.id}';
    function queryData(e,queryForm){
    	var param = {};
    	var playDate = queryForm.find(":input[name=playDate]:eq(0)").val();
    	var playDate2 = queryForm.find(":input[name=playDate]:eq(1)").val();
    	var createTime = queryForm.find(":input[name=createTime]:eq(0)").val();
    	var createTime2 = queryForm.find(":input[name=createTime]:eq(1)").val();
    	var createUserId = queryForm.find(":input[name=createUserId]").val();
    	if(playDate===''&&createTime===""){
    		layer.msg("请限定售票日期或者演出日期")
    		return;
    	}
    	if(roleString.indexOf("financial")==-1){
    		param.createUserId = userId;
    	}else{
    		var createUserId = queryForm.find(":input[name=createUserId]").val();
    		if(createUserId!=''){
        		param.createUserId = createUserId;
        	} 
    	}
    	var table = $('#list table');
    	table.find("tr:gt(0)").remove();
    	$.ajax({
    		url:"<%=path%>/public/stat/queryTicketSaleBalance.htmls",
    		type:"POST",
    		dataType:"json",
    		async:false,
    		data:{paramString:JSON.stringify(param)},
    		success:function(ret){
    			if(ret==''){
    				table.append($("<tr><td colspan='8'>没有数据！</td></tr>"));
    				return;
    			}
    			var map1 = _.groupBy(ret,function(v){
    				return v[0];
    			})
    			var keys1 = _.keys(map1);
    			for(var i=0;i<keys1.length;i++){
    				var first1 = 0;
    				var map2 = _.groupBy(map1[keys1[i]],function(v){
        				return v[1];
        			})
        			var keys2 = _.keys(map2);
    				
    				for(var j=0;j<keys2.length;j++){
    					var first2 = 0;
    					var map3 = _.groupBy(map2[keys2[j]],function(v){
            				return v[2];
            			})
            			var keys3 = _.keys(map3);
    					var tr3;
    					var totalNum1 = 0,amount1 = 0 ,realAmount1 = 0;
    					for(var n=0;n<keys3.length;n++){
    						var first3 = 0;
    						var list = map3[keys3[n]];
    						var tr;
    						var totalNum3 = 0,amount3 = 0 ,realAmount3 = 0;
    						for(var m=0;m<list.length;m++){
    							tr = $("<tr>").appendTo(table);
    							var data = list[m]
    							totalNum1 += parseInt(data[5]);
    							amount1 += parseInt(data[6]);
    							realAmount1 += parseInt(data[7]);
    							totalNum3 += parseInt(data[5]);
    							amount3 += parseInt(data[6]);
    							realAmount3 += parseInt(data[7]);
    							for(var x=0;x<data.length;x++){
    								
    								if(x==2){
    									if(first3===0){
    	    								first3++;
    	    								tr.append($("<td>").attr("rowspan",list.length).html(data[2]));
    	    								
    	    							}
    								}else{
    									if(x==1){
    										if(first2===0){
    		    								first2++;
    		    								tr.append($("<td>").attr("rowspan",map2[keys2[j]].length+keys3.length).html(data[1]));
    		    							}
    									}else{
    										if(x===0){
    											if(first1===0){
    			    								first1++;
    			    								tr.append($("<td>").attr("rowspan",map1[keys1[i]].length+keys2.length+2).html(data[0]));
    			    							}
    										}else{
    											tr.append($("<td>").html(data[x]));
    										}
    										
    									}
    									
    								}
    								
    							}
    							
    						}
    						tr3 = $("<tr style='background-color:#F4F4F4;color:green'>").append("<td>结算点小计</td><td>---</td><td>---</td>"
    						+"<td>"+totalNum3+"</td><td>"+amount3+"</td><td>"+realAmount3+"</td>");
        					tr.after(tr3);
    					}
    					var tr4 = $("<tr style='color:red;font-weight:bold'>").append("<td>当日合计</td><td>---</td><td>---</td><td>---</td><td>"
    							+totalNum1+"</td><td>"+amount1+"</td><td>"+realAmount1+"</td>");
    					tr3.after(tr4);
    				}
    			}
    		}
    	})
    	ajustHeight();
    }
	var form;
    $(document).ready(function(){
    	form = new FormView("formview",formSetting);
    	form.init();
    	$('#formview form').find(":input[name=createTime]").val(getCurrentTime());
    	queryData(event,$('#formview form'));
		
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
<th width="100">售票员</th><th width="100">售票日期</th><th width="140">结算时间</th>
<th width="140">打印方式</th><th width="100">演出日期</th>
<th width="100">售票张数</th><th width="100">票面金额</th><th width="100">实收金额</th>
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