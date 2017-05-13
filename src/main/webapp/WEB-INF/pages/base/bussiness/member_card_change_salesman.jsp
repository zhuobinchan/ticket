<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>会员卡更换营销员</title>
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
		$("#fmMemberCardChangeSalesman").validate({
			submitHandler : function(form) {
				//submitReadyParas();
			}
		});
	});
</script>
</head>

<body style="margin-top: 60px">
	<form id="fmMemberCardChangeSalesman" class="validate">
		<table align="center">
			<tr>
				<td>新营销员</td>
				<td id="tdSalesmanId"></td>
			</tr>
		</table>
	</form>
</body>
</html>
