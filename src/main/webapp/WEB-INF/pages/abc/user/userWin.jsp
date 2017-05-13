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
<script type="text/javascript" src="<%=path %>/static/js/gridview.js?v=1"></script>
<script type="text/javascript" src="<%=path %>/static/js/tool.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/underscore.js"></script>
<script type="text/javascript" src="<%=path %>/static/js/backbone-min.js?v=1"></script>
<script type="text/javascript" src="<%=path%>/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<link rel="stylesheet" href="<%=path %>/static/ztree/css/metroStyle/metroStyle.css" type="text/css">
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
					var json = {orgId:currentNode.id};
					var data = {recordId: currentNode.id,data:json};
					grid.load(data);
				}
			}
	};
    genDicJs(['0102']);
    var a = 12;
	var gridSetting = {
			id:'userGrid',
			po:"User",
			className:"mtable4",
			height:'fixed',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "userCode,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"姓名",
				   align:'center',
				   width:300
			   }      
			   ,{
				   name: 'userCode',
				   descs:"用户账号",
				   align:'center',
				   width:260
			   }  
			     
			]
		};
	var grid,treeObj;
    $(document).ready(function(){
    	$.fn.zTree.init($("#treeDemo"), treeSetting);
    	treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    	grid = new GridView("list",gridSetting,"list");
    		grid.rowClick(function(id){
		});
		grid.init();
		grid.load();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rcon">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="208" valign="top">
    <div class="lshulist mr12">
      <div class="lshulistt">组织机构树</div>
      <div class="ltreebox">
        <ul id="treeDemo" class="ztree"></ul>
      </div>
    </div>
    </td>
    <td valign="top">
<div id="list" style="height:340px;overflow: auto"></div>
    </td>
  </tr>
</table>

        
        </div>
</body>
</html>