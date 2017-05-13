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
<script type="text/javascript" src="<%=path%>/static/js/base/business/venue/goods.js"></script>
</head>

<body>
	<div class="mainline"></div>
	<div id="formview" class="rbox1"></div>
	<div id="list" class="rbox2"></div>
	<div class="rbox2">
		<div class="rcon"></div>
	</div>
	<div style="display: none;">
		<form id="fmGoodsInfo">
			<table width="100%">
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
								<td width="100">名称<span style="color: red;">*</span></td>
								<td width="100"><input id="name" name="name" type="text" class="required"></input></td>
							</tr>
							<tr>
								<td>单位</td>
								<td><select id="unit" name="unit" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>单价<span style="color: red;">*</span></td>
								<td><input id="price" name="price" type="number" value="0" min="0" max="10000" class="required"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>提成比例<span style="color: red;">*</span></td>
								<td><select id="commision" name="commision" class="required" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>是否打折</td>
								<td><select id="discount" name="discount" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>是否配送</td>
								<td><select id="dispatch" name="dispatch" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>编码</td>
								<td><input id="code" name="code" type="text"></input></td>
							</tr>
							<tr>
								<td>是否加工</td>
								<td><select id="process" name="process" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>成本</td>
								<td><input id="cost" name="cost" type="number" value="0" min="0" max="10000" style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>出品区域</td>
								<td><select id="area" name="area" style="width: 100%"></select></td>
							</tr>
							<tr>
								<td>条形码</td>
								<td><input id="barCode" name="barCode" type="text"></input></td>
							</tr>
							<tr>
								<td width="100">排序<span style="color: red;">*</span></td>
								<td width="100"><input id="order" name="order" type="number" value="1" min="1" max="999" class="required"
									style="width: 100%;"></input></td>
							</tr>
							<tr>
								<td>备注</td>
								<td><input id="remark" name="remark" type="text"></input></td>
							</tr>
						</table></td>
				</tr>
			</table>
			<input type="hidden" id="goodsId" name="goodsId" />
		</form>
	</div>
	<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
	<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/VenueDwr.js'></script>
</body>
</html>