<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>会员卡类别</title>
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
</style>
<script type="text/javascript" src="<%=path%>/static/js/base/business/member_card_type.js"></script>
<script type="text/javascript">
	var currentUserId = ${sessionScope.userInfo.id};
</script>
</head>

<body>
	<div class="mainline"></div>
	<div id="formview" class="rbox1"></div>
	<div id="list" class="rbox2"></div>
	<div class="rbox2">
		<div class="rcon"></div>
	</div>

	<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
	<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/BaseDwr.js'></script>
	<script type='text/javascript' src='<%=path%>/dwr/interface/MemberDwr.js'></script>
</body>
</html>