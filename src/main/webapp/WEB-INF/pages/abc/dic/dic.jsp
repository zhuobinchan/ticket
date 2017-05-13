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
<script type="text/javascript" src="<%=path %>/static/js/tool.js?v=<%=new java.util.Date().getTime() %>"></script>
<link rel="stylesheet" href="<%=path %>/static/ztree/css/metroStyle/metroStyle.css" type="text/css">
<script type="text/javascript" src="<%=path%>/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<SCRIPT type="text/javascript">
    <!--
	var treeSetting = {
			async: {
				enable: true,
				url:"<c:url value="/public/dic/asTree.htmls"/>",
				autoParam:["id=pid"]
			},
			view:{
				showTitle: true,
				selectedMulti : false
			},callback:{
				onClick: function(event,treeId,node){
					currentNode = node;
					var arr = [];
					arr.push({or:[{parentId:currentNode.id},{id:currentNode.id}]});
					var data = {recordId: currentNode.id,data:arr};
					grid.load(data);
					ajustHeight3();
				}
			}
	};
    function ajustHeight3(){
    	var h = $(top).height()-80;
    	var elements = $("body").children(":not(script)");
    	var eh = 0;
    	var element = null;
    	elements.each(function(i,div){
    		eh += $(div).height();
    		element = div;
    	})
    	if(eh>h){
    		$(top.document).find("iframe[name=mainFrame]").height(eh+150);
    		$(top.document).find("div.mainl").height(eh+150);
    		$('#treeDemo').height(eh+150);
    		$('.ltreebox').height(eh+150);
    		$('.rcon').height(eh+150);
    	}else{
    		$(top.document).find("iframe[name=mainFrame]").height(h);
    		$(top.document).find("div.mainl").height(h);
    		$(element).height((h-eh)+$(element).height());
    	}
    }
    genDicJs(['0101']);
	var gridSetting = {
			id:'dicGrid',
			po:"Dic",
			title:'数据字典列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "tier,orderno,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'text',
				   descs:"词条描述",
				   editable: true,
				   align:"center",
				   width:300
			   }  ,{
				   name: 'code',
				   descs:"词条索引",
				   align:"center",
				   editable: true,
				   width:300
			   }    
			   ,{
				   name: 'orderno',
				   descs:"排序",
				   type:'int',
				   editable:true,
				   defaultValue : 1,
				   align:"center",
				   width:90
			   }      
			],
			toolbar:{align:'right',buttons:[{
		    	 id:'add1',
		    	 title:'增加一级菜单',
		    	 width:70,
		    	 handlers:{
		    		 click: function(){
		    			 var nodes = treeObj.getSelectedNodes();
		    			 var newRow = grid.newRow();
		    		 }
		    	 }
		     },
			   {
			    	 id:'add',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var nodes = treeObj.getSelectedNodes();
			    			 var newRow = grid.newRow();
			    			 if(nodes.length){
			    				 var parentId = currentNode.id;
			    				 grid.addExData(newRow,{parentId:parentId});
			    			 }else{
			    				 grid.addExData(newRow,{parentId:''});
			    			 }
			    		 }
			    	 }
			     }, {
			    	 id:'save',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 var nodes = treeObj.getSelectedNodes();
			    			 var id = "";
			    			 if(nodes.length>0){
			    				 id = nodes[0].id;
			    			 }
			    			 grid.save(id,"removeDicCache",function(ret){
			    				 if(ret==-1){
			    					 layer.msg("保存失败，请重试！");
			    				 }else{
			    					 var currentNodes = treeObj.getSelectedNodes();
			    					 if(currentNodes.length==1){
			    						 refreshNode(currentNodes[0]);
			    					 }else{
			    						 $.fn.zTree.init($("#treeDemo"), treeSetting);
			    					 }
			    					 layer.msg("数据已保存！");
			    					 grid.reload("1");
			    				 }
			    			 })
			    		 }
			    	 }
			     }
			     , {
			    	 id:'delete',
			    	 title:'删除',
			    	 handlers:{
			    		 click: function(){
			    			 grid.deletes(function(ret){
			    				 if(ret==-1){
			    					 layer.msg("删除失败，请重试！");
			    				 }else{
			    					 layer.msg("删除" + ret.recordNum + "条记录！");
			    					 var id = grid.getSelectedRows().attr("recordId");
					    			 var currentNodes = treeObj.getNodesByParam("id", id, null);
					    			 if(currentNodes.length==1){
					    				 refreshNode(currentNodes[0]);
						    			 grid.reload();
					    			 }
			    					
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }, {
			    	 id:'move',
			    	 title:'移动节点',
			    	 handlers:{
			    		 click: function(){
			    			 var ses = grid.getSelectedRows();
			    			 if(ses.length>0){
			    				 art.dialog.open('<c:url value="/r2.html"/>?pagedir=abc/navi/navi_node&id='+ses.attr("recordId")+"&height=400",
							    	{id:'changeOrgW',title: '选择要移动到哪个节点下',width:500,height:400}
							    );
			    			 }else{
			    				 art.dialog.tips("请选择要移动的节点记录！");
			    			 }
			    		 }
			    	 }
			     }
			 ]}
		};
	function refreshNode(node) {
		var open = node.open;
		var id = node.id;
		treeObj.reAsyncChildNodes(node.getParentNode(), "refresh", true);
		setTimeout(function(){
			treeObj.reAsyncChildNodes(node, "refresh", true);
			node = treeObj.getNodeByParam("id", id, null);
			if(node){
				treeObj.selectNode(node);
				if(open){
					treeObj.expandNode(node, true, true, true);
				}
			}
		},1);
	}
	var grid,treeObj;
    $(document).ready(function(){
    	$.fn.zTree.init($("#treeDemo"), treeSetting);
    	treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    	grid = new GridView
    	("list",gridSetting,"list");
		grid.init();
		grid.load({data:{id:''}});
		ajustHeight();
		var h = 0;
		$("body>div.mainline,body>div.rtit").each(function(i,v){
			h += $(v).height();
		})
		$("div.rcon").height($(document).height()-h-35);
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="mainline"></div>
<div class="rtit">数据字典管理</div>
<div class="rcon">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="208" valign="top">
    <div class="lshulist mr12">
      <div class="lshulistt">数据字典树</div>
      <div class="ltreebox">
      <div id="role"
						style="width: 100%;height:300px;  line-height: 100px; overflow: auto; overflow-x: hidden;">
        <ul id="treeDemo" class="ztree"></ul>
        </div>
      </div>
    </div>
    </td>
    <td valign="top">
<div class="clear10"></div>
<div id="list"></div>
    </td>
  </tr>
</table>

        
        </div>
</body>
</html>