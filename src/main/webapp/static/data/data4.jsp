<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="content-type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
    <script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
    

  </head>
  
  <body>
  <script type="text/javascript">
	var areaNames = null;
    var areaMap = null;
	var valueVar = function(areas,start,length){
		for(var i=start;i<=length;i++){
			var list = areaMap[areaNames[i]];
			var g = new top.Graph(list,areaNames[i],top.ticketData);
			top.window[areaNames[i]] = g.drawSeatGraph(list,areaNames[i],top.ticketData);
		}
	}
	
    $(function(){
    	top.setBase();
    	areaNames = top.areaNames;
    	areaMap = top.areaMap ;
    	valueVar(top.areas,12,14);
    })
</script>
  </body>
</html>
