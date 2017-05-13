<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商品管理</title>
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
<script type="text/javascript" src="<%=path%>/static/js/base/business/venue/goods_type.js"></script>
</head>

<body>
	<div class="mainline"></div>
	<div id="formview" class="rbox1"></div>
	<div id="list" class="rbox2"></div>
	<div class="rbox2">
		<div class="rcon"></div>
	</div>
	<div style="display: none;">
		<form id="fmBigType">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td width="100">大类名称<span style="color: red;">*</span></td>
								<td width="100"><input id="bigName" name="bigName" type="text" class="required" style="width: 100%;"></input>
								</td>
							</tr>
							<tr style="display: none;">
								<td width="100">大类排序<span style="color: red;">*</span></td>
								<td width="100"><input id="bigOrder" name="bigOrder" type="number" value="1" min="1" max="999"
									class="required" style="width: 100%;"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
			<input type="hidden" id="bigId" name="bigId" />
		</form>
	</div>
	<div style="display: none;">
		<form id="fmDeleteBigType">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td width="100">大类<span style="color: red;">*</span></td>
								<td width="100"><select id="deleteBigTypeId" name="deleteBigTypeId" class="required" style="width: 100%"></select></td>
							</tr>
						</table></td>
				</tr>
			</table>
		</form>
	</div>
	<div style="display: none;">
		<form id="fmSmallType">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>大类<span style="color: red;">*</span></td>
								<td><select id="bigTypeId" name="bigTypeId" class="required" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td width="100">小类名称<span style="color: red;">*</span></td>
								<td width="100"><input id="smallName" name="smallName" type="text" class="required" style="width: 100%"></input></td>
							</tr>
							<tr>
								<td width="100">小类说明</td>
								<td width="100"><input id="smallText" name="smallText" type="text" style="width: 100%"></input></td>
							</tr>
							<tr style="display: none;">
								<td width="100">小类排序<span style="color: red;">*</span></td>
								<td width="100"><input id="smallOrder" name="smallOrder" type="number" value="1" min="1" max="999"
									class="required" style="width: 100%;"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
			<input type="hidden" id="smallId" name="smallId" />
		</form>
	</div>

	<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
	<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/VenueDwr.js'></script>
</body>
</html>