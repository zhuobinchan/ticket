<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>商品点单</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/css.css?1" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/dhtmlx/codebase/dhtmlx.css" />
<script type="text/javascript" src="<%=path%>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/formview.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
<script type='text/javascript' src='<%=path%>/static/js/date.js'></script>
<script type='text/javascript' src='<%=path%>/static/dhtmlx/codebase/dhtmlx.js'></script>
<script type="text/javascript" src="<c:url value="/static/js/encrypt.js"/>"></script>
<jsp:include page="/WEB-INF/pages/base/bussiness/include.jsp" />
<script type="text/javascript" src="<%=path%>/static/js/underscore.js"></script>
<script type='text/javascript' src='<%=path%>/static/My97DatePicker/WdatePicker.js'></script>
<script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
<script type='text/javascript' src='<%=path%>/dwr/interface/VenueDwr.js'></script>
<script type='text/javascript' src='<%=path%>/dwr/interface/MemberDwr.js'></script>
<script type='text/javascript' src='<%=path%>/static/js/LodopFuncs.js'></script>
<style type="text/css">
#list td, th {
	white-space: nowrap;
}

#list2 td, th {
	white-space: nowrap;
}
</style>
<script type="text/javascript" src="<%=path%>/static/js/abc/setting/parameter.js"></script>
</head>

<body style="width: 100%; height: 100%; margin: 0px; overflow: hidden;">
</body>
</html>