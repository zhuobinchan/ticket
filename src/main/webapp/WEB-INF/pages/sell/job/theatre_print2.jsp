<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path %>/static/bussiness/salecommon.css?v=<%=Math.random() %>" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/bussiness/saleprint2.css?v=<%=Math.random() %>" />
<link href="<%=path %>/static/jeegoocontext/skins/cm_default/style.css" rel="Stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=path %>/static/jquery-ui/jquery-ui.theme.min.css" />
</head>
<body style="background-color: #000">
<div id="progress" style="position: absolute;top:300px;left:410px;color: green;font-size: 20pt">正在加载数据，请稍后...</div>
<table cellspacing="1" align="center" class="topInfo" style="background-color: #404040;color: #fff;font-size: 14px">
<tr>
<td width="80" align="center"><a class="btn6" onclick="selectFloor(1)">一层</a></td>
<td width="80" align="center"><button class="btn6" onclick="selectFloor(2)">二层</button></td>
<td width="80" align="center"><a class="btn6" href="javascript:void(0)" id="sizeSwitch">全屏</a></td>
<td width="250" align="center"><span id="seatDescs"></span></td>
<td>演出日期：<span id="topPlayDate" style="color:#CD6600;font-weight:bold;padding-right:10px"></span></td>
</tr>
</table>
<div class="section">
<table width="99%" align="center" style="margin-left: 10px" id="danceTable1" border="0" cellpadding="0" cellspacing="0">
<tr valign="top">
	<td style="background-color: #CAE1FF" colspan="4">
	<table width="100%" cellpadding="0" cellspacing="0" style="margin: 0">
			<tr>
		<td align="center" height="30">
		<table border="0" width="100%" align="center">
		<tr>
		<td align="center" height="30" width="90"><a class="btn6" href="javascript:void(0)" id="sizeSwitch">全屏</a><a class="btn7" id="selectMap" href="javascript:void(0)">刷新</a></td>
		</tr>
		<tr>
		<td align="center" height="20"><input type="text" name="playDate" id="playDate" onfocus="WdatePicker({onpicking:function(){setSeatStatus($dp.cal.getNewDateStr())}})" align="center" size="10"/>
		<span id="showNumberDiv"></span>
		</td>
		</tr>
		</table>
		</td>
		</tr>
		</table>
	</td>
</tr>
<tr>
<td colspan="4">
	<table>
	 <tr><td class="areaLabel" align="center">右厅</td><td class="areaLabel" align="center">中厅</td><td class="areaLabel" align="center">左厅</td>
	 <tr><td id="H03"></td><td id="H02"></td><td id="H01"></td>
	 </tr>
	</table>
</td>
</tr>
<tr>
	<td>
		<table>
		 <tr><td id="G10"></td><td id="G09"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="G08"></td><td id="G07"></td><td id="G06"></td><td>&nbsp;</td></td><td id="G05"></td><td id="G04"></td><td id="G03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="G02"></td><td id="G01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
		 <tr><td id="F10"></td><td id="F09"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="F08"></td><td id="F07"></td><td id="F06"></td><td>&nbsp;</td><td id="F05"></td><td id="F04"></td><td id="F03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="F02"></td><td id="F01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
		 <tr><td id="E10"></td><td id="E09"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="E08"></td><td id="E07"></td><td id="E06"></td><td>&nbsp;</td><td id="E05"></td><td id="E04"></td><td id="E03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="E02"></td><td id="E01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
		 <tr><td id="D10"></td><td id="D09"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="D08"></td><td id="D07"></td><td id="D06"></td><td>&nbsp;</td><td id="D05"></td><td id="D04"></td><td id="D03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="D02"></td><td id="D01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
		 <tr><td id="B10"></td><td id="B09"></td></tr>
		</table>
	</td>
	
	<td>
		<table>
		 <tr><td id="B08"></td><td id="B07"></td><td id="B06"></td><td>&nbsp;</td><td id="B05"><td id="B04"></td></td><td id="B03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="B02"></td><td id="B01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
		 <tr><td id="C10"></td><td id="C09"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="C08"></td><td id="C07"></td><td id="C06"></td><td>&nbsp;</td><td id="C05"></td><td id="C04"></td><td id="C03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="C02"></td><td id="C01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
		 <tr></td><td id="A10"></td><td id="A09"></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="A08"></td><td id="A07"></td><td id="A06"></td><td>&nbsp;</td><td id="A05"></td><td id="A04"></td><td id="A03"></td></tr>
		</table>
	</td>
	<td>
		<table>
		 <tr><td id="A02"></td><td id="A01"></td></tr>
		</table>
	</td>
</tr>
<tr>
	<td colspan="4" align="center" style="background-color: #000;">
		<table id="title" style="display:none" border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="reverseTxt" style="color:red;font-weight: 华文彩云;font-size: 50px;text-align: center">大 &nbsp;舞 &nbsp;台</td>
		</tr>
		</table>
	</td>
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
    var cancelOne = function(obj){
		synccolor($('#playDate').val(),[obj.attr("areaId")]);
    }
    var pickSeat = function(obj){
    	if(obj.hasClass("saled")){
    		layer.msg("已售");
    	}
    	obj.addClass("picked").removeClass("toggle");
		obj.attr("time",new Date().format("yyyy-MM-dd HH:mm:ss"));
		//obj.html(obj.attr('columnNum'));
    	obj = null;
    }
 //绘座位图
    var switchSize = function(t,e){
    	var mainl = $(top.document).find('.mainl');
    	var mainr = $(top.document).find('.mainr');
    	var mainFrame = $(top.document).find('#mainFrame');
    	var header = $(top.document).find('#header');
    	var footer = $(top.document).find('#footer');
    	
    	if(mainl.is(":hidden")){
    		//退出
    		mainl.show();
    		mainr.width(top.rightWidth);
    		header.show();
    		footer.show();
    	//	mainr.height(h);
    		setTimeout(function(){
    			mainFrame.height(h);
    		},1)
    		t.innerHTML = "全屏";
    	}else{
    		//全屏，第一次进入时也会执行
    		mainl.hide();
    		mainr.width(top.availWidth);
    		t.innerHTML = "退出全屏";
    		header.hide();
    		footer.hide();
    		//mainr.height(h);
    		setTimeout(function(){
    			mainFrame.height(h+60);
    		},1)
    		
    	}
    	
    	mainl = null;
    	mainr = null;
    	header = null;
    	t = null;
    }
    var drawSeatGraph=function(html,id){
    	var div = $('#'+id).html('');
		div.html(html);
	}
    var drawSeatGraphs=function(){
    	var areaNames = top.areaNames;
    	for(var i=0;i<areaNames.length;i++){
    		var areaName = areaNames[i];
    		drawSeatGraph(top[areaName],areaName);
    		readyEvents(areaName);
    	}
    }
    var h = $(document).height();//计算当前容器的高度
    $(document).ready(function(){
    	$('#title').delay("slow").fadeIn();
    	drawSeatGraphs();
    	$('#progress').hide();
    });
    //-->
</SCRIPT>
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

<script type="text/javascript" src="<%=path %>/static/jeegoocontext/jquery.jeegoocontext-2.0.0.min.js"></script>
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