<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/swi.js"></script>
<script type="text/javascript">
	SWI.contentPath = '${pageContext.request.contextPath}';
	SWI.currentUserName = '${sessionScope.userInfo.name}';
	SWI.currentUserId = '${sessionScope.userInfo.id}';
</script>
</head>
<body>

</body>
</html>