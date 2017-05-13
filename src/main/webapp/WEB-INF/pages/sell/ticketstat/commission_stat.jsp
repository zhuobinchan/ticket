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
td,th{white-space: nowrap;}
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
		                 			     }, {
		                 			    	 id:'reset',
		                 			    	 title:'打印',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			print();
		                 			    		 }
		                 			    	 }
		                 			     }, {
		                 			    	 id:'excel',
		                 			    	 title:'导出',
		                 			    	 handlers:{
		                 			    		 click: function(){
		                 			    			$("<iframe name='ef' style=\"display: none\"></iframe>").appendTo($("body"));
		    										$("<form target='ef' action='${pageContext.request.contextPath}/public/stat/excelCommisionStat.htmls'></form>").appendTo($('body')).submit();
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
    	if(playDate!=''){
    		param.playDate = playDate;
    	}
    	if(playDate2!=''){
    		param.playDate2 = playDate2;
    	}
    	if(createTime!=''){
    		param.createTime = createTime;
    	}
    	if(createTime2!=''){
    		param.createTime2 = createTime2;
    	}
    	if(createUserId!=''){
    		param.createUserId = createUserId;
    	}
    	var table = $('#list table');
    	table.find("tr:gt(0)").remove();
    	$.ajax({
    		url:"<%=path%>/public/stat/querySalesmanCommissionStat.htmls",
    		type:"POST",
    		dataType:"json",
    		async:false,
    		data:param,
    		success:function(ret){
    			if(ret==''){
    				table.append($("<tr><td colspan='10'>没有数据！</td></tr>"));
    				return;
    			}
    			var tr = null;
    			var totalNum = 0,amount = 0,commision=0;
    			for(var i=0;i<ret.length;i++){
    				var fields = ret[i];
    				tr = $("<tr>").appendTo(table);
    				tr.append($("<td>").html(i+1));
    				amount += parseInt(fields[5]);
    				commision += parseInt(fields[7]);
    				totalNum += parseInt(fields[8]);
    				for(var j=0;j<fields.length;j++){
    					if(j===1){
    						tr.append($("<td>").css({"color":"green","font-style":'italic'}).html(fields[j]));
    					}else{
    						tr.append($("<td>").html(fields[j]));
    					}
    					
    				}
    			}
    			tr = $("<tr>").appendTo(table).css({color:'red',fontWeight:'bold'});
    			tr.append($("<td colspan='2'>").html("总计"));
    			tr.append($("<td>").html("---"));
    			tr.append($("<td>").html("---"));
    			tr.append($("<td>").html("---"));
    			tr.append($("<td>").html("---"));
    			tr.append($("<td>").html(amount));
    			tr.append($("<td>").html("---"));
    			tr.append($("<td>").html(commision));
    			tr.append($("<td>").html(totalNum));
    		}
    	})
    	ajustHeight();
    }
    function CreateOneFormPage(){
    	var startTime = $('#formview_createTime').val();
    	var endTime= $('#formview_createTime_2').val();
    	LODOP=getLodop();  
    	LODOP.SET_PRINT_PAGESIZE(2,2100,2970,'');
    	var div = jQuery('#list');
    	var strStyle="<style>td,th {border:1px #000 solid;border-collapse: collapse;font-size:7pt;text-align:center}</style>";
    	var strFormHtml=strStyle+document.getElementById("list").innerHTML;
    	var today = getCurrentTime();
    	var time = "23:59:59";
    	if(today==endTime){
    		time = getCurrentTime("HH:mm");
    	}
    	LODOP.ADD_PRINT_HTM(10,"11mm", div.width(), 39,
    			"<div style='width:"+div.width()+";text-align:center;font-size:13pt'>田汉大剧院</div>");
    	LODOP.ADD_PRINT_HTM(40,"11mm", div.width(), 39,
    			"<div style='width:"+div.width()+";text-align:center;font-size:13pt'>营销员提成统计表</div>");
    	LODOP.ADD_PRINT_HTM(80,"11mm", div.width(), 39,
    			"<div style='width:100%;font-size:9pt;'>制表日期："+today+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制表人：${sessionScope.userInfo.name}</div>");
    	var addHeight = div.find('tr').length * 5;
    	var htmlHeight = div.height()+addHeight;
    	LODOP.ADD_PRINT_HTM(100,"6mm",div.width(),"BottomMargin:15mm",strFormHtml);
    	var pageHeight = LODOP.GET_VALUE('PRINTSETUP_PAGE_HEIGHT',0)-99;
    	LODOP.ADD_PRINT_HTM((pageHeight*0.1)+"mm", "11mm", div.width(), "BottomMargin:0mm",
    			"<table style='width:100%;font-size:9pt'><tr><td>查询条件【"+startTime+" 至 " +endTime+" "+time+"】</td></tr></table>");
    	pageHeight = pageHeight + 70;
    	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
    	LODOP.ADD_PRINT_HTM((pageHeight*0.1)+"mm","11mm", div.width(), 39,
    			"<div style='width:"+div.width()+";text-align:center;font-size:9pt'><span tdata='pageNO'>##</span>/<span tdata='pageCount'>##</span></div>");
    	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);

    	LODOP.SET_PRINT_STYLEA(0,"Horient",1);	
    };
    function print() {		
    	CreateOneFormPage();
    	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Auto-Width");
    	LODOP.PREVIEW();	
    };
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
<th width="30"></th>
<th width="140">营销号</th>
<th width="100">营销员</th><th width="100">服务部门</th>
<th width="140">工作单位</th><th width="100">操作员</th>
<th width="100">消费金额</th><th width="70">提成比例</th>
<th width="100">提成金额</th><th width="100">票数</th>
</tr>
</table>
</div>
</div>

<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type='text/javascript' src='<%=path %>/static/js/LodopFuncs.js'></script>
</body>
</html>