<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>消费卡</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/css.css?1" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path%>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
<script type='text/javascript' src='<%=path %>/static/js/date.js'></script>
<script type="text/javascript" src="<c:url value="/static/js/encrypt.js"/>"></script>
<jsp:include page="../include.jsp" />
<style type="text/css">
#list td, th {
	white-space: nowrap;
}

#list2 td, th {
	white-space: nowrap;
}
</style>
<script type="text/javascript" src="<%=path%>/static/js/base/business/venue/order_manager.js"></script>
</head>

<body>
	<div class="mainline"></div>
	<div id="formview" class="rbox1"></div>
	<div id="list" class="rbox2">
		<div class="rcon2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mtable" align="center">
			</table>
		</div>
	</div>
	<div id="list1" class="rbox1"></div>
	<div id="list2" class="rbox2"></div>
	<div style="display: none;">
		<form id="fmVenueSalesDetail">
			<table width="100%" height="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>商品大类<span style="color: red;">*</span></td>
								<td><select id="bigId" name="bigId" class="required" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>商品小类<span style="color: red;">*</span></td>
								<td><select id="smallId" name="smallId" class="required" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>商品名称<span style="color: red;">*</span></td>
								<td><select id="goodsId" name="goodsId" class="required" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>数量<span style="color: red;">*</span></td>
								<td><input id="count" name="count" type="number" value="1" min="1" max="100" class="required"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>单价<span style="color: red;">*</span></td>
								<td><input id="price" name="price" type="number" value="0" min="0" max="10000" class="required"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>总价<span style="color: red;">*</span></td>
								<td><input id="total" name="total" type="number" value="0" min="0" max="10000" class="required"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>备注</td>
								<td><input id="pgRemark" name="pgRemark" type="text"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
			<input type="hidden" id="pgId" name="pgId" />
		</form>
	</div>
	<div style="display: none;">
		<form id="fmCheckCard" class="validate">
			<table width="100%" height="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td width="100">卡号<span style="color: red;">*</span></td>
								<td width="100"><input id="cardId" name="cardId" type="text" class="required" minlength="8" maxlength="18"></input>
								</td>
							</tr>
							<tr>
								<td>原密码<span style="color: red;">*</span></td>
								<td><input id="password" name="password" type="password" class="required" minlength="6" maxlength="6"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
	<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/VenueDwr.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/MemberDwr.js'></script>
	<script type='text/javascript' src='<%=path%>/static/js/LodopFuncs.js'></script>
</body>
</html>