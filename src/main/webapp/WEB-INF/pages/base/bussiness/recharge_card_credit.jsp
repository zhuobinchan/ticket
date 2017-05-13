<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>充值卡挂账处理</title>
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
<script type="text/javascript" src="<%=path%>/static/js/swi.js"></script>
<script type="text/javascript" src="<c:url value="/static/js/encrypt.js"/>"></script>
<style type="text/css">
#list td, th {
	white-space: nowrap;
}

#list2 td, th {
	white-space: nowrap;
}

.ui-autocomplete {
	z-index: 198910151;
}
</style>
<script type="text/javascript" src="<%=path%>/static/js/base/business/recharge_card_credit.js"></script>
</head>

<body>
	<div class="mainline"></div>
	<div id="formview" class="rbox1"></div>
	<div id="list" class="rbox2"></div>
	<div class="rbox2">
		<div class="rcon"></div>
	</div>
	<div style="display: none;">
		<form id="fmRecoverCredit" class="validate">
			<table width="100%">
				<tr>
					<td align="center"><table align="center">
							<tr>
								<td>收回金额<span style="color: red;">*</span></td>
								<td><input id="money" name="money" type="number" value="0" min="0" max="10000" class="required"
									style="width: 100%"></input></td>
							</tr>
							<tr>
								<td>充值方式<span style="color: red;">*</span></td>
								<td><select id="payType" name="payType" style="width: 100%;" class="required"></select></td>
							</tr>
							<tr>
								<td>还款网点<span style="color: red;">*</span></td>
								<td><select id="branch" name="branch" style="width: 100%;" class="required"></select></td>
							</tr>
							<tr>
								<td>营销员<span style="color: red;">*</span></td>
								<td><input uitype="autocomplete" type="text" name="repaySalesmanId4Layer_txt" id="repaySalesmanId4Layer_txt"
									class="ui-autocomplete-input required" autocomplete="off" style="width: 100%"> <input
										uitype="autocomplete" type="hidden" name="repaySalesmanId4Layer" id="repaySalesmanId4Layer"></td>
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
	<script type='text/javascript' src='<%=path%>/dwr/interface/BaseDwr.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/MemberDwr.js'></script>
</body>
</html>