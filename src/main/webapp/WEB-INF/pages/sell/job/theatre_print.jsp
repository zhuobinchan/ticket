<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/accjtld" prefix="ac" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path %>/static/bussiness/salecommon.css?v=<%=Math.random() %>" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/bussiness/saleprint.css?v=<%=Math.random() %>" />
<link href="<%=path %>/static/jeegoocontext/skins/cm_default/style.css" rel="Stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=path %>/static/jquery-ui/jquery-ui.theme.min.css" />
</head>
<body style="background-color: #000">
<div id="progress" style="position: absolute;top:300px;left:410px;color: green;font-size: 20pt">正在加载数据，请稍后...</div>
<ac:theaterTopDesc/>
<div class="section" floor="1">
<table width="99%" align="center" style="margin-left: 10px" id="danceTable1" border="0" cellpadding="0" cellspacing="0">
<tr valign="top">
	<td id="D1"></td>
	<td style="background-color: #CAE1FF" align="center" valign="middle">
	<ac:createPlayTime/>
	</td>
	<td id="D2"></td>
</tr>
<tr valign="top">
	<td id="C3B3"></td><td id="C2B2B4"></td><td id="C1B1"></td>
</tr>
<tr>
<td colspan="3">&nbsp;</td>
</tr>
<tr valign="top">
	<td id="A3"></td><td id="A2"></td><td id="A1"></td>
</tr>
<tr>
	<td id="td1">
		<div id="V2"></div>
	</td>
	<td align="center" style="background-color: #000;">
		<table id="title" style="display:none" border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="reverseTxt" style="color:red;font-weight: 华文彩云;font-size: 60px;text-align: center">大 &nbsp;舞 &nbsp;台</td>
		</tr>
		</table>
	</td>
	<td id="td2">
		<div id="V1"></div>
	</td>
</tr>
</table>
</div>
<div class="section" style="display:none" floor="2">
<table width="99%" align="center" style="margin-left: 10px;" id="danceTable3" border="0" cellpadding="0" cellspacing="0">
	<tr><td id="L1"></td></tr>
	<tr><td id="I2"></td></tr>
	<tr><td id="I1"></td></tr>
	<tr><td id="G1"></td></tr>
	<tr><td id="F1"></td><!-- <td id="F2"></td><td id="F3"></td> --></tr>
	<tr><td id="E1"></td></tr>		
</table>
<br />
<br />
<br />
<table width="99%" align="center" style="margin-left: 10px" id="danceTable2" border="0" cellpadding="0" cellspacing="0">
	<tr><td id="H3"></td><td id="H2"></td><td id="H1"></td></tr>
	<tr><td id="K3"></td><td id="K2"></td><td id="K1"></td></tr>
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr><td id="J1" colspan="3"></td></tr>
	<tr>
		<td colspan="3" class="reverseTxt" style="color:red;font-weight: 华文彩云;font-size: 60px;text-align: center">大 &nbsp;舞 &nbsp;台</td>
	</tr>
</table>
</div>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js?v=<%=Math.random() %>"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<SCRIPT type="text/javascript">
    <!--
    var userId = '${sessionScope.userInfo.id}';
    var mouseoverEvent = 0,lastOutTd = null/* 刚刚经过的那个座位，用于鼠标离开时取消座位 */;

    var drawSeatGraph=function(html,id){
    	var div = $('#'+id).html('');
		div.html(html);
	}
    var drawSeatGraphs=function(){
    	for(var i=0;i<top.areaNames.length;i++){
    		var areaName = top.areaNames[i];
    		if($("#"+areaName).length===1){
    			drawSeatGraph(top[areaName],areaName);
        		readyEvents(areaName);
    		}
    	}
    	$("#C2B2B4").html("").append(top.C2).find("table tbody").append($(top.B2).find("tbody").html()).append($(top.B4).find("tbody").html());
    	$("#C3B3").html("").append(top.C3).find("table tbody").append($(top.B3).find("tbody").html());
    	$("#C1B1").html("").append(top.C1).find("table tbody").append($(top.B1).find("tbody").html());
    	readyEvents('C2B2B4');
    	readyEvents('C3B3');
    	readyEvents('C1B1');
    	
    }
    var h = $(document).height();//计算当前容器的高度
    $(document).ready(function(){
    	$('#title').delay("slow").fadeIn();
    	drawSeatGraphs();
    	$('#progress').hide();
    });
    //-->
</SCRIPT>
<%@include file="contextMenu.jspf"%>
<script type='text/javascript' src='<%=path %>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path %>/static/js/LodopFuncs.js'></script>
<script type='text/javascript' src='<%=path %>/static/bussiness/saleprint.js?v=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=path %>/static/js/date.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'> </script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'> </script>
<script type='text/javascript' src='<%=path %>/dwr/interface/MessagePush.js'> </script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
</body>
</html>