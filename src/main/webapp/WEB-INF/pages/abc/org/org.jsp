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
					
					if(node.type===0)
						grid.setDefaultValue("objType",'101101');
					else if(node.type===1){
						grid.setDefaultValue("objType",'101102');
					}
					grid.load(data);
				},
				onExpand:function(event, treeId, treeNode){
					//hf();
				},onCollapse:function(event, treeId, treeNode){
				//	hf();
				}
			}
	};
    genDicJs(['0101']);
	var gridSetting = {
			id:'organizationGrid',
			po:"Organization",
			title:'组织机构列表',
			height:'auto',
			service:"organizationServices!listOrgs",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "tier,orderno,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'name',
				   descs:"机构名称",
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
			toolbar:{align:'right',buttons:[
			{
				 id:'add1',
				 title:'增加一级机构',
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
			    			 grid.save(id,"setTier",function(ret){
			    				 if(ret==-1){
			    					 layer.msg("保存失败，请重试！");
			    				 }else{
			    					 var currentNodes = treeObj.getSelectedNodes();
			    					 if(currentNodes.length==1){
			    						 //treeObj.reAsyncChildNodes(currentNodes[0], "refresh");
			    						 refreshNode(currentNodes[0]);
			    						// setTimeout(refreshNode,100);
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
			    			 var currentNodes = treeObj.getSelectedNodes();
			    			 if(currentNodes.length>0){
			    				 var url = g_path+'/public/common/r2.htmls?page=abc/navi/move_node';
			    				 popSelectWin(null,"移动到指定节点",url,function(index,layero){
			    					 var nodes = $(layero.selector+" iframe")[0].contentWindow.treeObj.getSelectedNodes();
			    				    	if(nodes.length>0){
			    				    		$.ajax({
			    				    			url:'<%=path%>/public/org/moveNode.htmls',
			    				    			type:'POST',
			    				    			dataType:'json',
			    				    			data:{sourceId:currentNodes[0].id,destId:nodes[0].id},
			    				    			success:function(ret){
			    				    				afterOp();
			    				    			}
			    				    		})
			    				    	}
			    				    	layer.close(index);
			    				 },400,500)
			    			 }else{
			    				 layer.msg("请选择要移动的节点记录！",{icon:6,title:"提示信息"});
			    			 }
			    		 }
			    	 }
			     }
			 ]}
		};
	function afterOp(){
		var currentNode = treeObj.getSelectedNodes()[0];
		treeObj.reAsyncChildNodes(currentNode.getParentNode(), "refresh");
		setTimeout(refreshNode,300);
		grid.reload();
	}
	function refreshNode(node) {
		var open = node.open;
		var id = node.id;
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
		ajustHeight2();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rtit">组织机构管理</div>
<div class="rcon">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="208" valign="top">
    <div class="lshulist mr12">
      <div class="lshulistt">组织机构树</div>
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