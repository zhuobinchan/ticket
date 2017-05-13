<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=path %>/static/css/css.css" />
<link rel="stylesheet" type="text/css" href="<%=path %>/static/jquery-ui/jquery-ui.min.css" />
<script type="text/javascript" src="<%=path %>/static/js/json2.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/static/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/gridview.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<link rel="stylesheet" href="<%=path %>/static/ztree/css/metroStyle/metroStyle.css" type="text/css">
<script type="text/javascript" src="<%=path%>/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<SCRIPT type="text/javascript">
    <!--
	var treeSetting = {
			async: {
				enable: true,
				url:"<c:url value="/public/org/asTree.htmls"/>",
				autoParam:["id=pid"]
			},
			view:{
				showTitle: true,
				selectedMulti : false
			},callback:{
				onClick: function(event,treeId,node){
					currentNode = node;
					var json = {id:currentNode.id};
					var data = {recordId: currentNode.id,data:json};
				}
			}
	};
    genDicJs(['0101']);
	var treeObj;
    $(document).ready(function(){
    	$('.rcon').height($(document).height()-122);
    	$.fn.zTree.init($("#treeDemo"), treeSetting);
    	treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rcon" style="height:400px">
        <ul id="treeDemo" class="ztree"></ul>
      </div>
</body>
</html>