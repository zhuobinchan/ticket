<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>售票管理系统</title>
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" type="text/css" href="static/css/css.css" />
<script type="text/javascript" src="static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/main.js"></script>
<script type="text/javascript" src="static/layer/layer.js"></script>
<script type="text/javascript" src="static/js/tool.js"></script>
<script type="text/javascript" src="static/js/json2.js"></script>
<script type="text/javascript" src="static/js/underscore.js"></script>
<jsp:include page="/WEB-INF/pages/base/bussiness/include.jsp" />
<style type="text/css">
	a.active{
		color:red;
	}
	a.visited{
		color:yellow;
	}
</style>
</head>

<body>
<%-- <div id="htmlstr" style="display: none">
<c:out value="${areaMap}"/>
</div> --%>
<div id="wrap">
  <div id="openmenu" title="展开导航栏"></div>
<%@include file="/WEB-INF/pages/abc/home/top.jspf"%>
  <div id="mainer">
    <div class="main">
      <div class="mainl">
        <div class="mainlt"><img src="static/images/b2.png" class="img" />用户: <a href="#"><b>${userName }</b></a><br /><span>电子售票系统</span></div>
        <div class="mainlm">
          <ul>
            
          </ul>
        </div>
      </div>
      <div class="mainr">
      <div class="mainline"></div>
		 <iframe name='mainFrame' id="mainFrame" src="static/main.jsp" frameborder="0" scrolling="no" width="100%"></iframe>
      </div>
      <div class="clear"></div>
    </div>
  </div>

   <div id="footer">
    <div class="fcopy">版权所有©红太阳集团</div>
  </div>
  
</div>

<form id="logoutForm" action="<%=path %>/public/logoutSys.htmls" method="get"></form>
<script type="text/javascript" src="static/js/index.js"></script>
<script type='text/javascript' src='<%=path %>/static/js/date.js'></script>
<%@include file="/WEB-INF/pages/abc/home/seatData.jspf"%>
<iframe src="static/data/data.jsp" id='dataFrame' style='display:none' height="100"></iframe>
<iframe src="static/data/data2.jsp" id='dataFrame2' style='display:none' height="100"></iframe>
<iframe src="static/data/data3.jsp" id='dataFrame3' style='display:none' height="100"></iframe>
<iframe src="static/data/data4.jsp" id='dataFrame4' style='display:none' height="100"></iframe>
<iframe src="static/data/data5.jsp" id='dataFrame5' style='display:none' height="100"></iframe>
<iframe src="static/data/data6.jsp" id='dataFrame6' style='display:none' height="100"></iframe>
<iframe src="static/data/data7.jsp" id='dataFrame7' style='display:none' height="100"></iframe>
</body>
</html>