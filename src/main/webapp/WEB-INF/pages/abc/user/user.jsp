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
<jsp:include page="/WEB-INF/pages/base/bussiness/include.jsp" />
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
    function selectOrg(e,obj,title){
    	popSelectWin(obj,title,g_path+"/public/common/r2.htmls?page=abc/org/org_win",function(index,layero){
    		var nodes = $(layero.selector+" iframe")[0].contentWindow.treeObj.getSelectedNodes();
	    	if(nodes.length>0){
	    		obj.closest("td").attr("value",nodes[0].id).html(nodes[0].name)
	    	}
	    	layer.close(index);
    	},300,450);
    }
    function popSelect2(id,obj){
    	selectOrg(event,obj,"选择部门");
    }
	var gridSetting = {
			id:'userGrid',
			po:"User",
			title:'用户列表',
			height:'auto',
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
				   editable: true,
				   align:'center',
				   width:150
			   }      
			   ,{
				   name: 'userCode',
				   descs:"用户账号",
				   editable: true,
				   align:'center',
				   width:150
			   } ,{
				   name: 'theaterSn',
				   descs:"剧场编号",
				   editable: true,
				   align:'center',
				   width:150
			   } ,{
				   name: 'orgId',
				   descs:"所属部门",
				   editable: true,
				   width:220,
				   type:'pop',
				   align:"center"
			   }  
			   ,{
				   name: 'gender',
				   descs:"性别",
				   editable: true,
				   type:'select',
				   align:"center",
				   width:100
			   }    ,{
				   name: 'fetureLetter',
				   descs:"流水特征字",
				   editable: true,
				   align:"center"
			   }    
			],
			toolbar:{align:'right',buttons:[
			   {
			    	 id:'add',
			    	 title:'增加',
			    	 handlers:{
			    		 click: function(){
			    			 var nodes = treeObj.getSelectedNodes();
			    			 if(nodes.length){
			    				 if(nodes[0].isParent){
			    					 layer.msg("该机构无法挂靠人员");
			    				 }else{
			    					 var newRow = grid.newRow();
				    				 var orgId = currentNode.id;
				    				 grid.addExData(newRow,{orgId: orgId});
			    				 }
			    				
			    			 }else{
			    				 layer.msg("请选择组织机构");
			    			 }
			    		 }
			    	 }
			     }, {
			    	 id:'save',
			    	 title:'保存',
			    	 handlers:{
			    		 click: function(){
			    			 var rows = grid.getSelectedRows();
			    			 var id = "";
			    			 if(rows.length>0){
			    				 id = $(rows[0]).attr('recordId');
			    			 }
			    			 grid.save(id,"setUserPassword",function(ret){
			    				 if(ret==-1){
			    					 layer.msg("保存失败，请重试！");
			    				 }else{
			    					 layer.msg("数据已保存！");
			    					 grid.reload(id);
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
			    					 grid.reload();
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }
			 ]}
		};
	var gridSetting2 = {
			id:'roleGrid',
			po:"UserRole",
			title:'用户角色列表',
			height:'auto',
			service:"uiServices!listSimple",
			pageInfo: {
				records: -1,//auto表示根据容器高度自动判断
				orderby : "roleId,id"
			},
			fields:[
			   {	name:'id',
					hide:true
			   },{
				   name: 'roleName',
				   descs:"角色名称",
				   align: 'center',
				   width:300
			   }      
			   ,{
				   name: 'roleDesc',
				   descs:"角色描述",
				   align: 'center',
				   width:260
			   }  
			],
			toolbar:{align:'right',buttons:[
				{
					 id:'addRole',
					 title:'分配角色',
					 handlers:{
						 click: function(){
							 var userRows = grid.getSelectedRows();
							 if(userRows.length){
								 var userId = $(userRows[0]).attr('recordId');
								 layer.open({
									 title:'分配角色',
									 type:2,
									 area:['600px','400px'],
									 content:"<%=path%>/public/common/r2.htmls?page=abc/user/role"
								 });
							 }else{
								 layer.alert("请选择用户");
							 }
							 
						 }
					 }
				}  ,
				{
			    	 id:'delete2',
			    	 title:'删除角色',
			    	 handlers:{
			    		 click: function(){
			    			 grid2.deletes(function(ret){
			    				 if(ret==-1){
			    					 layer.msg("删除失败，请重试！");
			    				 }else{
			    					 layer.msg("删除" + ret.recordNum + "条记录！");
			    					 grid2.reload();
			    				 }
			    			 });
			    			 
			    		 }
			    	 }
			     }
			  ]
			}
		};
	var grid,treeObj,grid2;
    $(document).ready(function(){
    	$.fn.zTree.init($("#treeDemo"), treeSetting);
    	treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    	grid = new GridView("list",gridSetting,"list");
    	grid.rowClick(function(id){
			grid2.load({data:{userId:id}});
			$('div.rcon').height($('div.rcon table').height() + parseInt($('.rcon').css('padding-top')) + 
					parseInt($('.rcon').css('padding-bottom')));
			ajustHeight2();
		});
		grid.init();
		grid.load();
		
    	grid2 = new GridView("list2",gridSetting2,"list2");
		grid2.init();
		$('div.rcon').height($('div.rcon table').height() + parseInt($('.rcon').css('padding-top')) + 
				parseInt($('.rcon').css('padding-bottom')));
		ajustHeight2();
    });
    //-->
</SCRIPT>
</head>

<body>
<div class="rtit">用户管理</div>
<div class="rcon">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="208" valign="top">
	    <div class="lshulist mr12">
	      <div class="lshulistt">组织机构树</div>
	      <div class="ltreebox">
	      <div id="role"
						style="width:100%;height:300px;  line-height: 100px; overflow: auto; overflow-x: hidden;">
	        <ul id="treeDemo" class="ztree"></ul>
	        </div>
	      </div>
	    </div>
    </td>
    <td valign="top">
		<div id="list"></div>
		<div id="list2"></div>
    </td>
  </tr>
</table>

        
        </div>
</body>
</html>