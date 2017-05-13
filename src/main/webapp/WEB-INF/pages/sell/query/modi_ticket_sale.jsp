<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<link rel="stylesheet" href="<%=path %>/static/special/css/bootstrap.min.css" />
<script type="text/javascript">
$(function(){
	var payType = parent.grid.getSelectedRows().find("td[name=payType]").attr("value");
	dicSelect(dic0207).width(150).appendTo($('td[name=payType]')).attr("id","payType").val(payType);
	var options =$("#payType option").filter(function(e){
		if(this.value==="0207001"||this.value==="0207002"||this.value==="0207004"){
			return false;
		}
		return true;
	}).remove();
})
genDicJs(['0207']);

</script>
  </head>
  
  <body style="margin-top:60px">
    <table align="center">
    <tr>
    <td width="80">支付方式</td>
    <td name="payType">
    
    </td>
    </tr>
    </table>
  </body>
</html>
