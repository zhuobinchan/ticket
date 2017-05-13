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
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=<%=Math.random()%>""></script>
<script type="text/javascript" src="<%=path %>/static/js/formview.js?v=<%=Math.random()%>""></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js?v=<%=Math.random()%>""></script>
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
		                 			    	 id:'print',
		                 			    	 title:'打印',
		                 			    	 hide: true,
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
		    										$("<form target='ef' action='${pageContext.request.contextPath}/public/stat/excelBalanceList.htmls'></form>").appendTo($('body')).submit();
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
    	if(roleString.indexOf("financial")==-1&&roleString.indexOf("saller_charge")==-1){
    		param.createUserId = userId;
    	}else{
    		var createUserId = queryForm.find(":input[name=createUserId]").val();
    		if(createUserId!=''){
        		param.createUserId = createUserId;
        	} 
    	}
    	if(createTime!==''){
    		param.createTime = createTime;
    	}
    	if(createTime2!==''){
    		param.createTime2 = createTime2;
    	}
    	var table = $('#list table');
    	table.find("tr:gt(0)").remove();
    	$.ajax({
    		url:"<%=path%>/public/stat/queryTicketSaleBalanceList.htmls",
    		type:"POST",
    		dataType:"json",
    		async:false,
    		data:{paramString:JSON.stringify(param)},
    		success:function(ret){
    			if(ret==''){
    				table.append($("<tr><td colspan='16'>没有数据！</td></tr>"));
    				return;
    			}
    			var tr = null;
    			var totals = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
    			for(var i=0;i<ret.length;i++){
    				var fields = ret[i];
    				tr = $("<tr>").appendTo(table);
    			//	totals[0] += parseInt(fields[2]);
    				for(var j=0;j<fields.length;j++){
    					if(j>1){
    						totals[j-1] += parseInt(fields[j]);
    					}
    					if(j===1){
    						tr.append($("<td>").css({"color":"green"}).html(fields[j]));
    					}else{
    						tr.append($("<td>").html(fields[j]));
    					}
    					
    				}
    			}
    			tr = $("<tr>").appendTo(table).css({color:'red',fontWeight:'bold'});
    			tr.append($("<td colspan='2'>").html("总计"));
    			for(var i=1;i<totals.length;i++){
    				tr.append($("<td>").html(totals[i]));
    			}
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
    	var strFormHtml=$("#list div.rcon2").html();//获取内容
    	var table = $(strFormHtml);
    	table.find("tr th:eq(1)").remove();
    	table.find("col:eq(1)").remove();
    	table.find("tr").each(function(i,v){
    		$(v).find("td:eq(1)").remove();
    	})
    	strFormHtml = strStyle+table[0].outerHTML;
    	var today = getCurrentTime();
    	var time = "23:59:59";
    	if(today==endTime){
    		time = getCurrentTime("HH:mm");
    	}
    	LODOP.ADD_PRINT_HTM(10,5, div.width(), 39,"<div style='width:"+div.width()+";text-align:center;font-size:13pt'>田汉大剧院</div>");
    	LODOP.ADD_PRINT_HTM(40,5, div.width(), 39,"<div style='width:"+div.width()+";text-align:center;font-size:13pt'>销售结算表</div>");
    	LODOP.ADD_PRINT_HTM(70,10, div.width(), 39,"<div style='width:100%;font-size:9pt;'>制表日期："+today+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制表人：${sessionScope.userInfo.name}</div>");
    	LODOP.ADD_PRINT_TABLE(106,5,div.width(),'90%',strFormHtml);
    	LODOP.ADD_PRINT_TEXT(15, 50, div.width(),25,"查询条件【"+startTime+" 至 " +endTime+" "+time+"】");
    	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",4);
    	//LODOP.SET_PRINT_STYLEA(0,"ItemType",1);	
    	/* LODOP.ADD_PRINT_HTM((pageHeight*0.1)+"mm","11mm", div.width(), 39,
    			"<div style='width:"+div.width()+";text-align:center;font-size:9pt'><span tdata='pageNO'>##</span>/<span tdata='pageCount'>##</span></div>");
    	*///LODOP.SET_PRINT_STYLEA(0,"ItemType",1); 

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
    	//按钮权限
    	var forbitBtns = $.parseJSON('${forbitBtns}');
		for(var i=0;i<forbitBtns.length;i++){
			$("#"+forbitBtns[i]).hide();
		}
		setTimeout(function(){
			$('#print').show();
		},1500);
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
<th width="100">售票日期</th><th width="100">售票员</th><th width="140">现金</th>
<th width="140">信用卡</th><th width="100">会员卡</th>
<th width="100">挂账</th><th width="100">免单</th><th width="100">微信</th>
<th width="140">支付宝</th><th width="100">充值卡</th>
<th width="100">美团</th><th width="100">百度</th><th width="100">有效票</th>
<th width="100">原价数</th><th width="100">浮动数</th><th width="100">退票数</th>
<th width="100">换票数</th><th width="100">免票数</th><th width="100">最低票</th>
<th width="100">优惠票</th><th width="100">改签票</th>
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