<%@page contentType="text/html; charset=gb2312"%>
<%
String path = request.getContextPath();
 %>
<html>
<head>

<script language="JavaScript">	
	function openMainUI(){
		var height = screen.height - 61;
		var width = screen.width;	
		var newwin = window.open("<%=path%>/index.htmls","newwindow",
			"loaction=no,toolbar=no,scrollbars=no,menubar=no,resizable=no,top=" + 0 + ",left=" + 0 + ",width="+width+",height="+height);
			window.opener=null;
    		window.open('','_top');
    		window.top.close();
			newwin.focus();
	}		
	</script>
</head>
<body onload="openMainUI();">

</body>
</html>