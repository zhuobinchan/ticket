<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/accjtld" prefix="ac" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<script type="text/javascript" src="<%=path%>/static/js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bussiness/salecommon.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bussiness/saleprint_cz.css" />
<link href="<%=path%>/static/jeegoocontext/skins/cm_default/style.css" rel="Stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=path%>/static/jquery-ui/jquery-ui.theme.min.css" />
</head>
<body style="background-color: #000">
	<div id="progress" style="position: absolute; top: 300px; left: 410px; color: green; font-size: 20pt">正在加载数据，请稍后...</div>
	<ac:theaterTopDesc showInput="true"/>
	<div id="floors"></div>
	<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
	<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
	<jsp:include page="../../base/bussiness/include.jsp" />
	<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
	<script type="text/javascript" src="<%=path%>/static/js/sell/job/theatre_print_cz.js"></script>
	<script type="text/javascript" src="<%=path%>/static/js/sell/job/init_theater.js"></script>
	<script type="text/javascript" src="<%=path%>/static/jeegoocontext/jquery.jeegoocontext-2.0.0.min.js"></script>
	<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
	<script type='text/javascript' src='<%=path%>/static/js/LodopFuncs.js'></script>
	<script type='text/javascript' src='<%=path%>/static/bussiness/saleprint.js'></script>
	<script type='text/javascript' src='<%=path%>/static/bussiness/saleprint_cz.js'></script>
	<script type='text/javascript' src='<%=path%>/static/js/date.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/MessagePush.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/BaseDwr.js'></script>
	<script type="text/javascript" src="<%=path%>/static/jquery-ui/jquery-ui.min.js"></script>
	<ul id="menu" class="jeegoocontext cm_default">
		<li name="saled" id="saled" class="icon"><span class="icon file"></span><span name='text'></span></li>
		<li name="saled" id="gaiqian" class="icon"><span class="icon disk"></span><span name='text'></span></li>
		<li name="saled" id="gaiqianall" class="icon"><span class="icon file"></span><span name='text'></span></li>
		<li id="salereserved" class="icon"><span class="icon file"></span><span name='text'></span></li>
		<li id="clearSelected" class="icon"><span class="icon file"></span>清空选择</li>
		<li id="clearLocked" class="icon"><span class="icon disk"></span>全部解锁</li>
		<li id="quickreserve" class="icon"><span class="icon file"></span>快速预订</li>
		<li id="reserve" class="icon"><span class="icon drive"></span>预订</li>
		<li id="sale" class="icon"><span class="icon ok"></span>售票</li>
		<li id="changeSeat" class="icon"><span class="icon drive"></span>更换座位</li>
		<li id="info" class="icon"><span class="icon drive"></span><label></label></li>
		<li class="separator"></li>
		<li id="selectAll" class="icon"><span class="icon file"></span><label>打开指示灯</label></li>
		<li id="refrushPage" class="icon"><span class="icon file"></span>刷新本页</li>
		<li id="quit" class="icon"><span class="icon disk"></span>退出菜单</li>
	</ul>
	<script type="text/javascript">
		var userId = '${sessionScope.userInfo.id}';
		var currentTheaterId = '<%=request.getAttribute("theaterId")%>';
	</script>
</body>
</html>