<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>会员卡发卡</title>
<script type="text/javascript" src="<%=path%>/static/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=path%>/static/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/static/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
<script type="text/javascript">
	$(function() {
		var salesMap = genMaps("from Salesman order by name,id");
		joinSelect(salesMap, "salesmanId").width(100).appendTo($('td[id=tdSalesmanId]'));
		$("#fmNewMemberCard").validate({
			submitHandler : function(form) {
				//submitReadyParas();
			}
		});
	});
</script>
</head>

<body style="margin-top: 60px">
	<form id="fmNewMemberCard" class="validate">
		<table align="center">
			<tr>
				<td width="50">卡号</td>
				<td width="80"><input id="cardId" type="text" class="required">
				</td>
			</tr>
			<tr>
				<td>数量</td>
				<td><input id="count" type="text" value="1" min="1" max="500"
					class="required"></td>
			</tr>
			<tr>
				<td>营销员</td>
				<td id="tdSalesmanId"></td>
			</tr>
		</table>
	</form>
</body>
</html>
