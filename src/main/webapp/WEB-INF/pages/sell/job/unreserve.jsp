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
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<SCRIPT type="text/javascript">
    <!--
    var data = parent.data;
    var ticketArr = parent.ticketArr;
    var strategys = genMaps("from TicketStrategy order by orderno");
    var areas = genMaps("from Area order by code");
    var closeWin = function(){
    	var index = parent.layer.getFrameIndex(window.name);
	    parent.layer.close(index);
    }
    $(function(){
    	var table = $('#itemTable');
    	var totalSize = 0,amount = 0;
    	var arr = _.map(data,function(value,key){
    		var area = areas[key];
    		if(area){
    			var size = data[key].length; 
        		totalSize += size;
        		var money = size * area.price ;
        		amount += money;
        		return '<tr name="itemtr"><td align="center" width="140">'+area.name+'</td><td style="text-align:center" width="70" name="price">'+area.price 
        		+'</td><td style="text-align:center" name="size" width="70">'+size+'</td><td style="text-align:center" width="70">'
        		+money+'</td><td align="center" width="70" name="realPrice">'+area.price+'</td><td align="center" name="money">'+money+'</td></tr>';
    		}
    	})
    	
    	for(var i=0;i<arr.length;i++){
    		$(arr[i]).appendTo(table);
    	}
    	$('<tr style="color:green;font-weight:bold"><td align="center">总计</td><td align="center">---</td><td name="totalSize" align="center">'+totalSize
    		+'</td><td align="center">'+amount+'</td><td align="center">---</td><td align="center" name="amount">'+amount+'</td></tr>').appendTo(table);
    	$("#ok").click(function(e){
    		BaseDwr.unreserve(JSON.stringify(ticketArr),function(ret){
    			closeWin();
    		});
    	})
    	$('#closeBtn').click(function(e){
    	    closeWin();
    	})
    })
   
    //-->
</SCRIPT>
</head>

<body>
<div class="rcon" style="height:300px">
<table width="100%" class="mtable4" id='itemTable'>
<tr>
<th>片区</th>
<th>价格</th>
<th>人数</th>
<th>金额</th>
<th>优惠价</th>
<th>应付金额</th>
</tr>
</table>
<br/>
<table width="100%" class="ctable" id='mainTable'>
<tr>
<td align="center"><a href="#" id="ok" class="btn3">取消预订</a><a href="#" id="closeBtn" class="btn4">取消</a></td>
</tr>
</table>

        </div>
<script type='text/javascript' src='<%=path %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path %>/dwr/interface/BaseDwr.js'></script>
</body>
</html>